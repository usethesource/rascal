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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import org.apache.commons.codec.binary.Base64InputStream;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.eclipse.lsp4j.jsonrpc.ResponseErrorException;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChangeType;
import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChanged;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.UnsupportedSchemeException;
import org.rascalmpl.uri.vfs.IRemoteResolverRegistry;
import org.rascalmpl.util.NamedThreadPool;

import io.usethesource.vallang.ISourceLocation;

public interface IRascalFileSystemServices extends IRemoteResolverRegistry {
    static final URIResolverRegistry reg = URIResolverRegistry.getInstance();
    public static final ExecutorService executor = NamedThreadPool.cachedDaemon("rascal-vfs");

    @Override
    default public CompletableFuture<ISourceLocation> resolveLocation(ISourceLocation loc) {
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
    default public CompletableFuture<Void> watch(WatchRequest params) {
        return CompletableFuture.runAsync(() -> {
            try {
                ISourceLocation loc = params.getLocation();

                URIResolverRegistry.getInstance().watch(loc, params.isRecursive(), changed -> {
                    try {
                        onDidChangeFile(changed.getLocation(), changed.getChangeType().getValue(), null);
                    } catch (/*IO*/Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException | RuntimeException e) {
                throw new VSCodeFSError(e);
            }
        }, executor);
    }

    static FileChangeEvent convertChangeEvent(ISourceLocationChanged changed) throws IOException {
        return new FileChangeEvent(convertFileChangeType(changed.getChangeType()), changed.getLocation().getURI().toASCIIString());
    }

    static FileChangeType convertFileChangeType(ISourceLocationChangeType changeType) throws IOException {
        switch (changeType) {
            case CREATED:
                return FileChangeType.Created;
            case DELETED:
                return FileChangeType.Deleted;
            case MODIFIED:
                return FileChangeType.Changed;
            default:
                throw new IOException("unknown change type: " + changeType);
        }
    }

    @Override
    default public CompletableFuture<FileAttributes> stat(ISourceLocation loc) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!reg.exists(loc)) {
                    throw new FileNotFoundException();
                }
                return reg.stat(loc);
            } catch (IOException | RuntimeException e) {
                throw new VSCodeFSError(e);
            }
        }, executor);
    }

    @Override
    default public CompletableFuture<FileWithType[]> list(ISourceLocation loc) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!reg.isDirectory(loc)) {
                    throw VSCodeFSError.notADirectory(loc);
                }
                return Arrays.stream(reg.list(loc)).map(l -> new FileWithType(URIUtil.getLocationName(l),
                        reg.isDirectory(l) ? FileType.Directory : FileType.File)).toArray(FileWithType[]::new);
            } catch (IOException | RuntimeException e) {
                throw new VSCodeFSError(e);
            }
        }, executor);
    }

    @Override
    default public CompletableFuture<Void> mkDirectory(ISourceLocation loc) {
        return CompletableFuture.runAsync(() -> {
            try {
                reg.mkDirectory(loc);
            } catch (IOException | RuntimeException e) {
                throw new VSCodeFSError(e);
            }
        }, executor);
    }

    @Override
    default public CompletableFuture<String> readFile(ISourceLocation loc) {
        return CompletableFuture.supplyAsync(() -> {
            try (InputStream source = new Base64InputStream(reg.getInputStream(loc), true)) {
                return new String(source.readAllBytes(), StandardCharsets.US_ASCII);
            } catch (IOException | RuntimeException e) {
                throw new VSCodeFSError(e);
            }
        }, executor);
    }

    @Override
    default public CompletableFuture<Void> writeFile(ISourceLocation loc, String content, boolean append, boolean create, boolean overwrite) {
        return CompletableFuture.runAsync(() -> {
            try {
                boolean fileExists = reg.exists(loc);
                if (!fileExists && !create) {
                    throw new FileNotFoundException(loc.toString());
                }
                if (fileExists && reg.isDirectory(loc)) {
                    throw VSCodeFSError.isADirectory(loc);
                }

                ISourceLocation parentFolder = URIUtil.getParentLocation(loc);
                if (!reg.exists(parentFolder) && create) {
                    throw new FileNotFoundException(parentFolder.toString());
                }

                if (fileExists && create && !overwrite) {
                    throw new FileAlreadyExistsException(loc.toString());
                }
                try (OutputStream target = reg.getOutputStream(loc, false)) {
                    target.write(Base64.getDecoder().decode(content));
                }
            } catch (IOException | RuntimeException e) {
                throw new VSCodeFSError(e);
            }
        }, executor);
    }

    @Override
    default public CompletableFuture<Void> remove(ISourceLocation loc, boolean recursive) {
        return CompletableFuture.runAsync(() -> {
            try {
                reg.remove(loc, recursive);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    default public CompletableFuture<Void> rename(ISourceLocation from, ISourceLocation to, boolean overwrite) {
        return CompletableFuture.runAsync(() -> {
            try {
                reg.rename(from, to, overwrite);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    default public void onDidChangeFile(ISourceLocation loc, int type, String watchId) {
        // reg.watch(loc, false, null);
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
            return null;
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

    /** Maps common exceptions to FileSystemError in VS Code */
    public static class VSCodeFSError extends ResponseErrorException {
        //TODO (RA): zit ook in Rascal
        public VSCodeFSError(Exception original) {
            super(translate(original));
        }

        private static ResponseError fileExists(Object data) {
            return new ResponseError(-1, "File exists", data);
        }
        private static ResponseError fileIsADirectory(Object data) {
            return new ResponseError(-2, "File is a directory", data);
        }
        private static ResponseError fileNotADirectory(Object data) {
            return new ResponseError(-3, "File is not a directory", data);
        }
        private static ResponseError fileNotFound(Object data) {
            return new ResponseError(-4, "File is not found", data);
        }
        private static ResponseError noPermissions(Object data) {
            return new ResponseError(-5, "No permissions", data);
        }
        @SuppressWarnings("unused")
        private static ResponseError unavailable(Object data) {
            return new ResponseError(-6, "Unavailable", data);
        }

        private static ResponseError generic(@Nullable String message, Object data) {
            return new ResponseError(-99, message == null ? "no error message was provided" : message, data);
        }

        public static ResponseErrorException notADirectory(Object data) {
            return new ResponseErrorException(fileNotADirectory(data));
        }

        public static ResponseErrorException isADirectory(Object data) {
            return new ResponseErrorException(fileIsADirectory(data));
        }

        private static ResponseError translate(Exception original) {
            if (original instanceof FileNotFoundException
                || original instanceof UnsupportedSchemeException
                || original instanceof URISyntaxException
            ) {
                return fileNotFound(original);
            }
            else if (original instanceof FileAlreadyExistsException) {
                return fileExists(original);
            }
            else if (original instanceof NotDirectoryException) {
                return fileNotADirectory(original);
            }
            else if (original instanceof SecurityException) {
                return noPermissions(original);
            }
            else if (original instanceof ResponseErrorException) {
                return ((ResponseErrorException)original).getResponseError();
            }
            return generic(original.getMessage(), original);
        }
    }
}
