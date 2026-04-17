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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.ResponseErrorException;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode;
import org.rascalmpl.ideservices.GsonUtils;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.IExternalResolverRegistry;
import org.rascalmpl.uri.ISourceLocationWatcher;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.remote.jsonrpc.DirectoryEntry;
import org.rascalmpl.uri.remote.jsonrpc.ISourceLocationRequest;
import org.rascalmpl.uri.remote.jsonrpc.RemoteIOError;
import org.rascalmpl.uri.remote.jsonrpc.RemoveRequest;
import org.rascalmpl.uri.remote.jsonrpc.SetLastModifiedRequest;
import org.rascalmpl.uri.remote.jsonrpc.WatchRequest;
import org.rascalmpl.uri.remote.jsonrpc.WriteFileRequest;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryClient;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryServer;
import org.rascalmpl.util.Lazy;
import org.rascalmpl.util.NamedThreadPool;
import org.rascalmpl.util.base64.StreamingBase64;
import org.rascalmpl.util.functional.ThrowingConsumer;
import org.rascalmpl.util.functional.ThrowingFunction;
import org.rascalmpl.util.functional.ThrowingRunnable;
import org.rascalmpl.util.functional.ThrowingSupplier;
import org.rascalmpl.util.functional.ThrowingTriConsumer;
import org.rascalmpl.util.functional.ThrowingTriFunction;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.usethesource.vallang.ISourceLocation;

/**
 * Default implementation for access to a remote file system.
 */
public class RemoteExternalResolverRegistry implements IExternalResolverRegistry, IRemoteResolverRegistryClient {
    private final AtomicReference<IRemoteResolverRegistryServer> remote = new AtomicReference<>(null);
    private static final ExecutorService exec = NamedThreadPool.cachedDaemon("rascal-remote-resolver-registry");

    private final Map<WatchSubscriptionKey, Watchers> watchers = new ConcurrentHashMap<>();
    private final Map<String, Watchers> watchersById = new ConcurrentHashMap<>();

    private final int remoteResolverRegistryPort;

    /**
     * We keep track of whether a first connection to a remote resolver registry has been made.
     * During initialization, the thread is blocked, see {@link #getRemote}.
     */
    private volatile boolean firstConnectionEstablished = false;

    public RemoteExternalResolverRegistry(int remoteResolverRegistryPort) {
        this.remoteResolverRegistryPort = remoteResolverRegistryPort;
        scheduleReconnect(null);
    }

    private static final Duration LONGEST_TIMEOUT = Duration.ofMinutes(1);

    private void connect(Duration nextTimeout) {
        if (remote.get() != null) {
            return;
        }
        try {
            var newClient = startClient();
            if (!remote.compareAndSet(null, newClient.getRight())) {
                newClient.getLeft().close();
            }
            firstConnectionEstablished = true;
        } catch (RuntimeException | IOException e) {
            CompletableFuture.delayedExecutor(nextTimeout.toMillis(), TimeUnit.MILLISECONDS, exec).execute(() -> {
                var newTimeout = nextTimeout.plusMillis(10);
                if (newTimeout.compareTo(LONGEST_TIMEOUT) >= 0) {
                    newTimeout = LONGEST_TIMEOUT;
                }
                connect(newTimeout);
            });
        }
    }

    private void scheduleReconnect(IRemoteResolverRegistryServer oldServer) {
        if (remote.compareAndSet(oldServer, null)) {
            CompletableFuture.runAsync(() -> connect(Duration.ofMillis(10)), exec);
        }
    }

    private IRemoteResolverRegistryServer getRemote() throws IOException {
        for (var i = 0; i < 1000 && !firstConnectionEstablished; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        var result = remote.get();
        if (result == null) {
            throw new IOException("Not connected to remote external resolver registry");
        }
        return result;
    }

    private class ErrorDetectingInputStream extends InputStream {
        private final InputStream original;

        public ErrorDetectingInputStream(InputStream original) {
            this.original = original;
        }

        private IRemoteResolverRegistryServer server;

        public void connect(IRemoteResolverRegistryServer server) {
            this.server = server;
        }

        private <T> T socketExceptionCatcher(ThrowingSupplier<T, IOException> function) throws IOException {
            try {
                return function.get();
            } catch (SocketException e) {
                scheduleReconnect(server);
                throw e;
            }
        }
        
        private <T, R> R socketExceptionCatcher(ThrowingFunction<T, R, IOException> function, T t) throws IOException {
            try {
                return function.apply(t);
            } catch (SocketException e) {
                scheduleReconnect(server);
                throw e;
            }
        }

        private <T, U, V, R> R socketExceptionCatcher(ThrowingTriFunction<T, U, V, R, IOException> function, T t, U u, V v) throws IOException {
            try {
                return function.apply(t, u, v);
            } catch (SocketException e) {
                scheduleReconnect(server);
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
            return socketExceptionCatcher(original::available);
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
    }

    private class ErrorDetectingOutputStream extends OutputStream {
        private final OutputStream original;

        public ErrorDetectingOutputStream(OutputStream original) {
            this.original = original;
        }

        private IRemoteResolverRegistryServer server;

        public void connect(IRemoteResolverRegistryServer server) {
            this.server = server;
        }
        
        private void socketExceptionCatcher(ThrowingRunnable<IOException> runnable) throws IOException {
            try {
                runnable.run();
            } catch (SocketException e) {
                scheduleReconnect(server);
                throw e;
            }
        }

        private <T> void socketExceptionCatcher(ThrowingConsumer<T, IOException> consumer, T t) throws IOException {
            try {
                consumer.accept(t);
            } catch (SocketException e) {
                scheduleReconnect(server);
                throw e;
            }
        }

        private <T, U, V> void socketExceptionCatcher(ThrowingTriConsumer<T, U, V, IOException> consumer, T t, U u, V v) throws IOException {
            try {
                consumer.accept(t, u, v);
            } catch (SocketException e) {
                scheduleReconnect(server);
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
    }

    private Pair<Socket, IRemoteResolverRegistryServer> startClient() throws RuntimeException, IOException {
        @SuppressWarnings("resource")
        var socket = new Socket(InetAddress.getLoopbackAddress(), remoteResolverRegistryPort);
        socket.setTcpNoDelay(true);
        var inputStream = new ErrorDetectingInputStream(socket.getInputStream());
        var outputStream = new ErrorDetectingOutputStream(socket.getOutputStream());
        
        Launcher<IRemoteResolverRegistryServer> clientLauncher = new Launcher.Builder<IRemoteResolverRegistryServer>()
            .setRemoteInterface(IRemoteResolverRegistryServer.class)
            .setLocalService(this)
            .setInput(inputStream)
            .setOutput(outputStream)
            .configureGson(GsonUtils.complexAsJsonObject())
            .setExecutorService(exec)
            .create();

        clientLauncher.startListening();
        var remote = clientLauncher.getRemoteProxy();

        inputStream.connect(remote);
        outputStream.connect(remote);
        return Pair.of(socket, remote);
    }

    private static <T, U> U call(ThrowingFunction<T, CompletableFuture<U>, IOException> function, T argument) throws IOException {
        try {
            return function.apply(argument).get(1, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            throw new IOException("Remote resolver took too long to reply; interrupted to avoid deadlocks");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new UnsupportedOperationException("Thread should have been interrupted");
        } catch (CompletionException | ExecutionException e) {
            var cause = e.getCause();
            if (cause instanceof ResponseErrorException) {
                throw RemoteIOError.translate((ResponseErrorException) cause);
            }
            throw new IOException(e);
        }
    }

    @Override
    public InputStream getInputStream(ISourceLocation loc) throws IOException {
        return StreamingBase64.decode(call(getRemote()::readFile, new ISourceLocationRequest(loc)).getContent());
    }

    @Override
    public boolean exists(ISourceLocation loc) {
        try {
            return call(getRemote()::exists, new ISourceLocationRequest(loc)).getValue();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public long lastModified(ISourceLocation loc) throws IOException {
        return call(getRemote()::lastModified, new ISourceLocationRequest(loc)).getTimestamp();
    }

    @Override
    public long size(ISourceLocation loc) throws IOException {
        return call(getRemote()::size, new ISourceLocationRequest(loc)).getNumber();
    }

    private Boolean cachedIsDirectory(ISourceLocation loc) {
        var cached = cachedDirectoryListing.getIfPresent(URIUtil.getParentLocation(loc));
        if (cached == null) {
            return null;
        }
        var result = cached.get().get(URIUtil.getLocationName(loc));
        return result != null && result;
    }

    @Override
    public boolean isDirectory(ISourceLocation loc) {
        try {
            var cached = cachedIsDirectory(loc);
            if (cached != null) {
                return cached;
            }
            return call(getRemote()::isDirectory, new ISourceLocationRequest(loc)).getValue();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isFile(ISourceLocation loc) {
        try {
            var cached = cachedIsDirectory(loc);
            if (cached != null) {
                return !cached;
            }
            return call(getRemote()::isFile, new ISourceLocationRequest(loc)).getValue();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isReadable(ISourceLocation loc) throws IOException {
        return call(getRemote()::isReadable, new ISourceLocationRequest(loc)).getValue();
    }

    /**
     * Rascal's current implementions sometimes ask for a directory listing and then iterate over all entries
     * checking whether they are a directory. This is very slow for JSON-RPC, so we store the last directory listing
     * and check the cache first
     */
    private final Cache<ISourceLocation, Lazy<Map<String, Boolean>>> cachedDirectoryListing
        = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(5))
            .maximumSize(1000)
            .build();

    @Override
    public String[] list(ISourceLocation loc) throws IOException {
        var result = call(getRemote()::list, new ISourceLocationRequest(loc));
        cachedDirectoryListing.put(loc, Lazy.defer(() -> {
            return Stream.of(result.getEntries()).collect(Collectors.toMap(DirectoryEntry::getName, DirectoryEntry::isDirectory));
        }));
        return Stream.of(result.getEntries()).map(DirectoryEntry::getName).toArray(String[]::new);
    }

    @Override
    public boolean supportsHost() {
        return false;
    }

    @Override
    public FileAttributes stat(ISourceLocation loc) throws IOException {
        return call(getRemote()::stat, new ISourceLocationRequest(loc));
    }

    @Override
    public OutputStream getOutputStream(ISourceLocation loc, boolean append) throws IOException {
        var content = new StringBuilder();
        return StreamingBase64.encode(content, () -> {
            cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
            call(getRemote()::writeFile, new WriteFileRequest(loc, content.toString(), append));
            cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
        });
    }

    @Override
    public void mkDirectory(ISourceLocation loc) throws IOException {
        cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
        call(getRemote()::mkDirectory, new ISourceLocationRequest(loc));
        cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
    }

    @Override
    public void remove(ISourceLocation loc) throws IOException {
        cachedDirectoryListing.invalidate(loc);
        cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
        call(getRemote()::remove, new RemoveRequest(loc, true));
        cachedDirectoryListing.invalidate(loc);
        cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
    }

    @Override
    public void setLastModified(ISourceLocation loc, long timestamp) throws IOException {
        call(getRemote()::setLastModified, new SetLastModifiedRequest(loc, timestamp));
    }

    @Override
    public boolean isWritable(ISourceLocation loc) throws IOException {
        return call(getRemote()::isWritable, new ISourceLocationRequest(loc)).getValue();
    }

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        return call(getRemote()::resolveLocation, new ISourceLocationRequest(input)).getLocation();
    }

    @Override
    public void watch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException {
        synchronized (watchers) {
            var key = new WatchSubscriptionKey(root, recursive);
            var watch = watchers.get(key);
            if (watch == null) {
                watch = new Watchers();
                watch.addNewWatcher(watcher);
                watchersById.put(watch.getId(), watch);
                call(getRemote()::watch, new WatchRequest(root, recursive, watch.getId()));
                watchers.put(key, watch);
            }
            watch.addNewWatcher(watcher);
        }
    }

    @Override
    public void unwatch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException {
        synchronized (watchers) {
            var watchKey = new WatchSubscriptionKey(root, recursive);
            var watch = watchers.get(watchKey);
            if (watch != null && watch.removeWatcher(watcher)) {
                watchers.remove(watchKey);
                if (!watch.getCallbacks().isEmpty()) {
                    watchers.put(watchKey, watch);
                    return;
                }
                watchersById.remove(watch.getId());
                call(getRemote()::unwatch, new WatchRequest(root, recursive, watch.getId()));
            }
        }
    }

    @Override
    public boolean supportsRecursiveWatch() {
        try {
            return call(n -> getRemote().supportsRecursiveWatch(), null).getValue();
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
            switch (changed.getChangeType()) {
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

    /**
    * The watch API in Rascal uses closures identity to keep track of watches. Since we cannot share the instance
    * via the JSON-RPC bridge, we keep the closure around in this collection class.
    * If there are no more callbacks registered, we unregister the watch at the remote side.
    */
    private static class Watchers {
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

    private static class WatchSubscriptionKey {
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
        public boolean equals(Object obj) {
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
