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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rascalmpl.util.maven.Dependency;
import org.rascalmpl.util.maven.Project;
import org.rascalmpl.util.maven.Scope;

public class MavenResolverTest {

    private static Path tempRepo;

    @BeforeClass
    public static void setupRepo() throws IOException {
        tempRepo = Files.createTempDirectory("m2-test-repo");
    }

    @AfterClass
    public static void cleanupRepo() throws IOException {
         try (Stream<Path> pathStream = Files.walk(tempRepo)) {
            pathStream.sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
        }
    }

    private static Path getPomsPath(String subPath) {
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

    private Project parse(String path) {
        return Project.parseProjectPom(getPomsPath(path), tempRepo);
    }

    @Test
    public void rascalPomHasRightDependencies() {
        var project = parse("rascal/pom.xml");
        assertEquals("rascal", project.getCoordinate().getArtifactId());

        var maybeVallang = project.getDependencies().stream()
            .filter(d -> d.getCoordinate().getArtifactId().equals("vallang"))
            .findFirst();
        assertTrue("Rascal should have vallang", maybeVallang.isPresent());

        var vallang = maybeVallang.get();
        assertEquals("Vallang should be of right version", "1.0.0-RC15", vallang.getCoordinate().getVersion());
        assertTrue("Vallang should be found/downloaded in the repo", vallang.isFound());
        assertTrue("Vallang should depend on capsule", vallang.getDependencies().stream().anyMatch(d -> d.getCoordinate().getArtifactId().equals("capsule")));
    }

    @Test
    public void nestedDependenciesWithParentPomsShouldWork() {
        var project = parse("multi-module/example-core/pom.xml");
        var maybeJline3Reader = project.getDependencies().stream()
            .filter(d -> d.getCoordinate().getArtifactId().equals("jline-reader"))
            .findFirst();
        assertTrue("jline3 should be found as a dependency", maybeJline3Reader.isPresent());

        for (var dep: maybeJline3Reader.get().getDependencies()) {
            assertNotNull("versions should be found for jline-reader dependency: " + dep.getCoordinate().getArtifactId(), dep.getCoordinate().getVersion());
            assertTrue("we should have been able to download dependency: " + dep.getCoordinate().getArtifactId(), dep.isFound());
        }
    }

    @Test
    public void localReferenceIsAvailableInModel() {
        var project = parse("local-reference/pom.xml");
        var maybeTestLib = project.getDependencies().stream()
            .filter(d ->  d.getCoordinate().getArtifactId().equals("test-lib"))
            .findFirst();
        assertTrue("non-existing test lib should be found", maybeTestLib.isPresent());
        var testLib = maybeTestLib.get();

        assertEquals("0.1.0-SNAPSHOT", testLib.getCoordinate().getVersion());
        assertEquals(Scope.COMPILE, testLib.getScope());
        assertFalse("non-existing test lib should not be found", testLib.isFound());
    }

    @Test
    public void multiModulePomsWork() {
        var project = parse("multi-module/example-ide/pom.xml");

        assertTrue("rascal-lsp should be of the right version", 
            project.getDependencies().stream()
                .map(Dependency::getCoordinate)
                .filter(c -> c.getArtifactId().equals("rascal-lsp"))
                .anyMatch(c -> c.getVersion().equals("2.21.2")));

        assertTrue("We should have a link to our core project", 
            project.getDependencies().stream()
                .map(Dependency::getCoordinate)
                .anyMatch(c -> c.getArtifactId().equals("example-core")));
    }


}
