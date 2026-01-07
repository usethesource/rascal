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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import org.rascalmpl.uri.vfs.FileAttributesResult.FileType;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public interface IRemoteResolverRegistry {
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
    //TODO (Rodin): return type not "the same" as in ISourceLocationInput
    default CompletableFuture<FileWithType[]> list(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/size")
    default CompletableFuture<Long> size(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/stat")
    //TODO (Rodin): merge/replace FileAttributesResult with FileAttributes
    default CompletableFuture<FileAttributesResult> stat(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isReadable")
    default CompletableFuture<Boolean> isReadable(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/input/isWritable")
    default CompletableFuture<Boolean> isWritable(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/writeFile")
    //TODO (Rodin): terug naar loc, content, append; create+overwrite in TS oplossen
    default CompletableFuture<Void> writeFile(ISourceLocation loc, String content, boolean append, boolean create, boolean overwrite) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/mkDirectory")
    default CompletableFuture<Void> mkDirectory(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/remove")
    //TODO (Rodin): ISourceLocationOutput heeft geen `recursive`, URIResolverRegistry wel
    default CompletableFuture<Void> remove(ISourceLocation loc, boolean recursive) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/output/rename")
    default CompletableFuture<Void> rename(URI from, URI to, boolean overwrite) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/watcher/watch")
    //TODO (Rodin): uitzoeken
    default CompletableFuture<Void> watch(WatchRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/watcher/unwatch")
    //TODO (Rodin): uitzoeken
    default CompletableFuture<Void> unwatch(WatchRequest req) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("rascal/vfs/logical/resolveLocation")
    default CompletableFuture<ISourceLocation> resolveLocation(ISourceLocation loc) {
        throw new UnsupportedOperationException();
    }

    //TODO (Rodin): @JsonRequest tag, plus wat betekent dit binnen Rascal?
    default CompletableFuture<String[]> fileSystemSchemes() {
        throw new UnsupportedOperationException();
    }

    //TODO (Rodin): @JsonRequest tag, plus wat betekent dit binnen Rascal?
    default void onDidChangeFile(FileChangeEvent event) {
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
            //TODO (RA): make defensive
            this.loc = ValueFactoryFactory.getValueFactory().sourceLocation(uri);
            this.recursive = recursive;
            this.watcher = watcher;
            this.excludes = new String[0];
        }

        public WatchRequest(String uri, boolean recursive, String[] excludes) {
            //TODO (RA): make defensive
            this.loc = ValueFactoryFactory.getValueFactory().sourceLocation(uri);
            this.recursive = recursive;
            this.watcher = "";
            this.excludes = excludes;
        }

        public ISourceLocation getLocation() {
            //TODO (RA): make defensive
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

    public static class SourceLocation {
        @NonNull private final String uri;
        private final int @Nullable[] offsetLength;
        private final int @Nullable[] beginLineColumn;
        private final int @Nullable[] endLineColumn;

        public static SourceLocation fromRascalLocation(ISourceLocation loc) {
            //TODO (RA): hier stond iets defensievers
            // var uri = Locations.toUri(loc).toString();
            var uri = loc.getURI().toASCIIString();
            if (loc.hasOffsetLength()) {
                if (loc.hasLineColumn()) {
                    return new SourceLocation(uri, loc.getOffset(), loc.getLength(), loc.getBeginLine(), loc.getBeginColumn(), loc.getEndLine(), loc.getEndColumn());
                }
                else {
                    return new SourceLocation(uri, loc.getOffset(), loc.getLength());
                }
            }
            else {
                return new SourceLocation(uri);
            }
        }

        public ISourceLocation toRascalLocation() throws URISyntaxException {
            final IValueFactory VF = IRascalValueFactory.getInstance();
            //TODO (RA): hier stond iets defensievers
            // ISourceLocation tmp = Locations.toCheckedLoc(uri);
            var tmp = ValueFactoryFactory.getValueFactory().sourceLocation(URI.create(uri));

            if (hasOffsetLength()) {
                if (hasLineColumn()) {
                    tmp = VF.sourceLocation(tmp,getOffset(), getLength(), getBeginLine(), getEndLine(), getBeginColumn(), getEndColumn());
                }
                else {
                    tmp = VF.sourceLocation(tmp, getOffset(), getLength());
                }
            }

            return tmp;
        }

        private SourceLocation(String uri, int offset, int length, int beginLine, int beginColumn, int endLine, int endColumn) {
            this.uri = uri;
            this.offsetLength = new int[] {offset, length};
            this.beginLineColumn = new int [] {beginLine, beginColumn};
            this.endLineColumn = new int [] {endLine, endColumn};
        }

        private SourceLocation(String uri, int offset, int length) {
            this.uri = uri;
            this.offsetLength = new int[] {offset, length};
            this.beginLineColumn = null;
            this.endLineColumn = null;
        }

        private SourceLocation(String uri) {
            this.uri = uri;
            this.offsetLength = null;
            this.beginLineColumn = null;
            this.endLineColumn = null;
        }

        public String getUri() {
            return uri;
        }

        @EnsuresNonNullIf(expression = "this.offsetLength", result = true)
        public boolean hasOffsetLength() {
            return offsetLength != null;
        }

        @EnsuresNonNullIf(expression = "this.endLineColumn", result = true)
        @EnsuresNonNullIf(expression = "this.beginLineColumn", result = true)
        public boolean hasLineColumn() {
            return beginLineColumn != null && endLineColumn != null;
        }

        public int getOffset() {
            if (!hasOffsetLength()) {
                throw new IllegalStateException("This location has no offset");
            }
            return offsetLength[0];
        }

        public int getLength() {
            if (!hasOffsetLength()) {
                throw new IllegalStateException("This location has no length");
            }
            return offsetLength[1];
        }

        public int getBeginLine() {
            if (!hasLineColumn()) {
                throw new IllegalStateException("This location has no line and columns");
            }
            return beginLineColumn[0];
        }

        public int getBeginColumn() {
            if (!hasLineColumn()) {
                throw new IllegalStateException("This location has no line and columns");
            }
            return beginLineColumn[1];
        }

        public int getEndLine() {
            if (!hasLineColumn()) {
                throw new IllegalStateException("This location has no line and columns");
            }
            return endLineColumn[0];
        }

        public int getEndColumn() {
            if (!hasLineColumn()) {
                throw new IllegalStateException("This location has no line and columns");
            }
            return endLineColumn[1];
        }
    }
    
    public static class FileChangeEvent {
        @NonNull private final FileChangeType type;
        @NonNull private final String uri;

        public FileChangeEvent(FileChangeType type, @NonNull String uri) {
            this.type = type;
            this.uri = uri;
        }

        public FileChangeType getType() {
            return type;
        }

        public ISourceLocation getLocation() throws URISyntaxException {
            //TODO (RA): hier stond iets defensievers
            // return Locations.toCheckedLoc(uri);
            return ValueFactoryFactory.getValueFactory().sourceLocation(URI.create(uri));
        }
    }

    public enum FileChangeType {
        Changed(1), Created(2), Deleted(3);

        private final int value;

        private FileChangeType(int val) {
            assert val == 1 || val == 2 || val == 3;
            this.value = val;
        }

        public int getValue() {
            return value;
        }
    }
}
