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
import static org.junit.Assume.assumeThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class MavenResolverTest extends AbstractMavenTest {
    @Test
    public void rascalPomHasRightDependencies() throws ModelResolutionError {
        var parser = createParser("rascal/pom.xml");
        var project = parser.parseProject();

        assertEquals("rascal", project.getCoordinate().getArtifactId());
        var resolved = project.resolveDependencies(Scope.COMPILE, parser);

        var maybeVallang = locate(resolved, "vallang");
        assertTrue("Rascal should have vallang", maybeVallang.isPresent());

        var vallang = maybeVallang.get();
        assertEquals("Vallang should be of right version", "1.0.0-RC15", vallang.getCoordinate().getVersion());
        assertNotNull("Vallang should be found/downloaded in the repo", vallang.getResolved());

        var capsuleDep = locateDependency(vallang.getDependencies(), "capsule");
        assertTrue("Capsule should be a dependency of vallang", capsuleDep.isPresent());
        assertEquals(191, capsuleDep.get().getLine());
        assertEquals(14, capsuleDep.get().getColumn());

        var maybeCapsule = locate(resolved, "capsule");
        assertTrue("Vallang should depend on capsule", maybeCapsule.isPresent());


        Path artifactPath = tempRepo.resolve(Path.of("io", "usethesource", "vallang", "1.0.0-RC15"));
        Path sha1Path = artifactPath.resolve("vallang-1.0.0-RC15.jar.sha1");
        assertTrue("Vallang sha1 should have been written", Files.exists(sha1Path));
        Path jarPath = artifactPath.resolve("vallang-1.0.0-RC15.jar");
        assertTrue("Vallang jar should have been written", Files.exists(jarPath));
    }

    @Test
    public void nestedDependenciesWithParentPomsShouldWork() throws ModelResolutionError {
        var parser = createParser("multi-module/example-core/pom.xml");
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

    private static Optional<Dependency> locateDependency(List<Dependency> dependencies, String artifactId) {
        return dependencies.stream()
            .filter(d -> d.getCoordinate().getArtifactId().equals(artifactId))
            .findFirst();
    }

    @Test
    public void localReferenceIsAvailableInModel() throws ModelResolutionError {
        var parser = createParser("local-reference/pom.xml");
        var project = parser.parseProject();
        var resolved = project.resolveDependencies(Scope.COMPILE, parser);
        var maybeTestLib = locate(resolved, "test-lib");
        assertTrue("non-existing test lib should be found", maybeTestLib.isPresent());
        var testLib = maybeTestLib.get();

        assertEquals("0.1.0-SNAPSHOT", testLib.getCoordinate().getVersion());
        assertNull("non-existing test lib should not be found", testLib.getResolved());
    }

    @Test
    public void multiModulePomsWorkWithSiblings() throws ModelResolutionError {
        var parser = createParser("multi-module/example-ide/pom.xml");
        var project = parser.parseProject();
        var resolved = project.resolveDependencies(Scope.COMPILE, parser);
        var maybeRascalLsp = locate(resolved, "rascal-lsp");

        assertEquals(project.getCoordinate().getGroupId(), "org.rascalmpl");
        assertEquals(project.getCoordinate().getVersion(), "1.0.0-SNAPSHOT");
       
        assertTrue("Rascal-lsp should be found", maybeRascalLsp.isPresent());
        assertEquals("rascal-lsp should be resolved to the right version", "2.21.2", maybeRascalLsp.get().getCoordinate().getVersion());

        var maybeCoreLink = locate(resolved, "example-core");

        assertTrue("example-core should be in the list", maybeCoreLink.isPresent());
        assertEquals(maybeCoreLink.get().getCoordinate().getGroupId(), "org.rascalmpl");
        assertEquals(maybeCoreLink.get().getCoordinate().getVersion(), "1.0.0-SNAPSHOT");
        assertNotNull("example-core should resolved to a path", maybeCoreLink.get().getResolved());
        assertTrue("example-core should not have messages", maybeCoreLink.get().getMessages().isEmpty());


        // we should also have gotten dependencies that example-core has
        var maybeJline3Reader = locate(resolved, "jline-reader");
        assertTrue("jline3 should be found as a dependency", maybeJline3Reader.isPresent());
        assertNotNull("jline3 should be resolved to a path", maybeJline3Reader.get().getResolved());
    }

    @Test
    public void testMirrorDownload() throws ModelResolutionError {
        // Set "user.home" system property so we can read the settings.xml that contains mirror settings
        String originalHome = System.getProperty("user.home");
        try {
            Path cwd = Path.of(System.getProperty("user.dir"));
            Path mirrorHome = cwd.resolve(Path.of("test", "org", "rascalmpl", "util", "maven", "mirrorhomedir"));
            System.setProperty("user.home", mirrorHome.toString());

            MavenParser parser = new MavenParser(MavenSettings.readSettings(), getPomsPath(
                "local-reference/pom-mirror.xml"), tempRepo);

            Artifact project = parser.parseProject();
            List<Artifact> resolved = project.resolveDependencies(Scope.COMPILE, parser);
            Assert.assertNotNull("Dependency hast not been resolved by mirror", resolved.get(0).getResolved());
        }
        finally {
            System.setProperty("user.home", originalHome);
        }
    }

    /**
    * This tests requires a proxy to be setup so it is disabled by default.
    * To run this test, setup a proxy server for instance using tinyprox: https://tinyproxy.github.io/
    * Configure the proxy address in proxyhomedir/.m2/settings.xml
    * This test should now run and download artifacts using the proxy server.
    */
    @Ignore
    @Test
    public void testProxyDownload() throws ModelResolutionError {
        // Set "user.home" system property so we can read the settings.xml that contains proxy settings
        String originalHome = System.getProperty("user.home");
        try {
            Path cwd = Path.of(System.getProperty("user.dir"));
            Path mirrorHome = cwd.resolve(Path.of("test", "org", "rascalmpl", "util", "maven", "proxyhomedir"));
            System.setProperty("user.home", mirrorHome.toString());

            var parser = new MavenParser(MavenSettings.readSettings(), getPomsPath("local-reference/pom-proxy.xml"), tempRepo);

            var project = parser.parseProject();

            assertEquals("test-project", project.getCoordinate().getArtifactId());
            List<Artifact> resolved = project.resolveDependencies(Scope.COMPILE, parser);
            Assert.assertNotNull("Dependency has not been resolved using proxy", resolved.get(0).getResolved());
            Path jarPath = tempRepo.resolve(Path.of("org", "rascalmpl", "rascal", "0.40.17", "rascal-0.40.17.jar"));
            assertTrue("Rascal jar should have been written", Files.exists(jarPath));
        }
        finally {
            System.setProperty("user.home", originalHome);
        }
    }

    @Test
    public void testVersionRange() throws ModelResolutionError {
        var parser = createParser("remote-reference/pom.xml");
        var project = parser.parseProject();
        var resolved = project.resolveDependencies(Scope.COMPILE, parser);
        var maybeGson = locate(resolved, "gson");
        assertTrue("gson should be found", maybeGson.isPresent());
        assertNotNull("gson should be resolved", maybeGson.get().getResolved());
    }

    @Test
    public void testMissingGroupId() throws ModelResolutionError {
        var parser = createParser("local-reference/pom-no-groupid.xml");
        var project = parser.parseProject();
        var msgs = project.getMessages();
        assertEquals(1, msgs.size());
        assertTrue(msgs.get(0).toString().contains("\\'groupId\\' is missing"));
    }

    @Test
    public void testMissingVersion() throws ModelResolutionError {
        var parser = createParser("local-reference/pom-no-version.xml");
        var project = parser.parseProject();
        var msgs = project.getMessages();
        assertEquals(1, msgs.size());
        assertTrue(msgs.get(0).toString().contains("\\'version\\' is missing"));
    }

    @Test
    public void checkRascalPaths() throws ModelResolutionError, IOException {
        var parser = createParser("rascal/pom.xml");
        var project = parser.parseProject();
        var resolved = project.resolveDependencies(Scope.COMPILE, parser);
        List<Path> paths = resolved.stream()
            .map(artifact -> artifact.getResolved())
            .map(path -> tempRepo.relativize(path))
            .sorted()
            .collect(Collectors.toList());

        List<Path> expected = Files.lines(getPomsPath("rascal/expected-path-list.txt"))
            .map(line -> Path.of(line))
            .sorted()
            .collect(Collectors.toList());

        Assert.assertEquals(expected, paths);

        printMessages(project, resolved);
    }
}
