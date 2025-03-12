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

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelCache;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.WorkspaceModelResolver;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.usethesource.vallang.ISourceLocation;

/**
 * A class to keep track of some state & caches thats usefull to pass around
 */
/*package*/ class CurrentResolution {
    final ISourceLocation pom;
    final Path rootRepository;
    final ModelBuilder builder;
    final SimpleResolver resolver;
    final Map<ArtifactCoordinate, Dependency> dependencyCache;
    final HttpClient client;
    final Set<ArtifactCoordinate> cycleDetection;
    final ModelCache modelCache;
    final WorkspaceModelResolver workspaceResolver;

    public CurrentResolution(ModelBuilder builder,
        Map<ArtifactCoordinate, Dependency> dependencyCache, ISourceLocation rootPom, HttpClient client, Path rootRepository) {
        this(builder, dependencyCache, rootPom, client, rootRepository, new HashSet<>(), new CaffeineModelCache());

    }
    private CurrentResolution(ModelBuilder builder,
        Map<ArtifactCoordinate, Dependency> dependencyCache, 
        ISourceLocation rootPom, 
        HttpClient client, 
        Path rootRepository, 
        Set<ArtifactCoordinate> cycleDetection, 
        ModelCache cache) {
        this.pom = rootPom;
        this.builder = builder;
        this.resolver = new SimpleResolver(rootRepository, builder, client);
        this.workspaceResolver = new SimpleWorkspaceResolver(this);
        this.dependencyCache = dependencyCache;
        this.rootRepository = rootRepository;
        this.client = client;
        this.cycleDetection = cycleDetection;
        this.modelCache = cache;
    }

    /**
     * The ModelResolver should not be shared between pom parses
     * So we have to make a new one.
     */
    CurrentResolution newParse(ISourceLocation pom) {
        return new CurrentResolution(builder, dependencyCache, pom, client, rootRepository, cycleDetection, modelCache);
    }

    private static final class CaffeineModelCache implements ModelCache {
        private static final class Key {
            private final String groupId; 
            private final String artifactId;
            private final String version;
            private final String tag;

            public Key(String groupId, String artifactId, String version, String tag) {
                this.groupId = groupId;
                this.artifactId = artifactId;
                this.version = version;
                this.tag = tag;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + groupId.hashCode();
                result = prime * result + artifactId.hashCode();
                result = prime * result + version.hashCode();
                result = prime * result + tag.hashCode();
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null || !(obj instanceof Key))
                    return false;
                Key other = (Key) obj;
                return groupId.equals(other.groupId)
                    && artifactId.equals(artifactId)
                    && version.equals(other.version)
                    && tag.equals(other.tag);
            }
        }

        private final Cache<Key, Object> modelCache = Caffeine.newBuilder()
            .maximumSize(100)
            .build();


        @Override
        public void put(String groupId, String artifactId, String version, String tag, Object data) {
            modelCache.put(new Key(groupId, artifactId, version, tag), data);
        }

        @Override
        public Object get(String groupId, String artifactId, String version, String tag) {
            return modelCache.getIfPresent(new Key(groupId, artifactId, version, tag));
        }
    }
}
