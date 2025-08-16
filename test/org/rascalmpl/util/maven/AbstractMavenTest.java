package org.rascalmpl.util.maven;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.junit.AfterClass;
import org.junit.BeforeClass;

abstract class AbstractMavenTest {
    protected static Path tempRepo;

    @BeforeClass
    public static void setupRepo() throws IOException, InvalidRepositoryException {
        tempRepo = Files.createTempDirectory("m2-test-repo");
    }

    @AfterClass
    public static void cleanupRepo() throws IOException {
        if (tempRepo != null) {
            try (Stream<Path> pathStream = Files.walk(tempRepo)) {
                pathStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        }
    }

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

}
