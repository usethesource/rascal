/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Jurgen Vinju, Paul Klint, Davy Landman
 */

package org.rascalmpl.test.infrastructure;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.experiments.Compiler.Commands.Rascal;
import org.rascalmpl.library.experiments.Compiler.Commands.RascalC;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.TestExecutor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.lang.rascal.boot.IKernel;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * A JUnit test runner for compiled Rascal tests. Works only in the rascal project itself.
 * 
 * The  approach is as follows:
 *  - The modules to be tested are compiled and linked.
 *  - Meta-data in the compiled modules is used to determine the number of tests and the ignored tests.
 *  - The tests are executed per compiled module
 *  
 * The file IGNORED.config may contain (parts of) module names that will be ignored (using substring comparison)
 */
public class RascalJUnitCompiledTestRunner extends Runner {
    private static final String IGNORED = "test/org/rascalmpl/test_compiled/TESTS.ignored";
    private static IKernel kernel;
    private final IValueFactory vf = ValueFactoryFactory.getValueFactory();

    private PathConfig pcfg;
    private final String[] IGNORED_DIRECTORIES;
   
    private final HashMap<String, Integer> testsPerModule = new HashMap<String, Integer>();
    private final HashMap<String, List<Description>> ignoredPerModule = new HashMap<String, List<Description>>();
    
    private Description desc;
    private String prefix;
    private int totalTests = 0;
    
    public RascalJUnitCompiledTestRunner(Class<?> clazz) {
        initializeKernel();
       
        this.prefix = clazz.getAnnotation(RascalJUnitTestPrefix.class).value().replaceAll("\\\\", "");
       
        this.IGNORED_DIRECTORIES = initializeIgnoredDirectories();
        
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        if (!reg.getRegisteredInputSchemes().contains("project")) {
            reg.registerLogical(new ILogicalSourceLocationResolver() {
                ISourceLocation root = vf.sourceLocation(Paths.get(".").toAbsolutePath().toString().replaceFirst("\\.", ""));
                
                @Override
                public String scheme() {
                    return "project";
                }
                
                @Override
                public ISourceLocation resolve(ISourceLocation input) {
                    return URIUtil.getChildLocation(root, input.getPath());
                }
                
                @Override
                public String authority() {
                    return "rascal";
                }
            });
        }
        
        try {
            this.pcfg = initializePathConfig();
            pcfg.addLibLoc(URIUtil.correctLocation("project", "rascal", "bin"));
        }
        catch (IOException e) {
            assert false; // this project should exist
        }
    }

    private void initializeKernel() {
        if (kernel == null) {
            try {
                kernel = Java2Rascal.Builder.bridge(vf, new PathConfig(), IKernel.class)
                    .trace(false)
                    .profile(false)
                    .verbose(false)
                    .build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }  
    
    private String[] initializeIgnoredDirectories() {
        String[] ignored = new String[0];
        
        try (InputStream ignoredStream = new FileInputStream(Paths.get(".").toAbsolutePath().normalize().resolve(IGNORED).toString());
            Scanner ignoredScanner = new Scanner(ignoredStream, "UTF-8")){

            // TODO: It is probably better to replace this by a call to a JSON reader
            // See org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.Settings

            String text = ignoredScanner.useDelimiter("\\A").next();

            ignored = text.split("\\n");
            int emptyLines = 0;
            for(int i = 0; i < ignored.length; i++){   // Strip comments
                String ignore = ignored[i];
                int comment = ignore.indexOf("//");
                if(comment >= 0){
                    ignore = ignore.substring(0, comment);
                }
                ignored[i] =  ignore.replaceAll("/",  "::").trim();
                if(ignored[i].isEmpty()){
                    emptyLines++;
                }
            }
            if(emptyLines > 0){                                    // remove empty lines
                String[] tmp = new String[ignored.length - emptyLines];
                int k = 0;
                for(int i = 0; i < ignored.length; i++){
                    if(!ignored[i].isEmpty()){
                        tmp[k++] = ignored[i];
                    }
                }
                ignored = tmp;
            }
            
            return ignored;
        } catch (IOException e1) {
            System.err.println(IGNORED + " not found; no ignored directories");
            return new String[0];
        }
    }

    private PathConfig initializePathConfig() throws IOException {
        ISourceLocation rootProject = URIUtil.correctLocation("project", "rascal", "/");
        return new RascalManifest().makePathConfig(rootProject);
    }

    @Override
    public int testCount(){
        getDescription();
        return totalTests;
    }

    boolean isAcceptable(String rootModule, String candidate){
        if(!rootModule.isEmpty()){
            candidate = rootModule + "::" + candidate;
        }
        for(String ignore : IGNORED_DIRECTORIES){
            if(candidate.contains(ignore)){
                System.err.println("Ignoring: " + candidate);
                return false;
            }
        }
        return true;
    }

    private List<String> getRecursiveModuleList(ISourceLocation root, List<String> result) throws IOException {
        Queue<ISourceLocation> todo = new LinkedList<>();
        String rootPath = root.getPath().replaceFirst("/", "").replaceAll("/", "::");
        todo.add(root);
        while (!todo.isEmpty()) {
            ISourceLocation currentDir = todo.poll();
            String prefix = currentDir.getPath().replaceFirst(root.getPath(), "").replaceFirst("/", "").replaceAll("/", "::");
            for (ISourceLocation ent : URIResolverRegistry.getInstance().list(currentDir)) {
                if (ent.getPath().endsWith(".rsc")) {	
                    String candidate = (prefix.isEmpty() ? "" : (prefix + "::")) + URIUtil.getLocationName(ent).replace(".rsc", "");
                    if(isAcceptable(rootPath, candidate)){
                        result.add(candidate);
                    }
                } else {
                    if (URIResolverRegistry.getInstance().isDirectory(ent) && !todo.contains(ent)){
                        todo.add(ent);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Description getDescription() {			
        if (desc != null) {
            return desc;
        }

        
        Description desc = Description.createSuiteDescription(prefix);
        this.desc = desc;

        URIResolverRegistry resolver = URIResolverRegistry.getInstance();
        try {
            List<String> modules = new ArrayList<>();

            for (IValue loc : pcfg.getSrcs()) {
                getRecursiveModuleList(URIUtil.getChildLocation((ISourceLocation) loc, "/" + prefix.replaceAll("::", "/")), modules);
            }

            for (String module : modules) {
                String qualifiedName = (prefix.isEmpty() ? "" : prefix + "::") + module;
                RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(pcfg).build();
                ISourceLocation binary = Rascal.findBinary(pcfg.getBin(), qualifiedName);
                ISourceLocation source =  rex.getPathConfig().resolveModule(qualifiedName);

                //  Do a sufficient but not complete check on the binary; changes to imports will go unnoticed!
                if(!resolver.exists(binary) || resolver.lastModified(source) > resolver.lastModified(binary)){
                    IList programs = kernel.compileAndLink(
                        vf.list(vf.string(qualifiedName)),
                        pcfg.asConstructor(kernel),
                        kernel.kw_compileAndLink().enableAsserts(true).reloc(vf.sourceLocation("noreloc", "", "")));

                    if (!RascalC.handleMessages(programs, pcfg)) {
                        Description modDesc = Description.createTestDescription(getClass(), qualifiedName, new CompilationFailed() {
                            @Override
                            public Class<? extends Annotation> annotationType() {
                                return getClass();
                            }
                        });
                        desc.addChild(modDesc);
                        return desc;
                    }
                }

                RVMExecutable executable = RVMExecutable.read(binary);

                if (!RascalC.handleMessages(pcfg, executable.getErrors())) {
                    Description modDesc = Description.createTestDescription(getClass(), qualifiedName, new CompilationFailed() {
                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return getClass();
                        }
                    });
                    desc.addChild(modDesc);
                    return desc;
                }
                
                if(executable.getTests().size() > 0){
                    Description modDesc = Description.createSuiteDescription(qualifiedName);
                    desc.addChild(modDesc);
                    int ntests = 0;
                    LinkedList<Description> module_ignored = new LinkedList<Description>();

                    for (Function f : executable.getTests()) {
                        String test_name = f.computeTestName();
                        Description d = Description.createTestDescription(getClass(), test_name);
                        modDesc.addChild(d);
                        ntests++;
 
                        if(f.isIgnored(rex)){
                            module_ignored.add(d);
                        }
                    }

                    testsPerModule.put(qualifiedName,  ntests);
                    ignoredPerModule.put(qualifiedName, module_ignored);
                    totalTests += ntests;
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } 
        
        return desc;
    }

    @Override
    public void run(final RunNotifier notifier) {
        if (desc == null) {
            desc = getDescription();
        }
       
        notifier.fireTestRunStarted(desc);

        if (desc.getAnnotations().stream().anyMatch(t -> t instanceof CompilationFailed)) {
            notifier.fireTestFailure(new Failure(desc, new IllegalArgumentException("test module has compilation errors")));
            return;
        }
        
        for (Description mod : desc.getChildren()) {
            RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(pcfg).build();
            ISourceLocation binary = null;
            RVMCore rvmCore = null;
            
            if (mod.getAnnotations().stream().anyMatch(t -> t instanceof CompilationFailed)) {
                notifier.fireTestFailure(new Failure(desc, new IllegalArgumentException("test module has compilation errors")));
                continue;
            }
            
            try {
                binary = Rascal.findBinary(pcfg.getBin(), mod.getDisplayName());
                rvmCore = ExecutionTools.initializedRVM(binary, rex);
            } catch (IOException e1) {
                notifier.fireTestFailure(new Failure(mod, e1));
            }

            Listener listener = new Listener(notifier, mod);
            TestExecutor runner = new TestExecutor(rvmCore, listener, rex);
            try {
                runner.test(mod.getDisplayName(), testsPerModule.get(mod.getClassName())); 
                listener.done();
            } 
            catch (Throwable e) {
                // Something went totally wrong while running the compiled tests, force all tests in this suite to fail.
                System.err.println("RascalJunitCompiledTestrunner.run: " + mod.getMethodName() + " unexpected exception: " + e.getMessage());
                e.printStackTrace(System.err);
                notifier.fireTestFailure(new Failure(mod, e));
            }
        }

        notifier.fireTestRunFinished(new Result());
    }

    private final class Listener implements ITestResultListener {
        private final RunNotifier notifier;
        private final Description module;

        private Listener(RunNotifier notifier, Description module) {
            this.notifier = notifier;
            this.module = module;
        }

        private Description getDescription(String testName, ISourceLocation loc) {

            for (Description child : module.getChildren()) {
                if (child.getMethodName().equals(testName)) {
                    return child;
                }
            }

            throw new IllegalArgumentException(testName + " test was never registered");
        }

        @Override
        public void ignored(String test, ISourceLocation loc) {
            notifier.fireTestIgnored(getDescription(test, loc));
        }

        @Override
        public void start(String context, int count) {
            notifier.fireTestRunStarted(module);
        }

        @Override
        public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable t) {
            Description desc = getDescription(test, loc);
            notifier.fireTestStarted(desc);

            if (!successful) {
                notifier.fireTestFailure(new Failure(desc, t != null ? t : new AssertionError(message == null ? "test failed" : message)));
            }
            else {
                notifier.fireTestFinished(desc);
            }
        }

        @Override
        public void done() {
            notifier.fireTestRunFinished(new Result());
        }
    }
}
