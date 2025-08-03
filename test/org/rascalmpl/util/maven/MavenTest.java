package org.rascalmpl.util.maven;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

import org.apache.maven.model.Repository;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class MavenTest {
    protected static Path tempRepo;
    //protected static SimpleResolver resolver;

    @BeforeClass
    public static void setupRepo() throws IOException, InvalidRepositoryException {
        tempRepo = Files.createTempDirectory("m2-test-repo");

        /*
        var httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_2) // upgrade where possible
            .connectTimeout(Duration.ofSeconds(10)) // don't wait longer than 10s to connect to a repo
            .build();
        var modelBuilder = new DefaultModelBuilderFactory().newInstance();
        resolver = new SimpleResolver(tempRepo, modelBuilder, httpClient, Collections.emptyMap());
         */
    }

    @AfterClass
    public static void cleanupRepo() throws IOException {
        if (tempRepo != null) {
            try (Stream<Path> pathStream = Files.walk(tempRepo)) {
                pathStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        }
    }

    /*private static void addRepo(String id, String url) throws InvalidRepositoryException {
        Repository repo = new Repository();
        repo.setId(id);
        repo.setUrl(url);
        resolver.addRepository(repo);
    }
        */

    protected static Path getPomsPath(String subPath) {
        var path = MavenResolverTest.class.getResource("/org/rascalmpl/util/maven/poms/" + subPath);
        if (path == null) {
            throw new IllegalStateException("Could not find: " + subPath);
        }
        try {
            return Path.of(path.toURI());
        }
        catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    protected MavenParser createParser(String path) {
        return new MavenParser(new MavenSettings(), getPomsPath(path), tempRepo);
    }

    /*
    @Test
    public void testDependencyGraphBuilding() throws ModelResolutionError, UnresolvableModelException {
        var parser = new MavenParser(new MavenSettings(), getPomsPath("range/pom.xml"), tempRepo);
        DependencyResolver graph = new DependencyResolver(Scope.COMPILE, parser.parseProject());
        graph.gatherArtifacts(parser.parseProject(), parser, resolver);
        System.err.println("========================\nGathered artifacts:\n" + graph);
    }
    */
}
