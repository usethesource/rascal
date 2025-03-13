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

public class ArtifactCoordinate {

    public static final ArtifactCoordinate UNKNOWN = new ArtifactCoordinate("unkown", "unknown", "?");

    private final String groupId;
    private final String artifactId;
    private final String version;

    ArtifactCoordinate(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getArtifactId() {
        return artifactId;
    }
    public String getGroupId() {
        return groupId;
    }
    public String getVersion() {
        return version;
    }

    /*package*/ WithoutVersion versionLess() {
        return new WithoutVersion(this);
    }

    /*package*/ static WithoutVersion versionLess(String groupId, String artifactId) {
        return new WithoutVersion(new ArtifactCoordinate(groupId, artifactId, "???"));
    }

    /*package*/ static class WithoutVersion {
        private final ArtifactCoordinate base;
        public WithoutVersion(ArtifactCoordinate base) {
            this.base = base;
        }
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof WithoutVersion)) {
                return false;
            }
            var otherBase = ((WithoutVersion)obj).base;
            return base.groupId.equals(otherBase.groupId) 
                && base.artifactId.equals(otherBase.artifactId);
        }
        @Override
        public int hashCode() {
            return base.groupId.hashCode() 
                + base.artifactId.hashCode() * 11
                ;
        }

    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ArtifactCoordinate)) {
            return false;
        }
        ArtifactCoordinate other = (ArtifactCoordinate) obj;
        return Objects.equals(groupId, other.groupId) && Objects.equals(artifactId, other.artifactId)
            && Objects.equals(version, other.version);
    }



}
