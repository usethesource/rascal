/*
 * Copyright (c) 2025, Swat.engineering
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
package org.rascalmpl.uri.remote.jsonrpc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

import org.eclipse.lsp4j.jsonrpc.ResponseErrorException;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.rascalmpl.uri.UnsupportedSchemeException;

public enum RemoteIOError {
    FileExists(-1),
    FileNotFound(-2),
    IsADirectory(-3),
    IsNotADirectory(-4),
    DirectoryIsNotEmpty(-5),
    PermissionDenied(-6),
    UnsupportedScheme(-7),
    IllegalSyntax(-8),

    WatchAlreadyDefined(-10),
    WatchNotDefined(-11),

    FileSystemError(-20),

    IsRascalNative(-30),

    JsonRpcError(-40),

    Unknown(-100)
    ;

    public final int code;

    private static final Map<Integer, RemoteIOError> codeToError = new HashMap<>();

    static {
        Stream.of(values()).forEach(e -> codeToError.put(e.code, e));
    }

    private RemoteIOError(int code) {
        this.code = code;
    }

    private static RemoteIOError forCode(int code) {
        var error = codeToError.get(code);
        if (error == null) {
            throw new IllegalArgumentException("Unknown RemoteIOError code " + code);
        }
        return error;
    }

    public static ResponseErrorException translate(Throwable original) {
        if (original != null) {
            var cause = original;
            if (original instanceof CompletionException) {
                cause = original.getCause();
            }
            if (cause instanceof FileNotFoundException) {
                return createException(FileNotFound, cause, "File does not exist");
            } else if (cause instanceof UnsupportedSchemeException) {
                return createException(FileNotFound, cause, "Unsupported scheme");
            } else if (cause instanceof URISyntaxException) {
                return createException(FileNotFound, cause, "Invalid location syntax");
            } else if (cause instanceof FileAlreadyExistsException) {
                return createException(FileExists, cause, "File already exists");
            } else if (cause instanceof NotDirectoryException) {
                return createException(IsADirectory, cause, "Location is not a directory");
            } else if (cause instanceof SecurityException) {
                return createException(PermissionDenied, cause, "Permission denied");
            }
        }
        return createException(Unknown, original, "Unknown remote IO error occurred");
    }

    private static String messageOrDefault(Throwable t, String defaultMessage) {
        var message = t == null ? null : t.getMessage();
        return message == null ? defaultMessage : message;
    }

    private static ResponseErrorException createException(RemoteIOError error, Throwable cause, String defaultMessage) {
        return new ResponseErrorException(new ResponseError(error.code, messageOrDefault(cause, defaultMessage), null));
    }

    public static IOException translate(ResponseErrorException e) {
        var message = e.getResponseError() != null ? e.getResponseError().getMessage() : "";
        switch (forCode(e.getResponseError().getCode())) {
            case FileExists:
                return new FileAlreadyExistsException(message != "" ? message : "File exists");
            case FileNotFound:
                return new FileNotFoundException(message != "" ? message : "File does not exist");
            case IsADirectory:
                return new IOException(message != "" ? message : "Location is a directory");
            case IsNotADirectory:
                return new NotDirectoryException(message != "" ? message : "Location is not a directory");
            case DirectoryIsNotEmpty:
                return new DirectoryNotEmptyException(message != "" ? message : "Directory is not empty");
            case PermissionDenied:
                return new AccessDeniedException(message != "" ? message : "Permission denied");
            case UnsupportedScheme:
                return new IOException(message != "" ? message : "Unsupported scheme");
            case IllegalSyntax:
                return new IOException(message != "" ? message : "Invalid Json syntax");
            case WatchAlreadyDefined:
                return new IOException(message != "" ? message : "Watch already defined");
            case WatchNotDefined:
                return new IOException(message != "" ? message : "Watch not defined");
            case FileSystemError:
                return new IOException(message != "" ? message : "General file system error");
            case IsRascalNative:
                return new IOException(message != "" ? message : "Rascal native scheme should not be queried remotely");
            case JsonRpcError:
                return new IOException(message != "" ? message : "General JSON-RPC error");
            case Unknown:
                return new IOException(message != "" ? message : "Unknown Remote IO error");
            default:
                throw new IllegalArgumentException("Unexpected remote IO error: " + message, e);
        }
    }
}
