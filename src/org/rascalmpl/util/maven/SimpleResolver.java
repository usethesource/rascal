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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Repository;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Server;

/*package*/ class SimpleResolver implements ModelResolver {
    private static final String MAVEN_CENTRAL_ID = "central";
    private static final String MAVEN_CENTRAL_URL = "https://repo.maven.apache.org/maven2";

    public static SimpleResolver createRootResolver(Path rootRepository, HttpClient client, Map<String, Mirror> mirrors, Map<String, Server> servers) {
        SimpleResolver rootResolver = new SimpleResolver(rootRepository, client, mirrors, servers, null);
        Repository repo = new Repository();
        repo.setId(MAVEN_CENTRAL_ID);
        repo.setUrl(MAVEN_CENTRAL_URL);
        try {
            rootResolver.addRepository(repo);
            return rootResolver;
        }
        catch (InvalidRepositoryException e) {
            throw new RuntimeException("Invalid repository: " + repo.getId() + " at " + repo.getUrl(), e);
        }
    }

    private static RepositoryDownloaderFactory downloaderFactory;

    private final List<RepositoryDownloader> availableRepostories = new ArrayList<>();
    private final Path rootRepository;
    private final HttpClient client;

    private final Map<String, Mirror> mirrors;
    private final Map<String, Server> servers;

    private final SimpleResolver parentResolver;

    protected SimpleResolver(Path rootRepository, HttpClient client, Map<String, Mirror> mirrors, Map<String, Server> servers, SimpleResolver parentResolver) {
        this.rootRepository = rootRepository;
        this.client = client;
        this.mirrors = Map.copyOf(mirrors);
        this.servers = Map.copyOf(servers);

        downloaderFactory = new RepositoryDownloaderFactory(client);
        this.parentResolver = parentResolver;
    }

    public SimpleResolver createChildResolver() {
        return new SimpleResolver(rootRepository, client, mirrors, servers, this);
    }

    public SimpleResolver createSiblingResolver(String parentGroupId, Path parentPath) {
        return new SiblingResolver(rootRepository, client, mirrors, servers, parentResolver, parentGroupId, parentPath);
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

    private Path calculateMetadataPath(String groupId, String artifactId) {
        var result = rootRepository;
        for (var path : groupId.split("\\.")) {
            result = result.resolve(path);
        }
        result = result.resolve(artifactId);
        return result.resolve("maven-metadata.xml");
    }

    // Note: Because we download both the metadata and the pom files from the first repo that
    // has the file available, we could end up in a situation where the metadata file comes from 
    // a different repo than the pom file. This should not pose a problem as pom files with the
    // same version should be identical. If they are not, there is something seriously wrong with
    // the versioning in one of the repos and we are helpless to fix that anyway.
    // Caveat: for SNAPSHOT versions this could be a problem in some edge case scenario.
    private Metadata downloadArtifactMetadata(String groupId, String artifactId, String versionSpec) throws UnresolvableModelException {
        Path metadataPath = calculateMetadataPath(groupId, artifactId);
        var url = String.format("/%s/%s/%s", groupId.replace('.', '/'), artifactId, metadataPath.getFileName().toString());

        for (var repoDownloader : availableRepostories) {
            if (repoDownloader.getRepo().getLayout().equals("legacy")) {
                // TODO: support legacy repo
                continue;
            }

            Metadata metadata = repoDownloader.downloadMetadata(url, metadataPath);
            if (metadata != null) {
                return metadata;
            }
        }

        if (parentResolver != null) {
            return parentResolver.downloadArtifactMetadata(groupId, artifactId, versionSpec);
        }

        throw new UnresolvableModelException("Could not download artifact metadata from available repositories",
            groupId, artifactId, versionSpec);
    }

    public String findLatestMatchingVersion(String groupId, String artifactId, String versionSpec) throws UnresolvableModelException {
        var metadata = downloadArtifactMetadata(groupId, artifactId, versionSpec);
        try {
            VersionRange versionRange = VersionRange.createFromVersionSpec(versionSpec);
            return metadata.getVersioning().getVersions().stream()
                .map(version -> new DefaultArtifactVersion(version))
                .filter(version -> versionRange.containsVersion(version))
                .max((v1, v2) -> v1.compareTo(v2))
                .orElseThrow(() -> new UnresolvableModelException("No version found in range", groupId, artifactId, versionSpec))
                .toString();            
        } catch (InvalidVersionSpecificationException e) {
            throw new UnresolvableModelException("Invalid version range specification", groupId, artifactId, versionSpec, e);
        }
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
            this.availableRepostories.removeIf(r -> r.getRepo().getId().equals(repository.getId()));
        }
        Mirror mirror = mirrors.get(repository.getId());
        Repo repo = mirror == null ?  new Repo(repository) : new MirrorRepo(mirror,repository);
        this.availableRepostories.add(downloaderFactory.createDownloader(repo, servers.get(repo.getId())));
    }

    public void addDownloaders(SimpleResolver originalResolver) {
        for (var downloader : originalResolver.availableRepostories) {
            this.availableRepostories.add(downloader);
        }
     }

    @Override
    public ModelResolver newCopy() {
        var result = new SimpleResolver(rootRepository, client, mirrors, servers, parentResolver);
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
        for (var repoDownloader : availableRepostories) {
            if (repoDownloader.getRepo().getLayout().equals("legacy")) {
                // TODO: support legacy repo
                continue;
            }

            if (repoDownloader.download(url, local, force)) {
                return;
            }
        }

        if (parentResolver != null) {
            parentResolver.downloadArtifact(local, groupId, artifactId, version, force);
            return;
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
