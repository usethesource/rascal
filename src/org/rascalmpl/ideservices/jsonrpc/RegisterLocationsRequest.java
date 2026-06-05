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
package org.rascalmpl.ideservices.jsonrpc;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;

public class RegisterLocationsRequest {
    private final IString scheme;
    private final IString authority;
    private final ISourceLocation[][] mapping;
    
    public RegisterLocationsRequest(IString scheme, IString authority, IMap mapping) {
        this.scheme = scheme;
        this.authority = authority;
        this.mapping = mapLocLocToLocArray(mapping);
    }

    public IString getScheme() {
        return scheme;
    }

    public IString getAuthority() {
        return authority;
    }

    public IMap getMapping() {
        return locArrayToMapLocLoc(mapping);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RegisterLocationsRequest) {
            var other = (RegisterLocationsRequest) obj;
            return Objects.equals(scheme, other.scheme)
                && Objects.equals(authority, other.authority)
                && Arrays.deepEquals(mapping, other.mapping);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 7 * Objects.hash(scheme, authority)
            + 13 * Arrays.deepHashCode(mapping);
    }

    /** 
     * This function takes a map of type `map[loc, loc]` and converts it to a two-dimensional array of ISourceLocations
     */
    private static ISourceLocation[][] mapLocLocToLocArray(IMap mapping) {
        return mapping.stream()
            .map(ITuple.class::cast)
            .map(e -> new ISourceLocation[] { (ISourceLocation) e.get(0), (ISourceLocation) e.get(1)})
            .toArray(n -> new ISourceLocation[n][2]);
    }

    /** 
     * This function takes a two-dimensional array of ISourceLocations and converts it to a map of type `map[loc, loc]`
     */
    private static IMap locArrayToMapLocLoc(ISourceLocation[][] mapping) {
        var vf = ValueFactoryFactory.getValueFactory();
        return Stream.of(mapping).map(e -> vf.tuple(e[0], e[1])).collect(vf.mapWriter());
    }
}
