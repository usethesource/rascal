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

import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.maven.model.Exclusion;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.library.Messages;
import org.rascalmpl.uri.file.MavenRepositoryURIResolver;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;

/**
 * Identifies a listed dependency
 */
public class Dependency {
    private final ArtifactCoordinate coordinate;
    private final Scope scope;
    private final boolean found;
    private final boolean optional;
    private final List<String> exclusions;
    private final List<Dependency> dependencies;

    private Dependency(ArtifactCoordinate coordinate, Scope scope, boolean found, boolean optional, List<Dependency> dependencies, List<String> exclusions) {
        this.coordinate = coordinate;
        this.scope = scope;
        this.found = found;
        this.optional = optional;
        this.dependencies = dependencies;
        this.exclusions = exclusions;
    }

    public ArtifactCoordinate getCoordinate() {
        return coordinate;
    }

    public Scope getScope() {
        return scope;
    }

    public boolean isFound() {
        return found;
    }

    public boolean isOptional() {
        return optional;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public ISourceLocation getLocation() {
        return MavenRepositoryURIResolver.make(coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion(), "");
    }

    static Dependency build(org.apache.maven.model.Dependency d, IListWriter messages, CurrentResolution context) {
        var coodinate = new ArtifactCoordinate(d.getGroupId(), d.getArtifactId(), d.getVersion());
        Scope scope;

        switch (d.getScope() == null ? "compile" : d.getScope()) {
            case "provided": scope = Scope.PROVIDED; break;
            case "runtime": scope = Scope.RUNTIME; break;
            case "test": scope = Scope.TEST; break;
            case "system": scope = Scope.SYSTEM; break;
            case "import": scope = Scope.IMPORT; break;
            case "compile": // fall through
            default: scope = Scope.COMPILE; break;
        }

        var exclusions = d.getExclusions().stream()
            .map(e -> e.getGroupId() + ":" + e.getArtifactId())
            .collect(Collectors.toUnmodifiableList());


        var existing = context.dependencyCache.get(coodinate);
        if (existing != null && existing.exclusions.equals(exclusions)) {
            // do not calculate it again, but reuse for performance (some deps are very common)
            if (existing.scope == scope) {
                return existing;
            }
            return new Dependency(existing.coordinate, scope, existing.found, existing.optional, existing.dependencies, existing.exclusions);
        }

        if (context.cycleDetection.contains(coodinate)) {
            // we're in a cycle, so lets break it by returning a version of ourselves without calculated dependencies (and don't put it in the cache)
            return new Dependency(coodinate, scope, false, d.isOptional(), Collections.emptyList(), exclusions);
        }
        context.cycleDetection.add(coodinate);
        try {
            var dependencies = buildDependencies(d, messages, context);
            var result = new Dependency(coodinate, scope, dependencies != null, d.isOptional(), dependencies == null ? Collections.emptyList() : dependencies, exclusions);
            if (existing != null) {
                context.dependencyCache.put(coodinate, result);
            }
            return result;
        } finally {
            context.cycleDetection.remove(coodinate);
        }
    }

    private static @Nullable List<Dependency> buildDependencies(org.apache.maven.model.Dependency me, IListWriter messages,
        CurrentResolution context) {
        try {
            if (me.getVersion() == null) {
                // while rare, this happens when a user has an incomplete dependencyManagement section
                throw new UnresolvableModelException("Version of dependency missing", me.getGroupId(), me.getArtifactId(), me.getVersion());
            }
            if ("provided".equals(me.getScope())) {
                // current maven behavior seems to be:
                // - do not download provided dependencies
                // - if a provided dependency is present in the maven repository it's considered "provided"
                var pomDep = context.resolver.calculatePomPath(me.getGroupId(), me.getArtifactId(), me.getVersion());
                if (Files.notExists(pomDep)) {
                    // ok, doesn't exist yet. so don't download it to calculate dependencies
                    return null;
                }
            }
            // TODO: resolve system dependencies from system path instead of repositories
            var resolvedEntry = context.resolver.resolveModel(me);
            var fullDependencies = Project.parseRepositoryPom(resolvedEntry, context).getDependencies();
            var exclusions = me.getExclusions();
            if (exclusions.isEmpty()) {
                return fullDependencies;
            }
            return fullDependencies.stream()
                .filter(d -> !d.matches(exclusions))
                .collect(Collectors.toUnmodifiableList());
        } catch (UnresolvableModelException e) {
            var loc = context.pom;
            var artifactLoc = me.getLocation("artifactId");
            if (artifactLoc != null) {
                loc = IRascalValueFactory.getInstance().sourceLocation(loc , 0, 0, artifactLoc.getLineNumber(), artifactLoc.getColumnNumber(), artifactLoc.getLineNumber(), artifactLoc.getColumnNumber() + 1);
            }
            messages.append(Messages.warning("I could not resolve dependency in maven repository: " + me.getGroupId() + ":" + me.getArtifactId() + ":" + me.getVersion(), loc));
            return null;
        }
    }

    private boolean matches(List<Exclusion> exclusions) {
        for (var ex : exclusions) {
            if (ex.getGroupId().equals(coordinate.getGroupId()) && ex.getArtifactId().equals(coordinate.getArtifactId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return coordinate.toString() + "@" + scope + "["+found+"]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, scope, found, optional, dependencies, exclusions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Dependency)) {
            return false;
        }
        Dependency other = (Dependency) obj;
        return Objects.equals(coordinate, other.coordinate) && scope == other.scope && found == other.found && Objects.equals(exclusions, other.exclusions);
    }

    /*package*/ boolean shouldInclude(Scope forScope) {
        if (optional) {
            return false;
        }
        if (forScope == scope && scope != Scope.PROVIDED) {
            return true;
        }
        // for the ones where it's not the 
        switch (forScope) {
            case TEST:
                if (scope == Scope.COMPILE) {
                    return true;
                }
                // fall-through
            case COMPILE:
                if (scope == Scope.PROVIDED) {
                    return true;

                }
                // fall-through
            case RUNTIME:
                return scope == Scope.SYSTEM;
            case SYSTEM: // fall through: not a scope we request
            case IMPORT: // fall through
            case PROVIDED: // fall through
            default:
                return false;
        }
    }

}
