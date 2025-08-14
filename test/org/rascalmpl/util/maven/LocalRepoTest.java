package org.rascalmpl.util.maven;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocalRepoTest extends AbstractMavenTest {
    @Before
    public void replaceCentralWithLocalRepo() throws InvalidRepositoryException {
        // Pass the "remote" test repository URL through a system property so artifact resolvers can find it
        URL url = AbstractMavenTest.class.getResource("/org/rascalmpl/util/maven/m2/repository");
        System.setProperty("REPO", url.toString());
    }

    @After
    public void clearRepoProperty() {
        System.clearProperty("REPO");
    }

    @Test
    public void testRangedDependencies() throws ModelResolutionError, UnresolvableModelException {
        var parser = new MavenParser(new MavenSettings(), getPomsPath("range/pom.xml"), tempRepo);
        Artifact project = parser.parseProject();

        List<Artifact> resolvedDependencies = project.resolveDependencies(Scope.COMPILE, parser);
        List<ArtifactCoordinate> coordinates = resolvedDependencies.stream()
            .map(Artifact::getCoordinate)
            .collect(Collectors.toList());

        Assert.assertTrue(coordinates.contains(new ArtifactCoordinate("range", "level2", "2.0", null)));
    }

    //@Test
    /**
     * This is a stress test that checks all POMs in your local maven repository.
     */
    public void testAllPomsInM2Repo() throws ModelResolutionError, UnresolvableModelException, IOException {
        String home = System.getProperty("user.home");
        Path repo = Paths.get(home, ".m2", "repository");
        System.err.println("Checking all POMs in " + repo.toAbsolutePath());
        Files.walk(repo)
            .filter(Files::isRegularFile)
            .filter(path -> path.toString().endsWith(".pom"))
            .filter(path -> !path.toString().contains("SNAPSHOT"))
            .forEach(pomPath -> {
                try {
                    var parser = new MavenParser(new MavenSettings(), pomPath, tempRepo);
                    Artifact project = parser.parseProject();
                    List<Artifact> resolvedDependencies = project.resolveDependencies(Scope.COMPILE, parser);
                    List<ArtifactCoordinate> coordinates =
                        resolvedDependencies.stream().map(Artifact::getCoordinate).collect(Collectors.toList());
                    String relPath = repo.relativize(pomPath).toString();
                    Collections.sort(coordinates, (left,right) -> left.toString().compareTo(right.toString()));
                    System.out.println(relPath + ": " + coordinates);
                } catch (Exception e) {
                    System.err.println("Failed to parse " + pomPath + ": " + e.getMessage());
                    e.printStackTrace();
                }
            });
    }

}
