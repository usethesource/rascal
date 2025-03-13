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

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.maven.model.Model;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IValueFactory;

/**
 * This is a artifact that is resolved (or not in case of an error) and points to a jar. 
 */
public class Artifact {
    private final ArtifactCoordinate coordinate;
    private final @Nullable ArtifactCoordinate parentCoordinate;
    private final @Nullable Path resolved;
    private final List<Dependency> dependencies;

    private Artifact(ArtifactCoordinate coordinate, @Nullable ArtifactCoordinate parentCoordinate, @Nullable Path resolved, List<Dependency> dependencies) {
        this.coordinate = coordinate;
        this.parentCoordinate = parentCoordinate;
        this.resolved = resolved;
        this.dependencies = dependencies;
    }

    public ArtifactCoordinate getCoordinate() {
        return coordinate;
    }

    public @Nullable ArtifactCoordinate getParentCoordinate() {
        return parentCoordinate;
    }

    /**
     * The path where dependency is located, can be null in case we couldn't resolve it
     */
    public @Nullable Path getResolved() {
        return resolved;
    }

    /**
     * Calculate the dependencies for this artifact for a given scope
     * @param forScope
     * @return
     */
    public List<Dependency> getDependencies(Scope forScope) {
        return dependencies.stream()
            .filter(d -> d.shouldInclude(forScope))
            .collect(Collectors.toList());
    }

    private static final IValueFactory VF = IRascalValueFactory.getInstance();

    /*package*/ static @Nullable Artifact build(Model m, @Nullable Path pomLocation, String classifier, Set<ArtifactCoordinate.WithoutVersion> exclusions, CurrentResolution context) {
        if (m.getPackaging() != null && !"jar".equals(m.getPackaging())) {
            return null;
        }
        var coordinate = new ArtifactCoordinate(m.getGroupId(), m.getArtifactId(), m.getVersion(), classifier);
        var parentCoordinate = m.getParent() == null ? null : new ArtifactCoordinate(m.getParent().getGroupId(), m.getParent().getArtifactId(), m.getParent().getVersion(), "");
        var loc = calculateJarLocation(pomLocation, classifier);
        // TODO: make sure this jar is downloaded, as with the classifier it might not have been downloaded
        // we might move towards only downloading the jar when we need it, not when we download the pom. 
        // But then we have to keep track of where we downloaded the pom from (the _remote.repositories file)
        var dependencies = m.getDependencies().stream()
            .filter(d -> !"import".equals(d.getScope()))
            .filter(d -> !exclusions.contains(ArtifactCoordinate.versionLess(d.getGroupId(), d.getArtifactId())))
            .map(d -> Dependency.build(d, VF.listWriter(), context))
            .collect(Collectors.toUnmodifiableList())
            ;
        return new Artifact(coordinate,  parentCoordinate, loc, dependencies);

    }

    /*package*/ static Artifact unresolved(ArtifactCoordinate coordinate) {
        return new Artifact(coordinate, null, null, Collections.emptyList());
    }

    private static @Nullable Path calculateJarLocation(@Nullable Path pomLocation, String classifier) {
        if (pomLocation == null) {
            return null;
        }
        var filename = pomLocation.getFileName().toString();
        if (filename.endsWith(".pom")) {
            filename = filename.substring(0, filename.length() - 4);
        }
        if (!classifier.isEmpty()) {
            filename += "-" + classifier;
        }
        return pomLocation.resolveSibling(filename + ".jar");
    }

    @Override
    public String toString() {
        return coordinate + "[" + (resolved != null ? "resolved" : "missing" )+ "]";
    }

}
