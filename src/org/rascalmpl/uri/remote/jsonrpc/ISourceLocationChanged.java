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

import org.rascalmpl.uri.ISourceLocationWatcher;

import io.usethesource.vallang.ISourceLocation;

public class ISourceLocationChanged {
    private final ISourceLocation root;
    private final ISourceLocationChangeType type;
    private final String watchId;

    public ISourceLocationChanged(ISourceLocation root, ISourceLocationChangeType type, String watchId) {
        this.root = root;
        this.type = type;
        this.watchId = watchId;
    }
    
    public ISourceLocation getRoot() {
        return root;
    }

    public ISourceLocationChangeType getChangeType() {
        return type;
    }

    public String getWatchId() {
        return watchId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ISourceLocationChanged) {
            var other = (ISourceLocationChanged)obj;
            return Objects.equals(root, other.root)
                && Objects.equals(type, other.type)
                && Objects.equals(watchId, other.watchId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, type, watchId);
    }

    public ISourceLocationWatcher.ISourceLocationChanged translate() {
        return ISourceLocationWatcher.makeChange(getRoot(), ISourceLocationChangeType.translate(type));
    }

    @Override
    public String toString() {
        return "ISourceLocationChanged [changeType=" + type + ", root=" + root + ", watchId=" + watchId + "]";
    }
}
