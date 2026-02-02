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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.apache.maven.model.resolution.UnresolvableModelException;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Server;

/**
 * This class helps us find siblings in a multi-module pom project
 * It simulates how maven in a multi-module reactor build finds siblings if they
 * depend on each-other
 * 
 * Note for brevity sake we don't verify version numbers, 
 * as that would mean parsing the pom (and it's parent pom). But we do check if
 * the version is not a -SNAPSHOT and it exist in the local repo, we use that,
 * for cases of local installs
 */
/*package*/ class SiblingResolver extends SimpleResolver {

    private final Path parentPath;
    private final String groupId;

    protected SiblingResolver(Path rootRepository, HttpClient client, Map<String, Mirror> mirrors,
        Map<String, Server> servers, SimpleResolver parentResolver, String groupId, Path parentPath) {
        super(rootRepository, client, mirrors, servers, parentResolver);
        this.groupId = groupId;
        this.parentPath = parentPath;
    }

    @Override
    public Path calculatePomPath(String groupId, String artifactId, String version) {
        Path result = super.calculatePomPath(groupId, artifactId, version);
        if (!version.endsWith("-SNAPSHOT") && Files.exists(result)) {
            // we should not link to a sibling if the specific version is in the pom.xml 
            // (for example the result of a previous local install)
            return result;
        }
        if (this.groupId.equals(groupId)) {
            var siblingResult = parentPath.resolve(artifactId).resolve("pom.xml");
            if (Files.exists(siblingResult)) {
                return siblingResult;
            }
        }
        return result;
    }



    @Override
    public Path resolveJar(ArtifactCoordinate coordinate) throws UnresolvableModelException {
        Path siblingResult = parentPath.resolve(coordinate.getArtifactId());
        if (!coordinate.getVersion().endsWith("-SNAPSHOT") 
            || !this.groupId.equals(coordinate.getGroupId())
            || !Files.exists(siblingResult)) {
            try {
                return super.resolveJar(coordinate);
            }
            catch (UnresolvableModelException mre) {
                if (Files.exists(siblingResult)) {
                    return siblingResult;
                }
                throw mre;
            }
        }
        return siblingResult;
    }
}
