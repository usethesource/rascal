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
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Repository;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.UnresolvableModelException;

/*package*/ class SimpleResolver implements ModelResolver {
    // TODO: support repository overrides with settings.xml


    private final List<SimpleRepositoryDownloader> availableRepostories = new ArrayList<>();
    private final Path rootRepository;
    private final ModelBuilder builder;
    private final HttpClient client;

    public SimpleResolver(Path rootRepository, ModelBuilder builder, HttpClient client) {
        this.rootRepository = rootRepository;
        this.builder = builder;
        this.client = client;
    }

    public Path calculatePomPath(ArtifactCoordinate coordinate) {
        return calculatePomPath(coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion());
    }


    public Path calculatePomPath(String groupId, String artifactId, String version) {
        var result = rootRepository;
        for (var path: groupId.split("\\.")) {
            result = result.resolve(path);
        }
        result = result.resolve(artifactId);
        result = result.resolve(version);
        return result.resolve(String.format("%s-%s.pom", artifactId, version));
    }

    private Path calculateJarPath(ArtifactCoordinate coordinate) {
        return calculateJarPath(coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion(), coordinate.getClassifier());
    }

    private Path calculateJarPath(String groupId, String artifactId, String version, String classifier) {
        var pomLocation = calculatePomPath(groupId, artifactId, version);
        var fileName = artifactId + "-" + version;
        if (!classifier.isEmpty()) {
            fileName += "-" + classifier;
        }
        return pomLocation.resolveSibling(fileName + ".jar");
    }

    @Override
    public ModelSource resolveModel(String groupId, String artifactId, String version)
        throws UnresolvableModelException {
        var local = calculatePomPath(groupId, artifactId, version);
        if (!Files.exists(local)) {
            downloadPom(local, groupId, artifactId, version);
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

    public ModelSource resolveModel(ArtifactCoordinate coordinate) throws UnresolvableModelException {
        return resolveModel(coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion());
    }

    @Override
    public void addRepository(Repository repository) throws InvalidRepositoryException {
        addRepository(repository, false);
    }

    @Override
    public void addRepository(Repository repository, boolean replace) throws InvalidRepositoryException {
        if (replace) {
            this.availableRepostories.removeIf(r -> r.repo.getId().equals(repository.getId()));
        }
        this.availableRepostories.add(new SimpleRepositoryDownloader(repository, client));
    }

    @Override
    public ModelResolver newCopy() {
        var result = new SimpleResolver(rootRepository, builder, client);
        result.availableRepostories.addAll(this.availableRepostories);
        return result;
    }

    private void downloadPom(Path local, String groupId, String artifactId, String version) throws UnresolvableModelException {
        if (version.endsWith("-SNAPSHOT")) {
            // TODO: snapshot handling, both in downloading and resolving paths?
            throw new UnresolvableModelException("No downloading & updating logic of SNAPSHOTs yet", groupId, artifactId, version);
        }
        downloadArtifact(local, groupId, artifactId, version, false);
    }

    private void downloadJar(Path local, String groupId, String artifactId, String version) throws UnresolvableModelException {
        if (version.endsWith("-SNAPSHOT")) {
            // TODO: snapshot handling, both in downloading and resolving paths?
            throw new UnresolvableModelException("No downloading & updating logic of SNAPSHOTs yet", groupId, artifactId, version);
        }
        downloadArtifact(local, groupId, artifactId, version, false);
    }

    private void downloadArtifact(Path local, String groupId, String artifactId, String version, boolean force) throws UnresolvableModelException {
        // TODO: deal with repository filters etc in settings.xml
        var url = getUrl(local, groupId, artifactId, version);
        for (var r : availableRepostories) {
            if (r.repo.getLayout().equals("legacy")) {
                // TODO: support legacy repo
                continue;
            }
            if (r.download(url, local, force)) {
                return;
            }
        }
        throw new UnresolvableModelException("Could not download artifact from available repositories", groupId, artifactId, version);
    }

    private String getUrl(Path local, String groupId, String artifactId, String version) {
        return String.format("/%s/%s/%s/%s", groupId.replace('.', '/'), artifactId,version, local.getFileName().toString());
    }

    public Path resolveJar(ArtifactCoordinate coordinate) throws UnresolvableModelException {
        var jarPath = calculateJarPath(coordinate);
        if (Files.notExists(jarPath)) {
            // lets try downloading
            downloadJar(jarPath, coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion());
        }
        return jarPath;
    }


    
}
