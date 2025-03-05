/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
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
package org.rascalmpl.runtime.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.JavaCompilation;
import org.rascalmpl.library.util.PathConfig;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;


public class JavaBridge {
    private String javaCompilerPath;

    public JavaBridge(List<ClassLoader> classLoaders, PathConfig config) {
        // TODO: @jurgenvinju this used to say `config.getJavaCompilerPath` but that is not is not available anymore (removed in #1969)? 
        //StringBuilder sw = new StringBuilder();
        //for(IValue v : config.getJavaCompilerPath()) {
        //    if(sw.length() > 0) sw.append(":");
        //    sw.append(v.toString());
        //}
        //javaCompilerPath = sw.toString();
        try {
            javaCompilerPath = config.resolveCurrentRascalRuntimeJar().getPath(); // TODO: review if this is correct, as it's now not a path anymore
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (ToolProvider.getSystemJavaCompiler() == null) {
            throw new ImplementationError("Could not find an installed System Java Compiler, please provide a Java Runtime that includes the Java Development Tools (JDK 1.6 or higher).");
        }
    }

    public <T> Class<T> compileJava(ISourceLocation loc, String className, String source) {
        return compileJava(loc, className, getClass(), source);
    }

    public <T> Class<T> compileJava(ISourceLocation loc, String className, Class<?> parent, String source) {
        try {
            // watch out, if you start sharing this compiler, classes will not be able to reload
            List<String> commandline = Arrays.asList(new String[] {"-proc:none", "-cp", javaCompilerPath});
            JavaCompiler<T> javaCompiler = new JavaCompiler<T>(parent.getClassLoader(), null, commandline);
            Class<T> result = javaCompiler.compile(className, source, null, Object.class);
            return result;
        } 
        catch (ClassCastException e) {
            throw new JavaCompilation(e.getMessage(), 0, 0, className, className, e);
        } 
        catch (JavaCompilerException e) {
            if (!e.getDiagnostics().getDiagnostics().isEmpty()) {
                Diagnostic<? extends JavaFileObject> msg = e.getDiagnostics().getDiagnostics().iterator().next();
                throw new JavaCompilation(msg.getMessage(null), msg.getLineNumber(), msg.getColumnNumber(), className, javaCompilerPath, e);
            }
            else {
                throw new JavaCompilation(e.getMessage(), 0, 0,  className, javaCompilerPath, e);
            }
        }
    }
}
