package org.rascalmpl.util.maven;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.apache.maven.settings.Mirror;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class LocalRepoTest extends AbstractMavenTest {
    private String testRepo;
    private MavenSettings settings;

    @Before
    public void replaceCentralWithLocalRepo() throws InvalidRepositoryException {
        URL url = AbstractMavenTest.class.getResource("/org/rascalmpl/util/maven/m2/repository");
        testRepo = url.toString();

        // We can use a mirror in settings to override the central repository
        settings = new MavenSettings() {
            @Override
            public Map<String, Mirror> getMirrors() {
                Mirror mirror = new Mirror();
                mirror.setId("local");
                mirror.setName("Maven Test Repository");
                mirror.setUrl(testRepo);
                mirror.setMirrorOf("central");

                return Map.of("central", mirror);
            }
        };

    }

    @After
    public void clearRepoProperty() {
        System.clearProperty("REPO");
    }

    @Test
    public void testRangedDependencies() throws ModelResolutionError, UnresolvableModelException {
        var parser = new MavenParser(settings, getPomsPath("range/pom.xml"), tempRepo);
        Artifact project = parser.parseProject();

        List<Artifact> resolvedDependencies = project.resolveDependencies(Scope.COMPILE, parser);
        List<ArtifactCoordinate> coordinates = resolvedDependencies.stream()
            .map(Artifact::getCoordinate)
            .collect(Collectors.toList());

        Assert.assertTrue(coordinates.contains(new ArtifactCoordinate("range", "level2", "2.0", null)));

        // Check that the warning about multiple version ranges is present
        for (Artifact artifact : resolvedDependencies) {
            if (artifact.getCoordinate().getArtifactId().equals("level1b")) {
                // The full message would be something like:
                // Multiple version ranges found for range:level2, 1.5 is used. Maybe you should fix the desired version in your top-level pom using a fixed version spec like [1.5]
                Assert.assertTrue(artifact.getMessages().get(0).toString().contains("Multiple version ranges found"));
            }
        }
    }

    @Test
    public void testSimpleResolverParentResolution() throws ModelResolutionError, UnresolvableModelException, IOException {
        // We still want access to maven central, so use string replacement to set the REPO
        String content = Files.readString(getPomsPath("parent/pom.xml"));
        content = content.replace("${REPO}", testRepo);
        // We need some place to store the modified pom
        Path pomPath = tempRepo.resolve("parent-pom.xml");
        Files.writeString(pomPath, content);

        var parser = new MavenParser(new MavenSettings(), pomPath, tempRepo);
        Artifact project = parser.parseProject();
        List<Artifact> resolvedDependencies = project.resolveDependencies(Scope.COMPILE, parser);

        // just parent:rascal:1.0 and org.rascalmpl:rascal:1.0
        Assert.assertEquals(resolvedDependencies.size(), 2);

        for (Artifact artifact : resolvedDependencies) {
            System.err.println("messages for " + artifact.getCoordinate() + ": " + artifact.getMessages());
            Assert.assertTrue(artifact.getMessages().isEmpty());
        }
    }

    /**
     * This test checks if transitive exclusions work correctly:
     * - pom-transitive-exclusions.xml has trans:level1:1.0 as a dependency with trans:level3 as an exclusion
     * - trans:level1:1.0 has trans:level2:1.0 as dependency
     * - trans:level2:1.0 has trans:level3:1.0 as dependency
     * - trans:level3:1.0 should not be included in the resolved dependencies
     */
    @Test
    public void testTransitiveExclusions() throws ModelResolutionError, UnresolvableModelException, IOException {
        var parser = new MavenParser(settings, getPomsPath("local-reference/pom-transitive-exclusions.xml"), tempRepo);
        Artifact project = parser.parseProject();
        List<Artifact> resolvedDependencies = project.resolveDependencies(Scope.COMPILE, parser);
        Assert.assertEquals(2, resolvedDependencies.size());
        Assert.assertTrue(resolvedDependencies.stream().allMatch(artifact -> !artifact.getCoordinate().getArtifactId().equals("level3")));
    }

    /**
     * This test checks if caching takes transitive exclusions into account.
     * - pom-transitive-exclusions.xml has:
     *      - trans:level1:1.0 as a dependency with trans:level3 as an exclusion
     *      - trans:level2:1.0 as a dependency without exclusions
     * - trans:level1:1.0 has trans:level2:1.0 as dependency
     * - trans:level2:1.0 has trans:level3:1.0 as dependency
     * - trans:level3:1.0 should be included in the resolved dependencies because there is a path where it is not excluded.
     */
    @Test
    public void testExclusionBasedCaching() throws ModelResolutionError, UnresolvableModelException, IOException {
        var parser = new MavenParser(settings, getPomsPath("local-reference/pom-exclusion-caching.xml"), tempRepo);
        Artifact project = parser.parseProject();
        List<Artifact> resolvedDependencies = project.resolveDependencies(Scope.COMPILE, parser);
        for (Artifact artifact : resolvedDependencies) {
            System.out.println("messages for " + artifact.getCoordinate() + ": " + artifact.getMessages());
        }
        Assert.assertEquals(3, resolvedDependencies.size());
        Assert.assertTrue(resolvedDependencies.stream().anyMatch(artifact -> artifact.getCoordinate().getArtifactId().equals("level3")));
    }

    /**
     * This test checks if version numbers are resolved "breadth-first".
     * - pom-breadth-first.xml has:
     *     - breadth-first:level1a:1.0 as a dependency
     *     - breadth-first:level1b:1.0 as a dependency
     * - breadth-first:level1a:1.0 has breadh-first:level2:1.0 as a dependency
     * - breadth-first:level2:1.0 has breadh-first:level3:1.0 as a dependency
     * - breadth-first:level1b:1.0 has breadh-first:level3:2.0 as a dependency
     * 
     * When resolving breadth-first, breadth-first:level3 should be resolved to version 2.0.
     */
    @Test
    public void testBreadthFirstResolving() throws ModelResolutionError, UnresolvableModelException, IOException {
        var parser = new MavenParser(settings, getPomsPath("local-reference/pom-breadth-first.xml"), tempRepo);
        Artifact project = parser.parseProject();
        List<Artifact> resolvedDependencies = project.resolveDependencies(Scope.COMPILE, parser);
        var level3 = resolvedDependencies.get(3);
        Assert.assertEquals("2.0", level3.getCoordinate().getVersion());
    }

    //@Ignore
    @Test
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
