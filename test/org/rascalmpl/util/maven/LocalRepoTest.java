package org.rascalmpl.util.maven;

import java.net.URL;
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
        URL url = AbstractMavenTest.class.getResource("/org/rascalmpl/util/maven/m2/repository");
        System.err.println("Adding repository: " + url);

        // Pass the repository URL through a system property so artifact resolvers can find it
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

}
