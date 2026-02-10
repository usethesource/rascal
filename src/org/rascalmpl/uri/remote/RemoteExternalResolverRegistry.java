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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.rascalmpl.ideservices.GsonUtils;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.IExternalResolverRegistry;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistry;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistry.WatchRequest;
import org.rascalmpl.util.NamedThreadPool;

import engineering.swat.watch.DaemonThreadPool;
import io.usethesource.vallang.ISourceLocation;

public class RemoteExternalResolverRegistry implements IExternalResolverRegistry {
    private final IRemoteResolverRegistry remote;

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
                .setExecutorService(DaemonThreadPool.buildConstrainedCached("rascal-remote-resolver-registry", Math.max(2, Math.min(6, Runtime.getRuntime().availableProcessors() - 2))))
                .create();

                clientLauncher.startListening();
                return clientLauncher.getRemoteProxy();
        } catch (Throwable e) {
            System.err.println("Error setting up remote resolver registry connection: " + e.getMessage());
            return null;
        }
    }

    @Override
    public InputStream getInputStream(ISourceLocation loc) throws IOException {
        try {
            var contents = remote.readFile(loc).get(1, TimeUnit.MINUTES);
            return new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_16));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote for `getInputStream` on " + loc + ": " + e.getMessage());
        }
    }

    @Override
    public boolean exists(ISourceLocation loc) {
        try {
            return remote.exists(loc).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return false;
        }
    }

    @Override
    public long lastModified(ISourceLocation loc) throws IOException {
        try {
            return remote.lastModified(loc).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote `lastModified` on " + loc + ": " + e.getMessage());
        }
    }

    @Override
    public long size(ISourceLocation loc) throws IOException {
        try {
            return remote.size(loc).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote `size` on " + loc + ": " + e.getMessage());
        }
    }

    @Override
    public boolean isDirectory(ISourceLocation loc) {
        try {
            return remote.isDirectory(loc).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isFile(ISourceLocation loc) {
        try {
            return remote.isFile(loc).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isReadable(ISourceLocation loc) throws IOException {
        try {
            return remote.isReadable(loc).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote `isReadable` on " + loc + ": " + e.getMessage());
        }
    }

    @Override
    public String[] list(ISourceLocation loc) throws IOException {
        try {
            return Stream.of(remote.list(loc).get(1, TimeUnit.MINUTES)).map(fwt -> fwt.getName()).toArray(String[]::new);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote `list` on " + loc + ": " + e.getMessage());
        }
    }

    @Override
    public boolean supportsHost() {
        return false;
    }

    @Override
    public FileAttributes stat(ISourceLocation loc) throws IOException {
        try {
            return remote.stat(loc).get(1, TimeUnit.MINUTES).getFileAttributes();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote `stat` on " + loc + ": " + e.getMessage());
        }
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
                var contents = this.toString(StandardCharsets.UTF_16);
                remote.writeFile(loc, contents, append, true, true);
            }
        };
    }

    @Override
    public void mkDirectory(ISourceLocation loc) throws IOException {
        try {
            remote.mkDirectory(loc).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote `mkDirectory` on " + loc + ": " + e.getMessage());
        }
    }

    @Override
    public void remove(ISourceLocation loc) throws IOException {
        try {
            remote.remove(loc, false).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote `remove` on " + loc + ": " + e.getMessage());
        }
    }

    @Override
    public void setLastModified(ISourceLocation loc, long timestamp) throws IOException {
        throw new IOException("setLastModified is not supported remotely");
    }

    @Override
    public boolean isWritable(ISourceLocation loc) throws IOException {
        try {
            return remote.isWritable(loc).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote `isWritable` on " + loc + ": " + e.getMessage());
        }
    }

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        try {
            return remote.resolveLocation(input).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Error during remote `resolve` on " + input + ": " + e.getMessage());
        }
    }

    @Override
    public void watch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException {
        try {
            remote.watch(new WatchRequest(root, recursive, "")).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Could not watch `" + root + "` remotely: " + e.getMessage());
        }
    }

    @Override
    public void unwatch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException {
        try {
            //TODO: arguments are currently not correct
            remote.unwatch(new WatchRequest(root.toString(), true, "")).get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Could not unwatch `" + root + "` remotely: " + e.getMessage());
        }
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
