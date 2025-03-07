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
package org.rascalmpl.util.maven;

import java.util.Map;

import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.resolution.ModelResolver;

import io.usethesource.vallang.ISourceLocation;

/*package*/ class CurrentResolution {
    final ISourceLocation pom;
    final ModelBuilder builder;
    final ModelResolver resolver;
    final Map<ArtifactCoordinate, Dependency> dependencyCache;

    public CurrentResolution(ModelBuilder builder, ModelResolver resolver,
        Map<ArtifactCoordinate, Dependency> dependencyCache, ISourceLocation rootPom) {
        this.pom = rootPom;
        this.builder = builder;
        this.resolver = resolver;
        this.dependencyCache = dependencyCache;
    }

    /**
     * The ModelResolver should not be shared between pom parses
     * So we have to make a new one.
     */
    CurrentResolution newParse(ISourceLocation pom, ModelResolver resolver) {
        return new CurrentResolution(builder, resolver, dependencyCache, pom);
    }
}
