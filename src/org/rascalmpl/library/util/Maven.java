/** 
 * Copyright (c) 2024, Rodin Aarssen, Swat.engineering 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.rascalmpl.library.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.apache.maven.cli.CliRequest;
import org.apache.maven.cli.MavenCli;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;

public class Maven {
    /**
     * Calls Maven with the provided arguments. The working directory will be set to `manifestRoot`,
     * which should contain a `pom.xml` file. If `outputFile` refers to an existing file, its contents
     * will the read and returned after Maven concludes.
     * 
     * Maven's output is fully suppressed. However, it is often possible to redirect (parts of) the output
     * to a file. For instance, the output of `mvn dependency:build-classpath` can be redicted to a file
     * by providing an additional argument `-Dmdep.outputFile=/path/to/file`.
     */
    public static List<String> runCommand(List<String> args, @Nullable ISourceLocation manifestRoot, Path outputFile) {
        try {
            ISourceLocation pomxml = manifestRoot != null ? URIUtil.getChildLocation(manifestRoot, "pom.xml") : null;
            pomxml = pomxml != null ? URIResolverRegistry.getInstance().logicalToPhysical(pomxml) : null;
            manifestRoot = manifestRoot != null ? URIResolverRegistry.getInstance().logicalToPhysical(manifestRoot) : null;
            
            if (manifestRoot != null && !"file".equals(manifestRoot.getScheme())) {
                throw new IllegalArgumentException("`manifestRoot` could not be resolved");
            }

            if (pomxml != null && !URIResolverRegistry.getInstance().exists(pomxml)) {
                throw new IllegalArgumentException("`manifestRoot` does not contain pom.xml");
            }

            var maven = new MavenCli();
            maven.doMain(buildRequest(args.toArray(String[]::new), manifestRoot));

            if (outputFile != null && Files.exists(outputFile)) {
                return Files.readAllLines(outputFile);
            }
        } catch (IOException | ReflectiveOperationException e) {
            // Fall through to return the empty list
        }

        return Collections.emptyList();
    }

    /**
     * Calls Maven with the provided arguments. The working directory will be set to `manifestRoot`,
     * which should contain a `pom.xml` file. A temporary file is created, applied to the first argument for
     * consumption, and its contents will be read and returned after Maven concludes.
     * 
     * Maven's output is fully suppressed. However, it is often possible to redirect (parts of) the output
     * to a file. For instance, the output of `mvn dependency:build-classpath` can be redicted to a file
     * by providing an additional argument `-Dmdep.outputFile=/path/to/file`.
     */
    public static List<String> runCommand(Function<Path, List<String>> args, @Nullable ISourceLocation manifestRoot) {
        try {
            var tempFile = Maven.getTempFile();
            return runCommand(args.apply(tempFile), manifestRoot, tempFile);
        }
        catch (IOException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Calls Maven with the provided arguments. The working directory will be set to `manifestRoot`,
     * which should contain a `pom.xml` file. Maven's output is fully suppressed.
     */
    public static void runCommand(List<String> args, ISourceLocation manifestRoot) {
        runCommand(args, manifestRoot, null);
    }
    
    private static void setField(CliRequest req, String fieldName, Object value) throws ReflectiveOperationException {
        var field = CliRequest.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(req, value);
    }
    
    private static CliRequest buildRequest(String[] args, @Nullable ISourceLocation manifestRoot) throws ReflectiveOperationException {
        // we need to set a field that the default class doesn't set
        // it's a work around around a bug in the MavenCli code
        var cons = CliRequest.class.getDeclaredConstructor(String[].class, ClassWorld.class);
        cons.setAccessible(true);
        var result = cons.newInstance(args, null);

        if (manifestRoot != null) {
            var manifestRootFile = new File(manifestRoot.getPath());
            setField(result, "workingDirectory", manifestRootFile.getPath());
            setField(result, "multiModuleProjectDirectory", manifestRootFile);
        }
        else {
            try {
                setField(result, "multiModuleProjectDirectory", File.createTempFile("dummy", ""));
            }
            catch (ReflectiveOperationException | IOException e) {
                // ignore for robustness sake, since we don't have a manifestRoot
            }
        }

        return result;
    }

    public static Path getTempFile() throws IOException {
        return Files.createTempFile("rascal-maven-", ".tmp");
    }
}
