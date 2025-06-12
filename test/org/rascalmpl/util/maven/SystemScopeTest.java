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
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPEC
            AL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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

public class SystemScopeTest {

    @Test
    public void testSystemScope() throws ModelResolutionError, MalformedURLException, IOException {
        var parser = new MavenParser(new MavenSettings(), Path.of("test/org/rascalmpl/util/maven/poms/local-reference/pom-system.xml"));
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
        verifyIllegalSystemPath("pom-system-no-path.xml");
    }

    @Test
    public void testSystemScopeNoFile() throws ModelResolutionError, MalformedURLException, IOException {
        verifyIllegalSystemPath("pom-system-no-file.xml");
    }

    private void verifyIllegalSystemPath(String pomFile) throws ModelResolutionError, MalformedURLException, IOException{
        var parser = new MavenParser(new MavenSettings(), Path.of("test/org/rascalmpl/util/maven/poms/local-reference/" + pomFile));
        var project = parser.parseProject();
        List<Artifact> deps = project.resolveDependencies(Scope.RUNTIME, parser);
        Assert.assertEquals("Dependency should not be dropped", 1, deps.size());

        // Expect a single error at the dependency level
        Artifact dep = deps.get(0);
        Assert.assertEquals(1, dep.getMessages().length());
        Assert.assertTrue(Messages.isError(dep.getMessages().get(0)));

        // Dependency should not be resolved
        Assert.assertNull("System dependency with invalid systemPath should not be resolved", dep.getResolved());
    }

}
