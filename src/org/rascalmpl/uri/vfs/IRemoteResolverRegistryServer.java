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

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;

public interface IRemoteResolverRegistryServer {
    @JsonRequest("rascal/vfs/input/readFile")
    default CompletableFuture<String> readFile(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/exists")
    default CompletableFuture<Boolean> exists(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/lastModified")
    default CompletableFuture<Long> lastModified(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/created")
    default CompletableFuture<Long> created(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isDirectory")
    default CompletableFuture<Boolean> isDirectory(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isFile")
    default CompletableFuture<Boolean> isFile(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/list")
    default CompletableFuture<FileWithType[]> list(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/size")
    default CompletableFuture<Long> size(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/stat")
    default CompletableFuture<FileAttributes> stat(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isReadable")
    default CompletableFuture<Boolean> isReadable(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/setLastModified")
    default CompletableFuture<Void> setLastModified(ISourceLocation loc, long timestamp) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isWritable")
    default CompletableFuture<Boolean> isWritable(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/writeFile")
    default CompletableFuture<Void> writeFile(ISourceLocation loc, String content, boolean append) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/mkDirectory")
    default CompletableFuture<Void> mkDirectory(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/remove")
    default CompletableFuture<Void> remove(ISourceLocation loc, boolean recursive) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/rename")
    default CompletableFuture<Void> rename(ISourceLocation from, ISourceLocation to, boolean overwrite) {
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
    default CompletableFuture<Boolean> supportsRecursiveWatch() {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/logical/resolveLocation")
    default CompletableFuture<ISourceLocation> resolveLocation(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    public static class WatchRequest {
        @NonNull private ISourceLocation loc;
        @NonNull private String watcher;

        private boolean recursive;

        private final String[] excludes;

        public WatchRequest(ISourceLocation loc, boolean recursive, String watcher) {
            this.loc = loc;
            this.recursive = recursive;
            this.watcher = watcher;
            this.excludes = new String[0];
        }

        public WatchRequest(@NonNull String uri, boolean recursive, @NonNull String watcher) {
            this.loc = ValueFactoryFactory.getValueFactory().sourceLocation(uri);
            this.recursive = recursive;
            this.watcher = watcher;
            this.excludes = new String[0];
        }

        public WatchRequest(String uri, boolean recursive, String[] excludes) {
            this.loc = ValueFactoryFactory.getValueFactory().sourceLocation(uri);
            this.recursive = recursive;
            this.watcher = "";
            this.excludes = excludes;
        }

        public ISourceLocation getLocation() {
            return loc;
        }

        public String getWatcher() {
            return watcher;
        }

        public boolean isRecursive() {
            return recursive;
        }

        public String[] getExcludes() {
            return excludes;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof WatchRequest) {
                var other = (WatchRequest)obj;
                return super.equals(other)
                    && other.recursive == recursive
                    && Objects.equals(watcher, other.watcher)
                    && Arrays.equals(excludes, other.excludes);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), watcher, recursive, excludes);
        }
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
