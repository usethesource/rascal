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
 *   * Davy Landman - Davy.Landman@cwi.nl (CWI)
 *******************************************************************************/
package org.rascalmpl.library.lang.java.m3.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.BiConsumer;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalRuntimeException;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.unicode.UnicodeDetector;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.TypeStore;

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
        converter.convert(jarLoc, eval);
        return converter.getModel(false);
    }

    public IValue createM3sFromFiles(ISet files, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion, IEvaluatorContext eval) {
        try {
            TypeStore store = new TypeStore();
            store.extendStore(eval.getHeap().getModule("lang::java::m3::Core").getStore());
            store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());
            Map<String, ISourceLocation> cache = new HashMap<>();
            ISetWriter result = VF.setWriter();
            buildCompilationUnits(files, true, errorRecovery.getValue(), sourcePath, classPath, javaVersion, (loc, cu) -> {
                checkInterrupted(eval);
                result.insert(convertToM3(store, cache, loc, cu));
            });
            return result.done();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }
    
    private void checkInterrupted(IEvaluatorContext eval) {
        if (eval.isInterrupted()) {
          throw new InterruptException(eval.getStackTrace(), eval.getCurrentAST().getLocation());
        }
    }

    public IValue createM3sAndAstsFromFiles(ISet files, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion, IEvaluatorContext eval) {
        try {
            TypeStore store = new TypeStore();
            store.extendStore(eval.getHeap().getModule("lang::java::m3::Core").getStore());
            store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());
            Map<String, ISourceLocation> cache = new HashMap<>();
            ISetWriter m3s = VF.setWriter();
            ISetWriter asts = VF.setWriter();
            buildCompilationUnits(files, true, errorRecovery.getValue(), sourcePath, classPath, javaVersion, (loc, cu) -> {
                checkInterrupted(eval);
                m3s.insert(convertToM3(store, cache, loc, cu));
                asts.insert(convertToAST(VF.bool(true), cache, loc, cu, store));
            });
            return VF.tuple(m3s.done(), asts.done());
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
        
    }


    public IValue createM3FromString(ISourceLocation loc, IString contents, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion, IEvaluatorContext eval) {
        try {
            CompilationUnit cu = getCompilationUnit(loc.getPath(), contents.getValue().toCharArray(), true, errorRecovery.getValue(), javaVersion, translatePaths(sourcePath), translatePaths(classPath));

            TypeStore store = new TypeStore();
            store.extendStore(eval.getHeap().getModule("lang::java::m3::Core").getStore());
            store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());

            return convertToM3(store, new HashMap<>(), loc, cu);
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }

    public IValue createAstsFromFiles(ISet files, IBool collectBindings, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion,
            IEvaluatorContext eval) {
        try {
            TypeStore store = new TypeStore();
            store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());
            
            Map<String, ISourceLocation> cache = new HashMap<>();
            ISetWriter result = VF.setWriter();

            buildCompilationUnits(files, collectBindings.getValue(), errorRecovery.getValue(), sourcePath, classPath, javaVersion, (loc, cu) -> {
                checkInterrupted(eval);
                result.insert(convertToAST(collectBindings, cache, loc, cu, store));
            });
            return result.done();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }

    public IValue createAstFromString(ISourceLocation loc, IString contents, IBool collectBindings, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion,
            IEvaluatorContext eval) {
        try {
            CompilationUnit cu = getCompilationUnit(loc.getPath(), contents.getValue().toCharArray(), collectBindings.getValue(), errorRecovery.getValue(), javaVersion, translatePaths(sourcePath), translatePaths(classPath));

            TypeStore store = new TypeStore();
            store.extendStore(eval.getHeap().getModule("lang::java::m3::AST").getStore());

            return convertToAST(collectBindings, new HashMap<>(), loc, cu, store);
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }

    protected IValue convertToM3(TypeStore store, Map<String, ISourceLocation> cache, ISourceLocation loc,
            CompilationUnit cu) {
        SourceConverter converter = new SourceConverter(store, cache);
        converter.convert(cu, cu, loc);
        for (Object cm: cu.getCommentList()) {
            Comment comment = (Comment)cm;
            // Issue 720: changed condition to only visit comments without a parent (includes line, block and misplaced javadoc comments).
            if (comment.getParent() != null)
                continue;
            converter.convert(cu, comment, loc);
        }
        return converter.getModel(true);
    }
    
    protected void buildCompilationUnits(ISet files, boolean resolveBindings, boolean errorRecovery, IList sourcePath, IList classPath, IString javaVersion, BiConsumer<ISourceLocation, CompilationUnit> buildNotifier) throws IOException {
        boolean fastPath = true;
        for (IValue f : files) {
            fastPath &= safeResolve((ISourceLocation)f).getScheme().equals("file");
        }
        if (fastPath) {
            Map<String, ISourceLocation> reversePathLookup = new HashMap<>();
            String[] absolutePaths = new String[files.size()];
            String[] encodings = new String[absolutePaths.length];
            int i = 0;
            for (IValue p : files) {
                ISourceLocation loc = (ISourceLocation)p;
                if (!URIResolverRegistry.getInstance().isFile(loc)) {
                    throw RuntimeExceptionFactory.io(VF.string("" + loc  + " is not a file"), null, null);
                }
                if (!URIResolverRegistry.getInstance().exists(loc)) {
                    throw RuntimeExceptionFactory.io(VF.string("" + loc  + " doesn't exist"), null, null);
                }

                absolutePaths[i] = new File(safeResolve(loc).getPath()).getAbsolutePath();
                reversePathLookup.put(absolutePaths[i], loc);
                encodings[i] = guessEncoding(loc);
                i++;
            }

            ASTParser parser = constructASTParser(resolveBindings, errorRecovery, javaVersion, translatePaths(sourcePath), translatePaths(classPath));
            parser.createASTs(absolutePaths, encodings, new String[0], new FileASTRequestor() {
                @Override
                public void acceptAST(String sourceFilePath, CompilationUnit ast) {
                    buildNotifier.accept(reversePathLookup.get(sourceFilePath), ast);
                }
            }, null);
        }
        else {
            for (IValue file: files) {
                ISourceLocation loc = (ISourceLocation) file;
                CompilationUnit cu = getCompilationUnit(loc.getPath(), getFileContents(loc), resolveBindings, errorRecovery, javaVersion, translatePaths(sourcePath), translatePaths(classPath));
                buildNotifier.accept(loc, cu);
            }
        }
    }

    protected String[] translatePaths(IList paths) {
        String[] result = new String[paths.length()];
        int i = 0;
        for (IValue p : paths) {
            ISourceLocation loc = safeResolve((ISourceLocation)p);
            if (!loc.getScheme().equals("file")) {
                throw RascalRuntimeException.io(VF.string("all path entries must have (or resolve to) the file:/// scheme: " + loc), null);
            }
            result[i++] = new File(loc.getPath()).getAbsolutePath();
        }
        return result;
    }

    private ISourceLocation safeResolve(ISourceLocation loc) {
        try {
            ISourceLocation result = URIResolverRegistry.getInstance().logicalToPhysical(loc);
            if (result != null) {
                return result;
            }
            return loc;
        }
        catch (IOException e) {
            return loc;
        }
    }

    protected String guessEncoding(ISourceLocation loc) {
        try {
            Charset result = URIResolverRegistry.getInstance().getCharset(loc);
            if (result != null) {
                return result.name();
            }
            try (InputStream file = URIResolverRegistry.getInstance().getInputStream(loc)) {
                return UnicodeDetector.estimateCharset(file).name();
            }
        }
        catch (Throwable x) {
            return null;
        }
    }

    protected CompilationUnit getCompilationUnit(String unitName, char[] contents, boolean resolveBindings, boolean errorRecovery, IString javaVersion, String[] sourcePath, String[] classPath) 
            throws IOException {
        ASTParser parser = constructASTParser(resolveBindings, errorRecovery, javaVersion, sourcePath, classPath);
        parser.setUnitName(unitName);
        parser.setSource(contents);
        return (CompilationUnit) parser.createAST(null);
    }

    protected IValue convertToAST(IBool collectBindings, Map<String, ISourceLocation> cache, ISourceLocation loc,
            CompilationUnit cu, TypeStore store) {
        ASTConverter converter = new ASTConverter(store, cache, collectBindings.getValue());
        converter.convert(cu, cu, loc);
        converter.insertCompilationUnitMessages(true, null);
        return converter.getValue();
    }
    
    protected ASTParser constructASTParser(boolean resolveBindings, boolean errorRecovery, IString javaVersion, String[] sourcePath, String[] classPath) {
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setResolveBindings(resolveBindings);
        parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(errorRecovery);

        Hashtable<String, String> options = new Hashtable<String, String>();

        options.put(JavaCore.COMPILER_SOURCE, javaVersion.getValue());
        options.put(JavaCore.COMPILER_COMPLIANCE, javaVersion.getValue());
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, "enabled");

        parser.setCompilerOptions(options);

        parser.setEnvironment(classPath, sourcePath, null, true);
        return parser;
    }

    protected char[] getFileContents(ISourceLocation loc) throws IOException {
        try (Reader textStream = URIResolverRegistry.getInstance().getCharacterReader(loc)) {
            return InputConverter.toChar(textStream);
        }
    }
}
