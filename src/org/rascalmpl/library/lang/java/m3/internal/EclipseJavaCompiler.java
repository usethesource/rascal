/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bas Basten - Bas.Basten@cwi.nl (CWI)
 *   * Jouke Stoel - Jouke.Stoel@cwi.nl (CWI)
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.library.lang.java.m3.internal;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalRuntimeException;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.TypeStore;

@SuppressWarnings("rawtypes")
public class EclipseJavaCompiler {
    protected final IValueFactory VF;

    public EclipseJavaCompiler(IValueFactory vf) {
        this.VF = vf;
    }

    public IValue createM3FromJarClass(ISourceLocation jarLoc, IEvaluatorContext eval) {
        TypeStore store = new TypeStore();
        store.extendStore(eval.getHeap().getModule("lang::java::m3::Core").getStore());
        store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());
        JarConverter converter = new JarConverter(store, new HashMap<>());
        converter.set(jarLoc);
        converter.convert(jarLoc, eval);

        return converter.getModel(false);
    }

    public IValue createM3sFromFiles(ISet files, ISet sourcePath, ISet classPath, IString javaVersion, IEvaluatorContext eval) {
        try {
            Map<String, ISourceLocation> cache = new HashMap<>();
            ISetWriter result = VF.setWriter();
            for (IValue file: files) {
                ISourceLocation loc = (ISourceLocation) file;
                CompilationUnit cu = this.getCompilationUnit(loc.getPath(), getFileContents(loc, eval), true, javaVersion, translatePaths(sourcePath), translatePaths(classPath));

                TypeStore store = new TypeStore();
                store.extendStore(eval.getHeap().getModule("lang::java::m3::Core").getStore());
                store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());
                SourceConverter converter = new SourceConverter(store, cache);

                converter.set(cu);
                converter.set(loc);
                cu.accept(converter);
                for (Iterator it = cu.getCommentList().iterator(); it.hasNext();) {
                    Comment comment = (Comment) it.next();
                    // Issue 720: changed condition to only visit comments without a parent (includes line, block and misplaced javadoc comments).
                    if (comment.getParent() != null)
                        continue;
                    comment.accept(converter);
                }

                result.insert(converter.getModel(true));
            }
            return result.done();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }

    private String[] translatePaths(ISet paths) {
        String[] result = new String[paths.size()];
        int i = 0;
        for (IValue p : paths) {
            ISourceLocation loc = (ISourceLocation)p;
            if (!loc.getScheme().equals("file")) {
                throw RascalRuntimeException.io(VF.string("all classpath entries must have the file:/// scheme: " + loc), null);
            }
            result[i++] = loc.getPath();
        }
        return result;
    }

    public IValue createM3FromString(ISourceLocation loc, IString contents, ISet sourcePath, ISet classPath, IString javaVersion, IEvaluatorContext eval) {
        try {
            CompilationUnit cu = getCompilationUnit(loc.getPath(), contents.getValue().toCharArray(), true, javaVersion, translatePaths(sourcePath), translatePaths(classPath));

            TypeStore store = new TypeStore();
            store.extendStore(eval.getHeap().getModule("lang::java::m3::Core").getStore());
            store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());
            SourceConverter converter = new SourceConverter(store, new HashMap<>());

            converter.set(cu);
            converter.set(loc);
            cu.accept(converter);
            for (Iterator it = cu.getCommentList().iterator(); it.hasNext();) {
                Comment comment = (Comment) it.next();
                if (comment.getParent() != null)
                    continue;
                comment.accept(converter);
            }

            return converter.getModel(true);
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }

    /*
     * Creates Rascal ASTs for Java source files
     */
    public IValue createAstsFromFiles(ISet files, IBool collectBindings, ISet sourcePath, ISet classPath, IString javaVersion,
            IEvaluatorContext eval) {
        try {
            Map<String, ISourceLocation> cache = new HashMap<>();
            ISetWriter result = VF.setWriter();
            for (IValue file: files) {
                ISourceLocation loc = (ISourceLocation) file;
                CompilationUnit cu = getCompilationUnit(loc.getPath(), getFileContents(loc, eval), collectBindings.getValue(), javaVersion, translatePaths(sourcePath), translatePaths(classPath));

                TypeStore store = new TypeStore();
                store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());
                ASTConverter converter = new ASTConverter(store, cache, collectBindings.getValue());

                converter.set(cu);
                converter.set(loc);
                cu.accept(converter);

                converter.insertCompilationUnitMessages(true, null);
                result.insert(converter.getValue());
            }
            return result.done();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }


    public IValue createAstFromString(ISourceLocation loc, IString contents, IBool collectBindings,ISet sourcePath, ISet classPath, IString javaVersion,
            IEvaluatorContext eval) {
        try {
            CompilationUnit cu = getCompilationUnit(loc.getPath(), contents.getValue().toCharArray(), collectBindings.getValue(), javaVersion, translatePaths(sourcePath), translatePaths(classPath));

            TypeStore store = new TypeStore();
            store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());
            ASTConverter converter = new ASTConverter(store, new HashMap<>(), collectBindings.getValue());

            converter.set(cu);
            converter.set(loc);
            cu.accept(converter);

            converter.insertCompilationUnitMessages(true, null);
            return converter.getValue();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }

    protected CompilationUnit getCompilationUnit(String unitName, char[] contents, boolean resolveBindings, IString javaVersion, String[] sourcePath, String[] classPath) 
            throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setUnitName(unitName);
        parser.setResolveBindings(resolveBindings);
        parser.setSource(contents);
        parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(true);

        Hashtable<String, String> options = new Hashtable<String, String>();

        options.put(JavaCore.COMPILER_SOURCE, javaVersion.getValue());
        options.put(JavaCore.COMPILER_COMPLIANCE, javaVersion.getValue());
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, "enabled");

        parser.setCompilerOptions(options);

        parser.setEnvironment(classPath, sourcePath, null, true);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        return cu;
    }

    private char[] getFileContents(ISourceLocation loc, IEvaluatorContext ctx) throws IOException {
        char[] data;
        Reader textStream = null;

        try {
            textStream = URIResolverRegistry.getInstance().getCharacterReader(loc);
            data = InputConverter.toChar(textStream);
        } finally {
            if (textStream != null) {
                textStream.close();
            }
        }
        return data;
    }
}
