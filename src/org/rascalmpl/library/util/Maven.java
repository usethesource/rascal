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

import org.apache.maven.cli.CliRequest;
import org.apache.maven.cli.MavenCli;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;

public class Maven {
    /**
     * Calls maven with the provided arguments. The working directory will be set to `manifestRoot`,
     * which should contain a `pom.xml` file. If `outputFile` refers to an existing file, its contents
     * will the read and returned after Maven concludes.
     * 
     * Maven's output is fully suppressed. However, it is often possible to redirect (parts of) the output
     * to a file. For instance, the output of `mvn dependency:build-classpath` can be redicted to a file
     * by providing an additional argument `-Dmdep.outputFile=/path/to/file`.
     */
    public static List<String> runCommand(List<String> args, ISourceLocation manifestRoot, Path outputFile) {
        try {
            ISourceLocation pomxml = URIUtil.getChildLocation(manifestRoot, "pom.xml");
            pomxml = URIResolverRegistry.getInstance().logicalToPhysical(pomxml);
            manifestRoot = URIResolverRegistry.getInstance().logicalToPhysical(manifestRoot);
            
            if (!"file".equals(manifestRoot.getScheme())) {
                throw new IllegalArgumentException("`manifestRoot` could not be resolved");
            }

            if (!URIResolverRegistry.getInstance().exists(pomxml)) {
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
     * Calls maven with the provided arguments. The working directory will be set to `manifestRoot`,
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
    
    private static CliRequest buildRequest(String[] args, ISourceLocation manifestRoot) throws ReflectiveOperationException {
        // we need to set a field that the default class doesn't set
        // it's a work around around a bug in the MavenCli code
        var cons = CliRequest.class.getDeclaredConstructor(String[].class, ClassWorld.class);
        cons.setAccessible(true);
        var result = cons.newInstance(args, null);
        var manifestRootFile = new File(manifestRoot.getPath());
        setField(result, "workingDirectory", manifestRootFile.getPath());
        setField(result, "multiModuleProjectDirectory", manifestRootFile);
        return result;
    }

    public static Path getTempFile(String kind) throws IOException {
        return Files.createTempFile("rascal-maven-" + kind + "-", ".tmp");
    }
}
