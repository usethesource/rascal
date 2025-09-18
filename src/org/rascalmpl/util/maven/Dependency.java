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

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.maven.model.InputLocation;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.library.Messages;

import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;

/**
 * Identifies a listed dependency, not resolved to an artifact yet.
 */
/*package*/ class Dependency {
    private final ArtifactCoordinate coordinate;
    private final Scope scope;
    private final @Nullable String systemPath;
    private final boolean optional;
    private final Set<ArtifactCoordinate.WithoutVersion> exclusions;
    private final ISourceLocation pomLocation;
    private final int line;
    private final int column;

    private Dependency(ArtifactCoordinate coordinate, Scope scope, @Nullable String systemPath, boolean optional, Set<ArtifactCoordinate.WithoutVersion> exclusions, ISourceLocation pomLocation, int line, int column) {
        this.coordinate = coordinate;
        this.scope = scope;
        this.systemPath = systemPath;
        this.optional = optional;
        this.exclusions = exclusions;
        this.pomLocation = pomLocation;
        this.line = line;
        this.column = column;
    }

    public ArtifactCoordinate getCoordinate() {
        return coordinate;
    }

    public Scope getScope() {
        return scope;
    }

    public @Nullable String getSystemPath() {
        return systemPath;
    }

    public boolean isOptional() {
        return optional;
    }

    public Set<ArtifactCoordinate.WithoutVersion> getExclusions() {
        return exclusions;
    }

    public ISourceLocation getPomLocation() {
        return pomLocation;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getPosition() {
        return line + ":" + column;
    }

    static @Nullable Dependency build(org.apache.maven.model.Dependency d, IListWriter messages, ISourceLocation pomLocation) {
        var version = d.getVersion();
        if (version == null) {
            // while rare, this happens when a user has an incomplete dependencyManagement section
            messages.append(Messages.error("Dependency " + d.getGroupId() + ":" + d.getArtifactId() + "is missing", pomLocation));
            version = "???";
        }
        var coordinate = new ArtifactCoordinate(d.getGroupId(), d.getArtifactId(), version, d.getClassifier());
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
            .map(e -> ArtifactCoordinate.versionLess(e.getGroupId(), e.getArtifactId()))
            .collect(Collectors.toUnmodifiableSet());

        int line = -1, column = -1;
        InputLocation loc = d.getLocation("");
        if (loc != null) {
            line = loc.getLineNumber();
            column = loc.getColumnNumber();
        }

        return new Dependency(coordinate, scope, d.getSystemPath(), d.isOptional(), exclusions, pomLocation, line, column);
    }


    @Override
    public String toString() {
        return coordinate.toString() + "@" + scope + (optional ? "(optional)" : "");
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, scope, systemPath, optional, exclusions, pomLocation);
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
        return Objects.equals(coordinate, other.coordinate) 
            && scope == other.scope 
            && Objects.equals(systemPath, other.systemPath)
            && optional == other.optional
            && Objects.equals(exclusions, other.exclusions)
            && Objects.equals(pomLocation, other.pomLocation);
    }

    /*package*/ boolean shouldInclude(Scope forScope) {
        if (optional) {
            return false;
        }
        if (forScope == scope && scope != Scope.PROVIDED && scope != Scope.TEST) {
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
