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
package org.rascalmpl.uri.remote.jsonrpc;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;


public class CapabilitiesResponse {
    /**
     * Are the `/input/` APIs supported, null is a shorthand a capability of level for{@link CapabilityLevel#UNSUPPORTED}.
     */
    private final @Nullable Capability input;
    /**
     * Are the `/output/` APIs supported, null is a shorthand a capability of level for{@link CapabilityLevel#UNSUPPORTED}.
     */
    private final @Nullable Capability output;
    /**
     * Are the `/watch/` APIs supported, null is a shorthand a capability of level for{@link CapabilityLevel#UNSUPPORTED}
     */
    private final @Nullable Capability watch;
    /**
     * Are the `/logical/` APIs supported, null is a shorthand a capability of level for{@link CapabilityLevel#UNSUPPORTED}
     */
    private final @Nullable Capability logical;
    /**
     * Is the `/getCharset` API supported, null is a shorthand a capability of level for{@link CapabilityLevel#UNSUPPORTED}
     */
    private final @Nullable Capability getCharset;

    public CapabilitiesResponse(@Nullable Capability input, @Nullable Capability watch, @Nullable Capability output, @Nullable Capability logical,
        @Nullable Capability getCharset) {
        this.input = input;
        this.watch = watch;
        this.output = output;
        this.logical = logical;
        this.getCharset = getCharset;
    }

    /** as GSON will set null fields, but we don't want this in our code, we replace them by unsupported in the getter */
    private static Capability replaceNull(@Nullable Capability cap) {
        return cap == null ? Capability.unsupported() : cap;
    }


    public Capability getGetCharset() {
        return replaceNull(getCharset);
    }

    public Capability getLogical() {
        return replaceNull(logical);
    }
    public Capability getWatch() {
        return replaceNull(watch);
    }

    public Capability getInput() {
        return replaceNull(input);
    }

    public Capability getOutput() {
        return replaceNull(output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, output, watch, logical, getCharset);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CapabilitiesResponse)) {
            return false;
        }
        CapabilitiesResponse other = (CapabilitiesResponse) obj;
        return Objects.equals(input, other.input) && Objects.equals(output, other.output)
            && Objects.equals(watch, other.watch) && Objects.equals(logical, other.logical)
            && Objects.equals(getCharset, other.getCharset);
    }

}
