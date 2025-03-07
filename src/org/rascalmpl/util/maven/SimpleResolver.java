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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Repository;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.UnresolvableModelException;

/*package*/ class SimpleResolver implements ModelResolver {
    // TODO: support repository overrides with settings.xml


    private final List<Repository> availableRepostories = new ArrayList<>();
    private final Path rootRepository;

    public SimpleResolver() {
        this.rootRepository = Util.mavenRepository();
    }

    private Path calculatePomPath(String groupId, String artifactId, String version) {
        var result = rootRepository;
        for (var path: groupId.split("\\.")) {
            result = result.resolve(path);
        }
        result = result.resolve(artifactId);
        result = result.resolve(version);
        return result.resolve(String.format("%s-%s.pom", artifactId, version));
    }

    @Override
    public ModelSource resolveModel(String groupId, String artifactId, String version)
        throws UnresolvableModelException {
        var local = calculatePomPath(groupId, artifactId, version);
        if (!Files.exists(local)) {
            download(local, groupId, artifactId, version, availableRepostories);
        }
        return new FileModelSource(local.toFile());
    }


    @Override
    public ModelSource resolveModel(Parent parent) throws UnresolvableModelException {
        return resolveModel(parent.getGroupId(), parent.getArtifactId(), parent.getVersion());
    }

    @Override
    public ModelSource resolveModel(Dependency dependency) throws UnresolvableModelException {
        return resolveModel(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion());
    }

    @Override
    public void addRepository(Repository repository) throws InvalidRepositoryException {
        this.availableRepostories.add(repository);
    }

    @Override
    public void addRepository(Repository repository, boolean replace) throws InvalidRepositoryException {
        if (replace) {
            this.availableRepostories.removeIf(p -> p.getId().equals(repository.getId()));
        }
    }

    @Override
    public ModelResolver newCopy() {
        var result = new SimpleResolver();
        result.availableRepostories.addAll(this.availableRepostories);
        return result;
    }

    static void download(Path local, String groupId, String artifactId, String version, List<Repository> availableRepostories) throws UnresolvableModelException {
        // TODO: download pom & jar (if it's a jar dependency)
        throw new UnresolvableModelException("No downloading yet", groupId, artifactId, version);
    }

    
}
