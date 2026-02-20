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
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.time.Duration;
import java.util.Base64;
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
import org.rascalmpl.ideservices.GsonUtils;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.IExternalResolverRegistry;
import org.rascalmpl.uri.ISourceLocationWatcher;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.vfs.FileAttributesResult.FileType;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistry;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistry.FileWithType;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistry.WatchRequest;
import org.rascalmpl.util.Lazy;
import org.rascalmpl.util.NamedThreadPool;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonPrimitive;

import io.usethesource.vallang.ISourceLocation;

public class RemoteExternalResolverRegistry implements IExternalResolverRegistry {
    private final IRemoteResolverRegistry remote;

    private final Map<WatchSubscriptionKey, Watchers> watchers = new ConcurrentHashMap<>();
    private final Map<String, Watchers> watchersById = new ConcurrentHashMap<>();

    public RemoteExternalResolverRegistry(int remoteResolverRegistryPort) {
        this.remote = startClient(remoteResolverRegistryPort);
    }

    private IRemoteResolverRegistry startClient(int remoteResolverRegistryPort) {
        try {
            @SuppressWarnings("resource")
            var socket = new Socket(InetAddress.getLoopbackAddress(), remoteResolverRegistryPort);
            socket.setTcpNoDelay(true);
            Launcher<IRemoteResolverRegistry> clientLauncher = new Launcher.Builder<IRemoteResolverRegistry>()
                .setRemoteInterface(IRemoteResolverRegistry.class)
                .setLocalService(this)
                .setInput(socket.getInputStream())
                .setOutput(socket.getOutputStream())
                .configureGson(GsonUtils.complexAsJsonObject())
                .setExecutorService(NamedThreadPool.cachedDaemon("rascal-remote-resolver-registry"))
                .create();

                clientLauncher.startListening();
                return clientLauncher.getRemoteProxy();
        } catch (Throwable e) {
            System.err.println("Error setting up remote resolver registry connection: " + e.getMessage());
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
                throw new IOException(cause);
            }
            throw new IOException(e);
        }
    }

    private static IOException translateException(ResponseErrorException cause) {
        var error = cause.getResponseError();
        switch (error.getCode()) {
            case -1:
                return new IOException("Generic error: " + error.getMessage());
            case -2: {
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
            case -3:
                return new IOException("Rascal native schemes should not be forwarded");
            default:
                return new IOException("Missing case for: " + error);
        }
    }

    @Override
    public InputStream getInputStream(ISourceLocation loc) throws IOException {   
        return new ByteArrayInputStream(call(remote::readFile, loc).getBytes(StandardCharsets.UTF_16));
    }

    @Override
    public boolean exists(ISourceLocation loc) {
        try {
            return call(remote::exists, loc);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public long lastModified(ISourceLocation loc) throws IOException {
        return call(remote::lastModified, loc);
    }

    @Override
    public long size(ISourceLocation loc) throws IOException {
        return call(remote::size, loc);
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
            return call(remote::isDirectory, loc);
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
            return call(remote::isFile, loc);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isReadable(ISourceLocation loc) throws IOException {
        return call(remote::isReadable, loc);
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
        var result = call(remote::list, loc);
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
        return call(remote::stat, loc).getFileAttributes();
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
                var contents = Base64.getEncoder().encodeToString(this.toByteArray());
                call(l -> remote.writeFile(l, contents, append, true, true), loc);
                cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
            }
        };
    }

    @Override
    public void mkDirectory(ISourceLocation loc) throws IOException {
        call(remote::mkDirectory, loc);
        cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
    }

    @Override
    public void remove(ISourceLocation loc) throws IOException {
        call(l -> remote.remove(l, true), loc);
        cachedDirectoryListing.invalidate(loc);
        cachedDirectoryListing.invalidate(URIUtil.getParentLocation(loc));
    }

    @Override
    public void setLastModified(ISourceLocation loc, long timestamp) throws IOException {
        throw new IOException("Remote `setLastModified` is not supported");
    }

    @Override
    public boolean isWritable(ISourceLocation loc) throws IOException {
        return call(remote::isWritable, loc);
    }

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        return call(remote::resolveLocation, input);
    }

    @Override
    public void watch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException {
        try {
            var watch = watchers.computeIfAbsent(new WatchSubscriptionKey(root, recursive), k -> {
                System.err.println("Fresh watch, setting up request to server");
                var result = new Watchers();
                result.addNewWatcher(watcher);
                watchersById.put(result.getId(), result);
                remote.watch(new WatchRequest(root, recursive, result.getId())).join();
                return result;
            });
            watch.addNewWatcher(watcher);
        } catch (CompletionException ce) {
            throw new IOException("Could not watch `" + root + "` remotely: " + ce.getCause().getMessage());
        }
    }

    @Override
    public void unwatch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException {
        var watchKey = new WatchSubscriptionKey(root, recursive);
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
        }
        call(remote::unwatch, new WatchRequest(root, recursive, watch.getId()));
    }

    @Override
    public boolean supportsRecursiveWatch() {
        return true;
    }

    private static final ExecutorService exec = NamedThreadPool.cachedDaemon("RemoteExternalResolverRegistry-watcher");

    /**
    * The watch api in rascal uses closures identity to keep track of watches.
    * Since we cannot share the instance via the json-rpc bridge, we keep the
    * closure around in this collection class.
    * If there are no more callbacks registered, we unregister the watch at the
    * VSCode side.
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
