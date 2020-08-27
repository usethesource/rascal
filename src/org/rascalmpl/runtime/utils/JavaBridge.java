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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.$RascalModule;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.RascalRuntimeValueFactory;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.JavaCompilation;
import org.rascalmpl.exceptions.JavaMethodLink;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.util.ListClassLoader;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;


public class JavaBridge {
    private final List<ClassLoader> loaders;

    private final IValueFactory vf;

    private final Map<Class<?>, Object> instanceCache;


    private String javaCompilerPath;

    public JavaBridge(List<ClassLoader> classLoaders, IValueFactory valueFactory, PathConfig config) {
        this.loaders = classLoaders;
        this.vf = valueFactory;
        this.instanceCache = new HashMap<Class<?>, Object>();

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
            throw new JavaCompilation(e.getMessage(), e);
        } 
        catch (JavaCompilerException e) {
            if (!e.getDiagnostics().getDiagnostics().isEmpty()) {
                Diagnostic<? extends JavaFileObject> msg = e.getDiagnostics().getDiagnostics().iterator().next();
                throw new JavaCompilation(msg.getMessage(null) + " at " + msg.getLineNumber() + ", " + msg.getColumnNumber() + " with classpath [" + javaCompilerPath + "]", e);
            }
            else {
                throw new JavaCompilation(e.getMessage(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getJavaClassInstance(String className, IRascalMonitor monitor, TypeStore store, PrintWriter out, PrintWriter err, OutputStream rawOut, OutputStream rawErr, InputStream in, $RascalModule module){
        PrintWriter[] outputs = new PrintWriter[] { out, err };
        int writers = 0;

        OutputStream[] rawOutputs = new OutputStream[] { rawOut, rawErr };
        int rawWriters = 0;

        try {
            for(ClassLoader loader : loaders){
                try{
                    Class<?> clazz = loader.loadClass(className);

                    Object instance = instanceCache.get(clazz);
                    if(instance != null){
                        return (T) instance;
                    }

                    if (clazz.getConstructors().length > 1) {
                        throw new IllegalArgumentException("Rascal JavaBridge can only deal with one constructor. This class has multiple: " + clazz);
                    }

                    Constructor<?> constructor = clazz.getConstructors()[0];

                    Object[] args = new Object[constructor.getParameterCount()];
                    Class<?>[] formals = constructor.getParameterTypes();

                    for (int i = 0; i < constructor.getParameterCount(); i++) {
                        if (formals[i].isAssignableFrom(IValueFactory.class)) {
                            args[i] = vf;
                        }
                        else if (formals[i].isAssignableFrom(TypeStore.class)) {
                            args[i] = store;
                        }
                        else if (formals[i].isAssignableFrom(TypeFactory.class)) {
                            args[i] = TypeFactory.getInstance();
                        }
                        else if (formals[i].isAssignableFrom(PrintWriter.class)) {
                            args[i] = outputs[writers++ % 2];
                        }
                        else if (formals[i].isAssignableFrom(OutputStream.class)) {
                            args[i] = rawOutputs[rawWriters++ %2];
                        }
                        else if (formals[i].isAssignableFrom(InputStream.class)) {
                            args[i] = in;
                        }
                        else if (formals[i].isAssignableFrom(IRascalMonitor.class)) {
                            args[i] = monitor;
                        }
                        else if (formals[i].isAssignableFrom(ClassLoader.class)) {
                            args[i] = new ListClassLoader(loaders, getClass().getClassLoader()); 
                        }
                        else if (formals[i].isAssignableFrom(IRascalValueFactory.class)) {
                            args[i] = new RascalRuntimeValueFactory(module);
                        }
                        else if (formals[i].isAssignableFrom($RascalModule.class)) {
                            args[i] = module;
                        }
                        else {
                            throw new IllegalArgumentException(constructor + " has unknown arguments. Only IValueFactory, TypeStore and TypeFactory are supported");
                        }
                    }

                    instance = constructor.newInstance(args);
                    instanceCache.put(clazz, instance);
                    return (T) instance;
                }
                catch(ClassNotFoundException e){
                    continue;
                } 
            }
        } 
        catch (NoClassDefFoundError | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException e) {
            throw new JavaMethodLink(className, e.getMessage(), e);
        }

        throw new JavaMethodLink(className, "class not found", null);	
    }
}
