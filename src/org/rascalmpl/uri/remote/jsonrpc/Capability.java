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

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A capability means that this feature is at least partially supported (namely only for specific schemes) and if full supported, no scheme limits are set
 */
public class Capability {
    private final CapabilityLevel level;
    private final Set<String> onlyForSchemes;

    public Capability(CapabilityLevel level, @Nullable String[] onlyForSchemes) {
        this.level = level;
        this.onlyForSchemes = onlyForSchemes == null ? Collections.emptySet() : Set.of(onlyForSchemes);
        if (level == CapabilityLevel.PARTIAL && this.onlyForSchemes.isEmpty()) {
            throw new IllegalArgumentException("Partial support should always include a list of the schemes that have support");
        }
        else if (level != CapabilityLevel.PARTIAL && !this.onlyForSchemes.isEmpty()) {
            throw new IllegalArgumentException("onlyForSchemes is only valid if the level is PARTIAL");
        }
    }

    public static Capability full() {
        return new Capability(CapabilityLevel.FULL, null);
    }

    public static Capability partial(String[] onlyForSchemes) {
        return new Capability(CapabilityLevel.PARTIAL, onlyForSchemes);
    }

    public static Capability unsupported() {
        return new Capability(CapabilityLevel.UNSUPPORTED, null);
    }

    public Set<String> getOnlyForSchemes() {
        return onlyForSchemes;
    }

    public boolean isFullSupport() {
        return level == CapabilityLevel.FULL;

    }

    public boolean isPartial() {
        return level == CapabilityLevel.PARTIAL;
    }

    public boolean isUnsupported() {
        return level == CapabilityLevel.UNSUPPORTED;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Capability) {
            var other = (Capability)obj;
            return other.level ==level 
                && Objects.equals(other.onlyForSchemes, onlyForSchemes);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, onlyForSchemes);
    }

}
 