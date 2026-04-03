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
import java.nio.file.NotDirectoryException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.remote.jsonrpc.CopyRequest;
import org.rascalmpl.uri.remote.jsonrpc.ISourceLocationChangeType;
import org.rascalmpl.uri.remote.jsonrpc.ISourceLocationChanged;
import org.rascalmpl.uri.remote.jsonrpc.ISourceLocationRequest;
import org.rascalmpl.uri.remote.jsonrpc.LocationContentResponse;
import org.rascalmpl.uri.remote.jsonrpc.RemoveRequest;
import org.rascalmpl.uri.remote.jsonrpc.RenameRequest;
import org.rascalmpl.uri.remote.jsonrpc.SourceLocationResponse;
import org.rascalmpl.uri.remote.jsonrpc.WatchRequest;
import org.rascalmpl.uri.remote.jsonrpc.WriteFileRequest;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryClient;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistryServer;
import org.rascalmpl.util.NamedThreadPool;
import org.rascalmpl.util.base64.StreamingBase64;

import io.usethesource.vallang.ISourceLocation;

/**
 * RascalFileSystemServices offers remote access to the Rascal file system.
 * Currently, this is limited to a single client-server connection.
 */
public class RascalFileSystemServices implements IRemoteResolverRegistryServer {
    private static final URIResolverRegistry reg = URIResolverRegistry.getInstance();
    private static final ExecutorService executor = NamedThreadPool.cachedDaemon("rascal-vfs");

    private volatile @MonotonicNonNull IRemoteResolverRegistryClient client = null;

    @EnsuresNonNull("this.client")
    protected void provideClient(IRemoteResolverRegistryClient client) {
        this.client = client;
    }

    @FunctionalInterface
    private interface IOSupplier<T> {
        T supply() throws IOException;
    }

    @FunctionalInterface
    private interface IORunner {
        void run() throws IOException;
    }

    private <T> CompletableFuture<T> async(IOSupplier<T> job) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return job.supply();
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    private CompletableFuture<Void> async(IORunner job) {
        return CompletableFuture.runAsync(() -> {
            try {
                job.run();
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<SourceLocationResponse> resolveLocation(ISourceLocationRequest req) {
        return async(() -> {
            ISourceLocation loc = req.getLocation();
            ISourceLocation resolved = reg.logicalToPhysical(loc);

            if (resolved == null) {
                resolved = loc;
            }

            return new SourceLocationResponse(resolved);
        });
    }

    @Override
    public CompletableFuture<Void> watch(WatchRequest params) {
        return async(() -> {
            URIResolverRegistry.getInstance().watch(params.getLocation(), params.isRecursive(), changed -> 
                client.sourceLocationChanged(new ISourceLocationChanged(
                    changed.getLocation(), ISourceLocationChangeType.forValue(changed.getChangeType().getValue()), params.getWatchId()
                ))
            );
        });
    }

    @Override
    public CompletableFuture<FileAttributes> stat(ISourceLocationRequest req) {
        return async(() -> reg.stat(req.getLocation()));
    }

    @Override
    public CompletableFuture<FileWithType[]> list(ISourceLocationRequest req) {
        return async(() -> {
            ISourceLocation loc = req.getLocation();
            if (!reg.isDirectory(loc)) {
                throw new NotDirectoryException(loc.toString());
            }
            return Arrays.stream(reg.list(loc))
                    .map(l -> new FileWithType(URIUtil.getLocationName(l), reg.isDirectory(l) ? FileType.Directory : FileType.File))
                    .toArray(FileWithType[]::new);
        });
    }

    @Override
    public CompletableFuture<Void> mkDirectory(ISourceLocationRequest req) {
        return async(() -> reg.mkDirectory(req.getLocation()));
    }

    @Override
    public CompletableFuture<LocationContentResponse> readFile(ISourceLocationRequest req) {
        return async(() -> {
            StringBuilder builder = new StringBuilder();
            StreamingBase64.encode(reg.getInputStream(req.getLocation()), builder, true);
            return new LocationContentResponse(builder.toString());
        });
    }

    @Override
    public CompletableFuture<Void> writeFile(WriteFileRequest req) {
        return async(() -> {
            try (var decoder = StreamingBase64.decode(req.getContent());
                    var target = reg.getOutputStream(req.getLocation(), req.isAppend())) {
                decoder.transferTo(target);
            }
        });
    }

    @Override
    public CompletableFuture<Void> remove(RemoveRequest req) {
        return async(() -> reg.remove(req.getLocation(), req.isRecursive()));
    }

    @Override
    public CompletableFuture<Void> rename(RenameRequest req) {
        return async(() -> reg.rename(req.getFrom(), req.getTo(), req.isOverwrite()));
    }

    @Override
    public CompletableFuture<Void> copy(CopyRequest req) {
        return async(() -> reg.copy(req.getFrom(), req.getTo(), req.isRecursive(), req.isOverwrite()));
    }
}
