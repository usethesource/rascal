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
package org.rascalmpl.uri.remote.jsonrpc;

import java.util.Objects;

import io.usethesource.vallang.ISourceLocation;

public class CopyRequest {
    private final ISourceLocation from;
    private final ISourceLocation to;
    private final boolean recursive;
    private final boolean overwrite;

    public CopyRequest(ISourceLocation from, ISourceLocation to, boolean recursive, boolean overwrite) {
        this.from = from;
        this.to = to;
        this.recursive = recursive;
        this.overwrite = overwrite;
    }

    public ISourceLocation getFrom() {
        return from;
    }

    public ISourceLocation getTo() {
        return to;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CopyRequest) {
            var other = (CopyRequest)obj;
            return Objects.equals(from, other.from)
                && Objects.equals(to, other.to)
                && recursive == other.recursive
                && overwrite == other.overwrite
                ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, recursive, overwrite);
    }
}
