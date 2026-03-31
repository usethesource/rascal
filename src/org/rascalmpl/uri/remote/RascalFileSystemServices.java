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
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
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

import io.usethesource.vallang.ISourceLocation;

public class RascalFileSystemServices implements IRemoteResolverRegistryServer {
    static final URIResolverRegistry reg = URIResolverRegistry.getInstance();
    static final ExecutorService executor = NamedThreadPool.cachedDaemon("rascal-vfs");

    private volatile @MonotonicNonNull IRemoteResolverRegistryClient client = null;

    @EnsuresNonNull("this.client")
    protected void provideClient(IRemoteResolverRegistryClient client) {
        this.client = client;
    }

    @Override
    public CompletableFuture<SourceLocationResponse> resolveLocation(ISourceLocationRequest req) {
        return CompletableFuture.supplyAsync(() -> {
            ISourceLocation loc = req.getLocation();
            try {
                ISourceLocation resolved = reg.logicalToPhysical(loc);

                if (resolved == null) {
                    return new SourceLocationResponse(loc);
                }

                return new SourceLocationResponse(resolved);
            } catch (Exception e) {
                return new SourceLocationResponse(loc);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> watch(WatchRequest params) {
        return CompletableFuture.runAsync(() -> {
            try {
                ISourceLocation loc = params.getLocation();

                URIResolverRegistry.getInstance().watch(loc, params.isRecursive(), changed -> {
                    client.sourceLocationChanged(new ISourceLocationChanged(
                        changed.getLocation(), ISourceLocationChangeType.forValue(changed.getChangeType().getValue()), params.getWatcher()
                    ));
                });
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<FileAttributes> stat(ISourceLocationRequest req) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return reg.stat(req.getLocation());
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<FileWithType[]> list(ISourceLocationRequest req) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ISourceLocation loc = req.getLocation();
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
    public CompletableFuture<Void> mkDirectory(ISourceLocationRequest req) {
        return CompletableFuture.runAsync(() -> {
            try {
                reg.mkDirectory(req.getLocation());
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<LocationContentResponse> readFile(ISourceLocationRequest req) {
        return CompletableFuture.supplyAsync(() -> {
            try (InputStream source = new Base64InputStream(reg.getInputStream(req.getLocation()), true)) {
                return new LocationContentResponse(new String(source.readAllBytes(), StandardCharsets.US_ASCII));
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> writeFile(WriteFileRequest req) {
        return CompletableFuture.runAsync(() -> {
            try {
                try (OutputStream target = reg.getOutputStream(req.getLocation(), req.isAppend())) {
                    target.write(Base64.getDecoder().decode(req.getContent()));
                }
            } catch (IOException | RuntimeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> remove(RemoveRequest req) {
        return CompletableFuture.runAsync(() -> {
            try {
                reg.remove(req.getLocation(), req.isRecursive());
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> rename(RenameRequest req) {
        return CompletableFuture.runAsync(() -> {
            try {
                reg.rename(req.getFrom(), req.getTo(), req.isOverwrite());
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }
}
