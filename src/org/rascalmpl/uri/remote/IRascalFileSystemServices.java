/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
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
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.NotDirectoryException;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import org.apache.commons.codec.binary.Base64InputStream;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChangeType;
import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChanged;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryClient;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryServer;
import org.rascalmpl.util.NamedThreadPool;

import io.usethesource.vallang.ISourceLocation;

public class IRascalFileSystemServices implements IRemoteResolverRegistryServer {
    static final URIResolverRegistry reg = URIResolverRegistry.getInstance();
    static final ExecutorService executor = NamedThreadPool.cachedDaemon("rascal-vfs");

    private volatile @MonotonicNonNull IRemoteResolverRegistryClient client = null;

    @EnsuresNonNull("this.client")
    protected void provideClient(IRemoteResolverRegistryClient client) {
        this.client = client;
    }

    @Override
    public CompletableFuture<ISourceLocation> resolveLocation(ISourceLocation loc) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ISourceLocation resolved = reg.logicalToPhysical(loc);

                if (resolved == null) {
                    return loc;
                }

                return resolved;
            } catch (Exception e) {
                return loc;
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> watch(WatchRequest params) {
        return CompletableFuture.runAsync(() -> {
            try {
                ISourceLocation loc = params.getLocation();

                URIResolverRegistry.getInstance().watch(loc, params.isRecursive(), changed -> {
                    client.sourceLocationChanged(changed.getLocation(), changed.getChangeType(), "");
                });
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    static FileChangeEvent convertChangeEvent(ISourceLocationChanged changed) throws IOException {
        return new FileChangeEvent(changed.getChangeType(), changed.getLocation().getURI().toASCIIString());
    }

    @Override
    public CompletableFuture<FileAttributes> stat(ISourceLocation loc) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return reg.stat(loc);
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<FileWithType[]> list(ISourceLocation loc) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!reg.isDirectory(loc)) {
                    throw new NotDirectoryException(loc.toString());
                }
                return Arrays.stream(reg.list(loc)).map(l -> new FileWithType(URIUtil.getLocationName(l),
                        reg.isDirectory(l) ? FileType.Directory : FileType.File)).toArray(FileWithType[]::new);
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> mkDirectory(ISourceLocation loc) {
        return CompletableFuture.runAsync(() -> {
            try {
                reg.mkDirectory(loc);
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<String> readFile(ISourceLocation loc) {
        return CompletableFuture.supplyAsync(() -> {
            try (InputStream source = new Base64InputStream(reg.getInputStream(loc), true)) {
                return new String(source.readAllBytes(), StandardCharsets.US_ASCII);
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> writeFile(ISourceLocation loc, String content, boolean append) {
        return CompletableFuture.runAsync(() -> {
            try {
                try (OutputStream target = reg.getOutputStream(loc, false)) {
                    target.write(Base64.getDecoder().decode(content));
                }
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> remove(ISourceLocation loc, boolean recursive) {
        return CompletableFuture.runAsync(() -> {
            try {
                reg.remove(loc, recursive);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> rename(ISourceLocation from, ISourceLocation to, boolean overwrite) {
        return CompletableFuture.runAsync(() -> {
            try {
                reg.rename(from, to, overwrite);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    public static class FileChangeEvent {
        @NonNull private final ISourceLocationChangeType type;
        @NonNull private final String uri;

        public FileChangeEvent(ISourceLocationChangeType type, @NonNull String uri) {
            this.type = type;
            this.uri = uri;
        }

        public ISourceLocationChangeType getType() {
            return type;
        }

        public ISourceLocation getLocation() throws URISyntaxException {
            return null;
        }
    }
}
