package org.rascalmpl.util.maven;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.rascalmpl.library.Messages;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class SystemScopeTest {

    @Test
    public void testSystemScope() throws ModelResolutionError, MalformedURLException, IOException {
        var parser = new MavenParser(Path.of("test/org/rascalmpl/util/maven/poms/local-reference/pom-system.xml"));
        var project = parser.parseProject();
        List<Artifact> deps = project.resolveDependencies(Scope.RUNTIME, parser);
        Assert.assertEquals(1, deps.size());
        Artifact dep = deps.get(0);
        Assert.assertEquals("42", dep.getCoordinate().getVersion());

        // Verify the artifact points to the right jar file
        URL url = new URL("jar:" + dep.getResolved().toUri().toString() + "!/lorem-ipsum.txt");        
        String content = IOUtils.toString(url);
        Assert.assertTrue(content.startsWith("Dream about hunting birds"));
    }

    @Test
    public void testSystemScopeNoSystemPath() throws ModelResolutionError, MalformedURLException, IOException {
        var parser = new MavenParser(Path.of("test/org/rascalmpl/util/maven/poms/local-reference/pom-system-no-path.xml"));
        var project = parser.parseProject();
        List<Artifact> deps = project.resolveDependencies(Scope.RUNTIME, parser);
        Assert.assertEquals(0, deps.size());

        Assert.assertEquals(1, project.getMessages().length());
        Assert.assertTrue(Messages.isError(project.getMessages().get(0)));
    }

}
