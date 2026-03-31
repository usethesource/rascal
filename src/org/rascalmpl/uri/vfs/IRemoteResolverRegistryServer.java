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
package org.rascalmpl.uri.vfs;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.remote.jsonrpc.BooleanResponse;
import org.rascalmpl.uri.remote.jsonrpc.ISourceLocationRequest;
import org.rascalmpl.uri.remote.jsonrpc.LocationContentResponse;
import org.rascalmpl.uri.remote.jsonrpc.NumberResponse;
import org.rascalmpl.uri.remote.jsonrpc.RemoveRequest;
import org.rascalmpl.uri.remote.jsonrpc.RenameRequest;
import org.rascalmpl.uri.remote.jsonrpc.SetLastModifiedRequest;
import org.rascalmpl.uri.remote.jsonrpc.SourceLocationResponse;
import org.rascalmpl.uri.remote.jsonrpc.TimestampResponse;
import org.rascalmpl.uri.remote.jsonrpc.WatchRequest;
import org.rascalmpl.uri.remote.jsonrpc.WriteFileRequest;

public interface IRemoteResolverRegistryServer {
    @JsonRequest("rascal/vfs/input/readFile")
    default CompletableFuture<LocationContentResponse> readFile(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/exists")
    default CompletableFuture<BooleanResponse> exists(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/lastModified")
    default CompletableFuture<TimestampResponse> lastModified(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/created")
    default CompletableFuture<TimestampResponse> created(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isDirectory")
    default CompletableFuture<BooleanResponse> isDirectory(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isFile")
    default CompletableFuture<BooleanResponse> isFile(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/list")
    default CompletableFuture<FileWithType[]> list(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/size")
    default CompletableFuture<NumberResponse> size(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/stat")
    default CompletableFuture<FileAttributes> stat(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isReadable")
    default CompletableFuture<BooleanResponse> isReadable(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/setLastModified")
    default CompletableFuture<Void> setLastModified(SetLastModifiedRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isWritable")
    default CompletableFuture<BooleanResponse> isWritable(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/writeFile")
    default CompletableFuture<Void> writeFile(WriteFileRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/mkDirectory")
    default CompletableFuture<Void> mkDirectory(ISourceLocationRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/remove")
    default CompletableFuture<Void> remove(RemoveRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/rename")
    default CompletableFuture<Void> rename(RenameRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/watcher/watch")
    default CompletableFuture<Void> watch(WatchRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/watcher/unwatch")
    default CompletableFuture<Void> unwatch(WatchRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/watcher/supportsRecursiveWatch")
    default CompletableFuture<BooleanResponse> supportsRecursiveWatch() {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/logical/resolveLocation")
    default CompletableFuture<SourceLocationResponse> resolveLocation(ISourceLocationRequest loc) {
        throw new UnsupportedOperationException();
    }

    public static class FileWithType {
        @NonNull private final String name;
        @NonNull private final FileType type;

        public FileWithType(@NonNull String name, @NonNull FileType type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public FileType getType() {
            return type;
        }
    }

    public enum FileType {
        Unknown(0), File(1), Directory(2), SymbolicLink(64);

        private final int value;

        private FileType(int val) {
            assert val == 0 || val == 1 || val == 2 || val == 64;
            this.value = val;
        }

        public int getValue() {
            return value;
        }
    }
}
