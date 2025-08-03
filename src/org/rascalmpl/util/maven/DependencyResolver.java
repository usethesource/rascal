package org.rascalmpl.util.maven;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.rascalmpl.util.maven.ArtifactCoordinate.WithoutVersion;

public class DependencyResolver {
    private static class Node {
        private Artifact artifact;
        private int index;

        public Node(Artifact artifact, int index) {
            this.artifact = artifact;
            this.index = index;
        }

        public Artifact getArtifact() {
            return artifact;
        }

        public int getIndex() {
            return index;
        }
    }

    private Scope scope;
    private Map<WithoutVersion, Artifact> artifacts;

    public DependencyResolver(Scope scope, Artifact rootArtifact) throws UnresolvableModelException{
        this.scope = scope;
        artifacts = new LinkedHashMap<>();
    }

    public void gatherArtifacts(Artifact artifact, MavenParser parser, SimpleResolver resolver) throws UnresolvableModelException {
        Queue<Artifact> queue = new LinkedList<>();
        gatherArtifacts(queue, 0, parser, resolver);
    }

    private void gatherArtifacts(Queue<Artifact> todo, int index, MavenParser parser, SimpleResolver resolver) throws UnresolvableModelException {
        /*while (!todo.isEmpty()) {
            Artifact artifact = todo.poll();
            System.err.println("gathering artifacts for " + artifact.getCoordinate());
            artifacts.put(artifact.getCoordinate().versionLess(), artifact);
*/
        for (var dep : artifact.getDependencies()) {
            ArtifactCoordinate coordinate = dep.getCoordinate();
            System.err.println("  dependency: " + coordinate);

            if (artifacts.containsKey(dep.getCoordinate().versionLess())) {
                System.err.println("    skipping");
                continue;
            }

            if (!dep.shouldInclude(scope)) {
                return; // Skip dependencies not in the current scope
            }

            if (dep.getScope() == Scope.SYSTEM) {
                return;
            }

            String version = coordinate.getVersion();

            if (version.charAt(0) == '[' || version.charAt(0) == '(') {
                // Add artifact graph for each existingversion in the range
                try {
                    VersionRange range = VersionRange.createFromVersionSpec(version);
                    Metadata metaData = resolver.downloadArtifactMetadata(coordinate.getGroupId(), coordinate.getArtifactId(), version);
                    for (String existingVersion : metaData.getVersioning().getVersions()) {
                        if (range.containsVersion(new DefaultArtifactVersion(existingVersion))) {
                            ArtifactCoordinate versionedCoordinate = new ArtifactCoordinate(coordinate.getGroupId(), coordinate.getArtifactId(), existingVersion, coordinate.getClassifier());
                            if (artifacts.containsKey(versionedCoordinate.versionLess())) {
                                continue; // Avoid cycles
                            }
                            Artifact depArtifact = parser.parseArtifact(versionedCoordinate, dep.getExclusions(), resolver);
                            gatherArtifacts(depArtifact, parser, resolver);
                        }
                    }
                }
                catch (InvalidVersionSpecificationException e) {
                    throw new UnresolvableModelException(e, coordinate.getGroupId(), coordinate.getArtifactId(), version);
                }
            } else {
                Artifact depArtifact = parser.parseArtifact(coordinate, dep.getExclusions(), resolver);
                gatherArtifacts(depArtifact, parser, resolver);
            }
        }
    }

    public Map<WithoutVersion, Artifact> getArtifacts() {
        return artifacts;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DependencyResolver for scope: ").append(scope).append("\n");
        for (WithoutVersion key : artifacts.keySet()) {
            Artifact artifact = artifacts.get(key);
            sb.append("  ").append(artifact.getCoordinate()).append("\n");
            sb.append("    resolved: ").append(artifact.getResolved()).append("\n");
            sb.append("    dependencies:\n");
            for (Dependency dep : artifact.getDependencies()) {
                sb.append("      ").append(dep.getCoordinate()).append("\n");
            }
        }

        return sb.toString();
    }
}
