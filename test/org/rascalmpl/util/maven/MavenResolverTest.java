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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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

    private MavenParser parse(String path) {
        return new MavenParser(getPomsPath(path), tempRepo);
    }

    @Test
    public void rascalPomHasRightDependencies() throws ModelResolutionError {
        var parser = parse("rascal/pom.xml");
        var project = parser.parseProject();

        assertEquals("rascal", project.getCoordinate().getArtifactId());
        var resolved = project.resolveDependencies(Scope.COMPILE, parser);

        var maybeVallang = locate(resolved, "vallang");
        assertTrue("Rascal should have vallang", maybeVallang.isPresent());

        var vallang = maybeVallang.get();
        assertEquals("Vallang should be of right version", "1.0.0-RC15", vallang.getCoordinate().getVersion());
        assertNotNull("Vallang should be found/downloaded in the repo", vallang.getResolved());

        var maybeCapsule = locate(resolved, "capsule");
        assertTrue("Vallang should depend on capsule", maybeCapsule.isPresent());
    }

    @Test
    public void nestedDependenciesWithParentPomsShouldWork() throws ModelResolutionError {
        var parser = parse("multi-module/example-core/pom.xml");
        var project = parser.parseProject();
        var resolved = project.resolveDependencies(Scope.COMPILE, parser);
        var maybeJline3Reader = locate(resolved, "jline-reader");
        assertTrue("jline3 should be found as a dependency", maybeJline3Reader.isPresent());
        assertNotNull("jline3 should be resolved to a path", maybeJline3Reader.get().getResolved());

        var maybeJline3Terminal = locate(resolved, "jline-terminal");
        assertTrue("jline3 dependencies should be in the resolved list", maybeJline3Terminal.isPresent());
        assertNotNull("jline3 dependencies should be resolved to a path", maybeJline3Terminal.get().getResolved());
    }

    private static Optional<Artifact> locate(List<Artifact> resolved, String artifactId) {
        return resolved.stream()
            .filter(d -> d.getCoordinate().getArtifactId().equals(artifactId))
            .findFirst();
    }

    @Test
    public void localReferenceIsAvailableInModel() throws ModelResolutionError {
        var parser = parse("local-reference/pom.xml");
        var project = parser.parseProject();
        var resolved = project.resolveDependencies(Scope.COMPILE, parser);
        var maybeTestLib = locate(resolved, "test-lib");
        assertTrue("non-existing test lib should be found", maybeTestLib.isPresent());
        var testLib = maybeTestLib.get();

        assertEquals("0.1.0-SNAPSHOT", testLib.getCoordinate().getVersion());
        assertNull("non-existing test lib should not be found", testLib.getResolved());
    }

    @Test
    public void multiModulePomsWork() throws ModelResolutionError {
        var parser = parse("multi-module/example-ide/pom.xml");
        var project = parser.parseProject();
        var resolved = project.resolveDependencies(Scope.COMPILE, parser);
        var maybeRascalLsp = locate(resolved, "rascal-lsp");

        assertTrue("Rascal-lsp should be found", maybeRascalLsp.isPresent());
        assertEquals("rascal-lsp should be resolved to the right version", "2.21.2", maybeRascalLsp.get().getCoordinate().getVersion());

        var maybeCoreLink = locate(resolved, "example-core");

        assertTrue("example-core should be in the list", maybeCoreLink.isPresent());
        assertNull("example-core should not be resolved to a path", maybeCoreLink.get().getResolved());
    }


}
