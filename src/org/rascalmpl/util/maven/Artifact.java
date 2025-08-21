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

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.maven.model.Model;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.library.Messages;
import org.rascalmpl.util.maven.ArtifactCoordinate.WithoutVersion;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;

/**
 * This is a artifact that is resolved (or not in case of an error) and points to a jar. 
 */
public class Artifact {
    private final ArtifactCoordinate coordinate;
    private final @Nullable ArtifactCoordinate parentCoordinate;
    private final @Nullable Path resolved;
    private final List<Dependency> dependencies;
    private final @Nullable SimpleResolver ourResolver;
    private IList messages;
    private final boolean anyError;

    private Artifact(ArtifactCoordinate coordinate, @Nullable ArtifactCoordinate parentCoordinate, @Nullable Path resolved, List<Dependency> dependencies, IList messages, @Nullable SimpleResolver originalResolver) {
        this.coordinate = coordinate;
        this.parentCoordinate = parentCoordinate;
        this.resolved = resolved;
        this.dependencies = dependencies;
        this.messages = messages;
        this.ourResolver = originalResolver;
        this.anyError = messages.stream().anyMatch(Messages::isError);
    }

    private Artifact(Artifact original, String version) {
        var coord = original.coordinate;
        this.coordinate = new ArtifactCoordinate(coord.getGroupId(), coord.getArtifactId(), version, coord.getClassifier());
        this.parentCoordinate = original.parentCoordinate;
        this.resolved = original.resolved;
        this.dependencies = original.dependencies;
        this.messages = original.messages;
        this.ourResolver = original.ourResolver;
        this.anyError = original.anyError;
    }

    public ArtifactCoordinate getCoordinate() {
        return coordinate;
    }

    public @Nullable ArtifactCoordinate getParentCoordinate() {
        return parentCoordinate;
    }

    public boolean hasErrors() {
        return anyError;
    }

    public IList getMessages() {
        return messages;
    }

    /**
     * The path where the jar is located, can be null in case we couldn't resolve it
     * Note, for the root project, it's will always be null, as we won't resolve it to a location in the repository.
     */
    public @Nullable Path getResolved() {
        return resolved;
    }

    /*package*/ List<Dependency> getDependencies() {
        return dependencies;
    }

    /**
     * Calculate the dependencies for this artifact for a given scope
     * @param forScope
     * @return
     */
    public List<Artifact> resolveDependencies(Scope forScope, MavenParser parser) {
        Set<WithoutVersion> alreadyIncluded = new HashSet<>();
        var result = new ArrayList<Artifact>(dependencies.size());
        var rangedDeps = new HashMap<WithoutVersion, SortedSet<String>>();
        calculateClassPath(forScope, alreadyIncluded, rangedDeps, result, parser);
        return result;
    }

    private static boolean isVersionRange(String version) {
        return version != null && (version.startsWith("[") || version.startsWith("("));
    }

    /**
     * For unrestricted versions (just simple version numbers), we will use the first version
     * it encounters. This means top-level versions take preference.
     * For version ranges maven uses the latest version that matches all ranges. If no such version
     * exists, resolution fails.
     */
    private void calculateClassPath(Scope forScope, Set<WithoutVersion> alreadyIncluded, Map<WithoutVersion, SortedSet<String>> rangedDeps, ArrayList<Artifact> result, MavenParser parser) {
        var nextLevel = new ArrayList<Artifact>(dependencies.size());
        for (var d : dependencies) {
            var coordinate = d.getCoordinate();
            var withoutVersion = coordinate.versionLess();

            var version = coordinate.getVersion();
            if (isVersionRange(version)) {
                try {
                    version = ourResolver.findLatestMatchingVersion(coordinate.getGroupId(), coordinate.getArtifactId(), version);
                    coordinate = new ArtifactCoordinate(coordinate.getGroupId(), coordinate.getArtifactId(), version, coordinate.getClassifier());
                }
                catch (UnresolvableModelException e) {
                    messages = messages.append(Messages.error("Version range error: " + e.getMessage(), d.getPomLocation()));
                    continue;
                }

                var versions = rangedDeps.computeIfAbsent(withoutVersion, k -> new TreeSet<>());
                if (versions.add(version) && versions.size() == 2) { // Only add a warning once
                    String effectiveVersion = versions.first();
                    messages = messages.append(Messages.warning("Multiple version ranges found for " + withoutVersion + " are used. " 
                        + "It is better to lock the desired version in your own pom to a specifick version, for example <version>["+ effectiveVersion + "]</version>", d.getPomLocation()));
                }
            }

            if (alreadyIncluded.contains(withoutVersion) || !d.shouldInclude(forScope)) {
                continue;
            }
            if (d.getScope() == Scope.PROVIDED) {
                // current maven behavior seems to be:
                // - do not download provided dependencies
                // - if a provided dependency is present in the maven repository it's considered "provided"
                var pomDep = ourResolver.calculatePomPath(d.getCoordinate());
                if (!Files.notExists(pomDep)) {
                    // ok, doesn't exist yet. so don't download it to calculate dependencies
                    // and don't add it to the list since somewhere else somebody might depend on it 
                    continue;
                }
            }
            if (d.getScope() == Scope.SYSTEM) {
                result.add(createSystemArtifact(d));                
                continue;
            }

            var art = parser.parseArtifact(coordinate, d.getExclusions(), ourResolver);
            if (art != null) {
                result.add(art);
                nextLevel.add(art);
            }
            alreadyIncluded.add(withoutVersion);
        }
        if (forScope == Scope.TEST) {
            // only do test scope for top level, switch to compile after that
            forScope = Scope.COMPILE;
        }
        // now we go through the new artifacts and make sure we add their dependencies if needed 
        // but now in their context (their resolver etc)
        for (var a: nextLevel) {
            a.calculateClassPath(forScope, alreadyIncluded, rangedDeps, result, parser);
        }
    }

    private static Artifact createSystemArtifact(Dependency d) {
        var messages = IRascalValueFactory.getInstance().listWriter();

        String systemPath = d.getSystemPath();
        Path path = null;
        if (systemPath == null) {
            messages.append(Messages.error("system dependency " + d + " without a systemPath property", d.getPomLocation()));
        } else {
            path = Path.of(systemPath);
            if (Files.notExists(path) || !Files.isRegularFile(path)) {
                // We have a system path, but it doesn't exist or is not a file, so keep the dependency unresolved.
                messages.append(Messages.error("systemPath property (of" + d + ") points to a file that does not exist (or is not a regular file): " + systemPath, d.getPomLocation()));
                path = null;
            }
        }

        return new Artifact(d.getCoordinate(), null, path, Collections.emptyList(), messages.done(), null);
    }

    /*package*/ static @Nullable Artifact build(Model m, boolean isRoot, Path pom, ISourceLocation pomLocation, String classifier, Set<ArtifactCoordinate.WithoutVersion> exclusions, IListWriter messages, SimpleResolver resolver) {
        if (m.getPackaging() != null && !("jar".equals(m.getPackaging()) || "eclipse-plugin".equals(m.getPackaging()))) {
            // we do not support non-jar artifacts right now
            return null;
        }

        var coordinate = new ArtifactCoordinate(m.getGroupId(), m.getArtifactId(), m.getVersion(), classifier);
        var parent = m.getParent();
        var parentCoordinate = parent == null ? null : new ArtifactCoordinate(parent.getGroupId(), parent.getArtifactId(), parent.getVersion(), "");
        try {
            var loc = isRoot ? null : resolver.resolveJar(coordinate); // download jar if needed
            var dependencies = m.getDependencies().stream()
                .filter(d -> !"import".equals(d.getScope()))
                .filter(d -> !exclusions.contains(ArtifactCoordinate.versionLess(d.getGroupId(), d.getArtifactId())))
                .map(d -> Dependency.build(d, messages, pomLocation))
                .collect(Collectors.toUnmodifiableList())
                ;
            return new Artifact(coordinate, parentCoordinate, loc, dependencies, messages.done(), resolver);
        } catch (UnresolvableModelException e) {
            messages.append(Messages.error("Could not download corresponding jar", pomLocation));
            return new Artifact(coordinate, parentCoordinate, null, Collections.emptyList(), messages.done(), resolver);
        }

    }

    /*package*/ static Artifact unresolved(ArtifactCoordinate coordinate, IListWriter messages) {
        return new Artifact(coordinate, null, null, Collections.emptyList(), messages.done(), null);
    }

    @Override
    public String toString() {
        return coordinate + "[" + (resolved != null ? "resolved" : "missing" )+ "]";
    }

    public void report(PrintWriter target) {
        target.append("coordinate: ");
        target.append(coordinate.toString());
        target.append("\n");

        target.append("parent: ");
        target.append(parentCoordinate == null ? "null" : parentCoordinate.toString());
        target.append("\n");

        target.append("resolved: ");
        target.append(resolved == null ? "null" : resolved.toString());
        target.append("\n");

        target.append("dependencies (unresolved):\n");
        for (var d : dependencies) {
            target.append("- ");
            target.append(d.toString());
            target.append("\n");
        }

        target.append("messsages:\n");
        Messages.write(messages, target);
    }


}
