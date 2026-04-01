/*
 * Copyright (c) 2018-2026, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.uri.remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.ResponseErrorException;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode;
import org.rascalmpl.ideservices.GsonUtils;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.IExternalResolverRegistry;
import org.rascalmpl.uri.ISourceLocationWatcher;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.remote.jsonrpc.ISourceLocationRequest;
import org.rascalmpl.uri.remote.jsonrpc.RemoveRequest;
import org.rascalmpl.uri.remote.jsonrpc.SetLastModifiedRequest;
import org.rascalmpl.uri.remote.jsonrpc.WatchRequest;
import org.rascalmpl.uri.remote.jsonrpc.WriteFileRequest;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryClient;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryServer;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryServer.FileType;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryServer.FileWithType;
import org.rascalmpl.util.Lazy;
import org.rascalmpl.util.NamedThreadPool;
import org.rascalmpl.util.base64.StreamingBase64;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonPrimitive;

import io.usethesource.vallang.ISourceLocation;

public class RemoteExternalResolverRegistry implements IExternalResolverRegistry, IRemoteResolverRegistryClient {
    private volatile IRemoteResolverRegistryServer remote = null;

    private final Map<WatchSubscriptionKey, Watchers> watchers = new ConcurrentHashMap<>();
    private final Map<String, Watchers> watchersById = new ConcurrentHashMap<>();

    private final int remoteResolverRegistryPort;

    public RemoteExternalResolverRegistry(int remoteResolverRegistryPort) {
        this.remoteResolverRegistryPort = remoteResolverRegistryPort;
        connect();
    }

    private static final Duration LONGEST_TIMEOUT = Duration.ofMinutes(1);

    private void connect() {
        var timeout = Duration.ZERO;
        while (true) {
            try {
                Thread.sleep(timeout.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            var remote = startClient();
            if (remote != null) {
                this.remote = remote;
                return;
            }
            timeout = timeout.plusMillis(10);
            if (timeout.compareTo(LONGEST_TIMEOUT) >= 0) {
                timeout = LONGEST_TIMEOUT;
            }
        }
    }

    private void scheduleReconnect() {
        CompletableFuture.runAsync(() -> connect());
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T, E extends Exception> {
        T get() throws E;
    }
    
    @FunctionalInterface
    private interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    private interface ThrowingTriFunction<T, U, V, R, E extends Exception> {
        R apply(T t, U u, V v) throws E;
    }

    @FunctionalInterface
    private interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    private interface ThrowingTriConsumer<T, U, V, E extends Exception> {
        void accept(T t, U u, V v) throws E;
    }

    @FunctionalInterface
    private interface ThrowingRunnable<E extends Exception> {
        void run() throws E;
    }

    private InputStream errorDetectingInputStream(InputStream original) {
        return new InputStream() {
            private <T> T socketExceptionCatcher(ThrowingSupplier<T, IOException> function) throws IOException {
                try {
                    return function.get();
                } catch (SocketException e) {
                    scheduleReconnect();
                    throw e;
                }
            }
            
            private <T, R> R socketExceptionCatcher(ThrowingFunction<T, R, IOException> function, T arg) throws IOException {
                try {
                    return function.apply(arg);
                } catch (SocketException e) {
                    scheduleReconnect();
                    throw e;
                }
            }

            private <T, U, V, R> R socketExceptionCatcher(ThrowingTriFunction<T, U, V, R, IOException> function, T t, U u, V v) throws IOException {
                try {
                    return function.apply(t, u, v);
                } catch (SocketException e) {
                    scheduleReconnect();
                    throw e;
                }
            }
            
            @Override
            public int read() throws IOException {
                return socketExceptionCatcher(original::read);
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return socketExceptionCatcher(original::read, b, off, len);
            }

            @Override
            public int available() throws IOException {
                return original.available();
            }
            
            @Override
            public long skip(long n) throws IOException {
                return socketExceptionCatcher(original::skip, n);
            }

            @Override
            public void close() throws IOException {
                original.close();
            }
            
            @Override
            public byte[] readNBytes(int len) throws IOException {
                return socketExceptionCatcher(original::readNBytes, len);
            }

            @Override
            public int readNBytes(byte[] b, int off, int len) throws IOException {
                return socketExceptionCatcher(original::readNBytes, b, off, len);
            }
        };
    }

    private OutputStream errorDetectingOutputStream(OutputStream original) {
        return new OutputStream() {
            private <T> void socketExceptionCatcher(ThrowingConsumer<T, IOException> consumer, T arg) throws IOException {
                try {
                    consumer.accept(arg);
                } catch (SocketException e) {
                    scheduleReconnect();
                    throw e;
                }
            }

            private <T, U, V> void socketExceptionCatcher(ThrowingTriConsumer<T, U, V, IOException> consumer, T t, U u, V v) throws IOException {
                try {
                    consumer.accept(t, u, v);
                } catch (SocketException e) {
                    scheduleReconnect();
                    throw e;
                }
            }

            private void socketExceptionCatcher(ThrowingRunnable<IOException> runnable) throws IOException {
                try {
                    runnable.run();
                } catch (SocketException e) {
                    scheduleReconnect();
                    throw e;
                }
            }

            @Override
            public void write(int b) throws IOException {
                socketExceptionCatcher(original::write, b);
            }
            
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                socketExceptionCatcher(original::write, b, off, len);
            }

            @Override
            public void flush() throws IOException {
                socketExceptionCatcher(original::flush);
            }

            @Override
            public void close() throws IOException {
                original.close();
            }
        };
    }
    
    private IRemoteResolverRegistryServer startClient() {
        try {
            @SuppressWarnings("resource")
            var socket = new Socket(InetAddress.getLoopbackAddress(), remoteResolverRegistryPort);
            socket.setTcpNoDelay(true);
            Launcher<IRemoteResolverRegistryServer> clientLauncher = new Launcher.Builder<IRemoteResolverRegistryServer>()
                .setRemoteInterface(IRemoteResolverRegistryServer.class)
                .setLocalService(this)
                .setInput(errorDetectingInputStream(socket.getInputStream()))
                .setOutput(errorDetectingOutputStream(socket.getOutputStream()))
                .configureGson(GsonUtils.complexAsJsonObject())
                .setExecutorService(NamedThreadPool.cachedDaemon("rascal-remote-resolver-registry"))
                .create();

                clientLauncher.startListening();
                return clientLauncher.getRemoteProxy();
        } catch (RuntimeException | IOException e) {
            System.err.println("Error setting up remote resolver registry connection, will reconnect: " + e.getMessage());
            return null;
        }
    }

    private static <T, U> U call(Function<T, CompletableFuture<U>> function, T argument) throws IOException {
        try {
            return function.apply(argument).get(1, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            throw new IOException("Remote resolver took too long to reply; interrupted to avoid deadlocks");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new UnsupportedOperationException("Thread should have been interrupted");
        } catch (CompletionException | ExecutionException e) {
            var cause = e.getCause();
            if (cause != null) {
                if (cause instanceof ResponseErrorException) {
                    throw translateException((ResponseErrorException) cause);
                }
            }
            throw new IOException(e);
        }
    }

    private static final int JsonRpcErrorCode_Generic = -1;
    private static final int JsonRpcErrorCode_FileSystem = -2;
    private static final int JsonRpcErrorCode_NativeRascal = -3;

    private static IOException translateException(ResponseErrorException cause) {
        var error = cause.getResponseError();
        switch (error.getCode()) {
            case JsonRpcErrorCode_Generic:
                return new IOException("Generic error: " + error.getMessage());
            case JsonRpcErrorCode_FileSystem: {
                if (error.getData() instanceof JsonPrimitive) {
                    var data = (JsonPrimitive) error.getData();
                    if (data.isString()) {
                        switch (data.getAsString()) {
                            case "FileExists": // fall-through
                            case "EntryExists":
                                return new FileAlreadyExistsException(error.getMessage());
                            case "FileNotFound": // fall-through
                            case "EntryNotFound":
                                return new NoSuchFileException(error.getMessage());
                            case "FileNotADirectory": // fall-through
                            case "EntryNotADirectory":
                                return new NotDirectoryException(error.getMessage());
                            case "FileIsADirectory": // fall-through
                            case "EntryIsADirectory":
                                return new IOException("File is a directory: " + error.getMessage());
                            case "NoPermissions":
                                return new AccessDeniedException(error.getMessage());
                        }
                    }
                }
                return new IOException("File system error: " + error.getMessage() + " data: " + error.getData());
            }
            case JsonRpcErrorCode_NativeRascal:
                return new IOException("Rascal native schemes should not be forwarded");
            default:
                return new IOException("Missing case for: " + error);
        }
    }

    @Override
    public InputStream getInputStream(ISourceLocation loc) throws IOException {
        return StreamingBase64.decode(call(remote::readFile, new ISourceLocationRequest(loc)).getContent());
    }

    @Override
    public boolean exists(ISourceLocation loc) {
        try {
            return call(remote::exists, new ISourceLocationRequest(loc)).getValue();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public long lastModified(ISourceLocation loc) throws IOException {
        return call(remote::lastModified, new ISourceLocationRequest(loc)).getTimestamp();
    }

    @Override
    public long size(ISourceLocation loc) throws IOException {
        return call(remote::size, new ISourceLocationRequest(loc)).getNumber();
    }

    @Override
    public boolean isDirectory(ISourceLocation loc) {
        try {
            var cached = cachedDirectoryListing.getIfPresent(URIUtil.getParentLocation(loc));
            if (cached != null) {
                var result = cached.get().get(URIUtil.getLocationName(loc));
                if (result != null) {
                    return result;
                }
            }
            return call(remote::isDirectory, new ISourceLocationRequest(loc)).getValue();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isFile(ISourceLocation loc) {
        try {
            var cached = cachedDirectoryListing.getIfPresent(URIUtil.getParentLocation(loc));
            if (cached != null) {
                var result = cached.get().get(URIUtil.getLocationName(loc));
                if (result != null) {
                    return !result;
                }
            }
            return call(remote::isFile, new ISourceLocationRequest(loc)).getValue();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isReadable(ISourceLocation loc) throws IOException {
        return call(remote::isReadable, new ISourceLocationRequest(loc)).getValue();
    }

    /**
     * Rascal's current implementions sometimes ask for a directory listing and then iterate over all entries
     * checking whether they are a directory. This is very slow for jsonrcp, so we store the last directory listing
     * and check the cache first
     */
    private final Cache<ISourceLocation, Lazy<Map<String, Boolean>>> cachedDirectoryListing
        = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(5))
            .maximumSize(1000)
            .build();

    @Override
    public String[] list(ISourceLocation loc) throws IOException {
        var result = call(remote::list, new ISourceLocationRequest(loc));
        cachedDirectoryListing.put(loc, Lazy.defer(() -> {
            return Stream.of(result).collect(Collectors.toMap(FileWithType::getName, e -> e.getType() == FileType.Directory));
        }));
        return Stream.of(result).map(FileWithType::getName).toArray(String[]::new);
    }

    @Override
    public boolean supportsHost() {
        return false;
    }

    @Override
    public FileAttributes stat(ISourceLocation loc) throws IOException {
        return call(remote::stat, new ISourceLocationRequest(loc));
    }

    @Override
    public OutputStream getOutputStream(ISourceLocation loc, boolean append) throws IOException {
        return new ByteArrayOutputStream() {
            private boolean closed = false;

            @Override
            public void close() throws IOException {
                if (closed) {
                    return;
                }
                closed = true;
                var content = new StringBuilder();
                try (var input = new ByteArrayInputStream(this.toByteArray())) {
                    StreamingBase64.encode(input, content, true);
                }
                call(remote::writeFile, new WriteFileRequest(loc, content.toString(), append));
                cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
            }
        };
    }

    @Override
    public void mkDirectory(ISourceLocation loc) throws IOException {
        call(remote::mkDirectory, new ISourceLocationRequest(loc));
        cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
    }

    @Override
    public void remove(ISourceLocation loc) throws IOException {
        call(remote::remove, new RemoveRequest(loc, true));
        cachedDirectoryListing.invalidate(loc);
        cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
    }

    @Override
    public void setLastModified(ISourceLocation loc, long timestamp) throws IOException {
        call(remote::setLastModified, new SetLastModifiedRequest(loc, timestamp));
    }

    @Override
    public boolean isWritable(ISourceLocation loc) throws IOException {
        return call(remote::isWritable, new ISourceLocationRequest(loc)).getValue();
    }

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        return call(remote::resolveLocation, new ISourceLocationRequest(input)).getLocation();
    }

    @Override
    public void watch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException {
        try {
            synchronized (watchers) {
                var key = new WatchSubscriptionKey(root, recursive);
                if (!watchers.containsKey(key)) {
                    System.err.println("Fresh watch, setting up request to server");
                    var freshWatchers = new Watchers();
                    freshWatchers.addNewWatcher(watcher);
                    watchersById.put(freshWatchers.getId(), freshWatchers);
                    remote.watch(new WatchRequest(root, recursive, freshWatchers.getId())).get(1, TimeUnit.MINUTES);
                    watchers.put(key, freshWatchers);
                }
                watchers.get(key).addNewWatcher(watcher);
            }
        } catch (CompletionException | InterruptedException | ExecutionException | TimeoutException ce) {
            throw new IOException("Could not watch `" + root + "` remotely: " + ce.getCause().getMessage());
        }
    }

    @Override
    public void unwatch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException {
        var watchKey = new WatchSubscriptionKey(root, recursive);
        synchronized (watchers) {
            var watch = watchers.get(watchKey);
            if (watch != null && watch.removeWatcher(watcher)) {
                System.err.println("No other watchers registered, so unregistering at server");
                watchers.remove(watchKey);
                if (!watch.getCallbacks().isEmpty()) {
                    System.err.println("Raced by another thread, canceling unregister");
                    watchers.put(watchKey, watch);
                    return;
                }
                watchersById.remove(watch.getId());
                call(remote::unwatch, new WatchRequest(root, recursive, watch.getId()));
            }
        }
    }

    @Override
    public boolean supportsRecursiveWatch() {
        try {
            return call(n -> remote.supportsRecursiveWatch(), null).getValue();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void sourceLocationChanged(org.rascalmpl.uri.remote.jsonrpc.ISourceLocationChanged changed) {
        var watcher = watchersById.get(changed.getWatchId());
        var root = changed.getRoot();
        if (watcher == null) {
            throw new ResponseErrorException(new ResponseError(ResponseErrorCode.RequestFailed, "Received notification for unregistered watch", root)); 
        }
        try {
            switch (ISourceLocationChangeType.fromValue(changed.getChangeType().getValue())) {
                case CREATED:
                    watcher.publish(ISourceLocationWatcher.created(root));
                    break;
                case DELETED:
                    watcher.publish(ISourceLocationWatcher.deleted(root));
                    break;
                case MODIFIED:
                    watcher.publish(ISourceLocationWatcher.modified(root));
                    break;
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseErrorException(new ResponseError(ResponseErrorCode.InvalidParams, "Unexpected FileChangeType " + changed.getChangeType().getValue(), root)); 
        }
    }

    private static final ExecutorService exec = NamedThreadPool.cachedDaemon("RemoteExternalResolverRegistry-watcher");

    /**
    * The watch API in Rascal uses closures identity to keep track of watches. Since we cannot share the instance
    * via the JSON-RPC bridge, we keep the closure around in this collection class.
    * If there are no more callbacks registered, we unregister the watch at the remote side.
    */
    public static class Watchers {
        private final String id;
        private final List<Consumer<ISourceLocationWatcher.ISourceLocationChanged>> callbacks = new CopyOnWriteArrayList<>();

        public Watchers() {
            this.id = UUID.randomUUID().toString();
        }

        public void addNewWatcher(Consumer<ISourceLocationWatcher.ISourceLocationChanged> watcher) {
            this.callbacks.add(watcher);
        }

        public boolean removeWatcher(Consumer<ISourceLocationWatcher.ISourceLocationChanged> watcher) {
            this.callbacks.remove(watcher);
            return this.callbacks.isEmpty();
        }

        public void publish(ISourceLocationWatcher.ISourceLocationChanged changed) {
            for (Consumer<ISourceLocationWatcher.ISourceLocationChanged> c : callbacks) {
                //schedule callbacks on different thread
                exec.submit(() -> c.accept(changed));
            }
        }

        public String getId() {
            return id;
        }

        public List<Consumer<ISourceLocationWatcher.ISourceLocationChanged>> getCallbacks() {
            return callbacks;
        }
    }

    public static class WatchSubscriptionKey {
        private final ISourceLocation loc;
        private final boolean recursive;
        public WatchSubscriptionKey(ISourceLocation loc, boolean recursive) {
            this.loc = loc;
            this.recursive = recursive;
        }

        @Override
        public int hashCode() {
            return Objects.hash(loc, recursive);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj instanceof WatchSubscriptionKey)) {
                WatchSubscriptionKey other = (WatchSubscriptionKey) obj;
                return recursive == other.recursive
                    && Objects.equals(loc, other.loc)
                    ;
            }
            return false;
        }
    }
}
