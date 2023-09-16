/*******************************************************************************
 * Copyright (c) 2009-2017 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils;

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
        StringBuilder sw = new StringBuilder();
        for(IValue v : config.getJavaCompilerPath()) {
            if(sw.length() > 0) sw.append(":");
            sw.append(v.toString());
        }
        javaCompilerPath = sw.toString();

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
