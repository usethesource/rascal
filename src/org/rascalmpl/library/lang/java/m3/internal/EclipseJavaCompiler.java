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
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.unicode.UnicodeDetector;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeStore;

public class EclipseJavaCompiler {
    protected final IValueFactory VF;
    protected final LimitedTypeStore definitions;
    private IRascalMonitor monitor;

    public EclipseJavaCompiler(IValueFactory vf, TypeStore definitions, IRascalMonitor monitor) {
        this.VF = vf;
        this.definitions = new TypeStoreWrapper(definitions);
        this.monitor = monitor;
    }
    
    protected LimitedTypeStore getM3Store() {
        return definitions;
    }

    public IValue createM3FromJarClass(ISourceLocation jarLoc, IList classPath) {
        return createM3FromJarClass(jarLoc, classPath, getM3Store());
    }
    
    public IValue createM3FromSingleClass(ISourceLocation classLoc, IString className, IList classpath) {
        JarConverter converter = new JarConverter(getM3Store(), new HashMap<>());
        converter.convertJarFile(classLoc, ((IString) className).getValue(), classpath);
        return converter.getModel(false);
    }
    
    protected IValue createM3FromJarClass(ISourceLocation jarLoc, IList classPath, LimitedTypeStore store) {
        JarConverter converter = new JarConverter(store, new HashMap<>());
        converter.convertJar(jarLoc, classPath);
        return converter.getModel(false);
    }
    
    public IValue createM3FromJarFile(ISourceLocation jarLoc, IList classPath) {
        return createM3FromJarFile(jarLoc, classPath, getM3Store());
    }
    
    protected IValue createM3FromJarFile(ISourceLocation jarLoc, IList classPath, LimitedTypeStore store) {
        JarConverter converter = new JarConverter(store, new HashMap<>());
        converter.convertJar(jarLoc, classPath);
        return converter.getModel(false);
    }
    
    public IValue createM3sFromFiles(ISet files, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion) {
        return createM3sFromFiles(files, errorRecovery, sourcePath, classPath, javaVersion, getM3Store(), () -> checkInterrupted(monitor));
    }

    protected IValue createM3sFromFiles(ISet files, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion, LimitedTypeStore store, Runnable interruptChecker) {
        try {
            Map<String, ISourceLocation> cache = new HashMap<>();
            ISetWriter result = VF.setWriter();
            buildCompilationUnits(files, true, errorRecovery.getValue(), sourcePath, classPath, javaVersion, (loc, cu) -> {
                interruptChecker.run();
                result.insert(convertToM3(store, cache, loc, cu));
            });
            return result.done();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }
    
    private void checkInterrupted(IRascalMonitor eval) {
        // if (eval.jobIsCanceled(name)) {
        //   throw new InterruptException("Java compiler interrupted", URIUtil.rootLocation("java"));
        // }
    }
    public IValue createM3sAndAstsFromFiles(ISet files, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion) {
        return createM3sAndAstsFromFiles(files, errorRecovery, sourcePath, classPath, javaVersion, getM3Store(), () -> checkInterrupted(monitor));
    }
    
    protected IValue createM3sAndAstsFromFiles(ISet files, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion, LimitedTypeStore store, Runnable interruptChecker) {
        try {
            Map<String, ISourceLocation> cache = new HashMap<>();
            ISetWriter m3s = VF.setWriter();
            ISetWriter asts = VF.setWriter();
            buildCompilationUnits(files, true, errorRecovery.getValue(), sourcePath, classPath, javaVersion, (loc, cu) -> {
                interruptChecker.run();
                m3s.insert(convertToM3(store, cache, loc, cu));
                asts.insert(convertToAST(VF.bool(true), cache, loc, cu, store));
            });
            return VF.tuple(m3s.done(), asts.done());
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
        
    }

    public IValue createM3FromString(ISourceLocation loc, IString contents, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion) {
        return createM3FromString(loc, contents, errorRecovery, sourcePath, classPath, javaVersion, getM3Store()); 
    }
    
    
    protected IValue createM3FromString(ISourceLocation loc, IString contents, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion, LimitedTypeStore store) {
        try {
            CompilationUnit cu = getCompilationUnit(loc.getPath(), contents.getValue().toCharArray(), true, errorRecovery.getValue(), javaVersion, translatePaths(sourcePath), translatePaths(classPath));
            return convertToM3(store, new HashMap<>(), loc, cu);
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }
    
    public IValue createAstsFromFiles(ISet files, IBool collectBindings, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion) {
        return createAstsFromFiles(files, collectBindings, errorRecovery, sourcePath, classPath, javaVersion, getM3Store(), () -> checkInterrupted(monitor));
    }

    protected IValue createAstsFromFiles(ISet files, IBool collectBindings, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion, LimitedTypeStore store, Runnable interruptChecker) {
        try {
            Map<String, ISourceLocation> cache = new HashMap<>();
            ISetWriter result = VF.setWriter();

            buildCompilationUnits(files, collectBindings.getValue(), errorRecovery.getValue(), sourcePath, classPath, javaVersion, (loc, cu) -> {
                interruptChecker.run();
                result.insert(convertToAST(collectBindings, cache, loc, cu, store));
            });
            return result.done();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }

    public IValue createAstFromString(ISourceLocation loc, IString contents, IBool collectBindings, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion) {
        return createAstFromString(loc, contents, collectBindings, errorRecovery, sourcePath, classPath, javaVersion, getM3Store());
    }
    
    protected IValue createAstFromString(ISourceLocation loc, IString contents, IBool collectBindings, IBool errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion, LimitedTypeStore store) {
        try {
            CompilationUnit cu = getCompilationUnit(loc.getPath(), contents.getValue().toCharArray(), collectBindings.getValue(), errorRecovery.getValue(), javaVersion, translatePaths(sourcePath), translatePaths(classPath));
            return convertToAST(collectBindings, new HashMap<>(), loc, cu, store);
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), null, null);
        }
    }

    protected IValue convertToM3(LimitedTypeStore store, Map<String, ISourceLocation> cache, ISourceLocation loc,   CompilationUnit cu) {
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
    
    protected void buildCompilationUnits(ISet files, boolean resolveBindings, boolean errorRecovery, IList sourcePath, IList classPath, IConstructor javaVersion, BiConsumer<ISourceLocation, CompilationUnit> buildNotifier) throws IOException {
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
                throw RuntimeExceptionFactory.io(VF.string("all path entries must have (or resolve to) the file:/// scheme: " + loc), null, null);
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

    protected CompilationUnit getCompilationUnit(String unitName, char[] contents, boolean resolveBindings, boolean errorRecovery, IConstructor javaVersion, String[] sourcePath, String[] classPath) 
            throws IOException {
        ASTParser parser = constructASTParser(resolveBindings, errorRecovery, javaVersion, sourcePath, classPath);
        parser.setUnitName(unitName);
        parser.setSource(contents);
        return (CompilationUnit) parser.createAST(null);
    }

    protected IValue convertToAST(IBool collectBindings, Map<String, ISourceLocation> cache, ISourceLocation loc,
            CompilationUnit cu, LimitedTypeStore store) {
        ASTConverter converter = new ASTConverter(store, cache, collectBindings.getValue());
        converter.convert(cu, cu, loc);
        converter.insertCompilationUnitMessages(true, null);
        return converter.getValue();
    }
    
    protected ASTParser constructASTParser(boolean resolveBindings, boolean errorRecovery, IConstructor javaVersion, String[] sourcePath, String[] classPath) {
        ASTParser parser = ASTParser.newParser(AST.JLS13);
        parser.setResolveBindings(resolveBindings);
        parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(errorRecovery);

        IBool previewParameter = (IBool) javaVersion.asWithKeywordParameters().getParameter("preview");
        if (previewParameter == null) {
            previewParameter = IRascalValueFactory.getInstance().bool(true);
        }

        Hashtable<String, String> options = new Hashtable<String, String>();

        options.put(JavaCore.COMPILER_SOURCE, ((IString) javaVersion.asWithKeywordParameters().getParameter("version")).getValue());
        options.put(JavaCore.COMPILER_COMPLIANCE, ((IString) javaVersion.get("version")).getValue());
        options.put(JavaCore.COMPILER_PB_ENABLE_PREVIEW_FEATURES, previewParameter.getValue() ?  JavaCore.ENABLED :  JavaCore.DISABLED);
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);

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
