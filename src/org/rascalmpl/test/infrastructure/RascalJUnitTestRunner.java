/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Paul Klint, Jurgen Vinju
 */

package org.rascalmpl.test.infrastructure;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.DumbTerminal;
import org.jline.terminal.impl.DumbTerminalProvider;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.TestEvaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.library.util.PathConfig.RascalConfigMode;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.uri.project.TargetURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

public class RascalJUnitTestRunner extends Runner {
    private static class InstanceHolder {
		final static IRascalMonitor monitor;
        static {
            Terminal tm;
            try {
                tm = TerminalBuilder.terminal();
            }
            catch (IOException e) {
                try {
                    tm = new DumbTerminal(System.in, System.err);
                }
                catch (IOException e1) {
                    throw new IllegalStateException("Could not create a terminal representation");
                }
            }
            monitor = IRascalMonitor.buildConsoleMonitor(tm);
        }
    }
   
   	public static IRascalMonitor getCommonMonitor() {
	    return InstanceHolder.monitor;
   	}

    private static Evaluator evaluator;
    private static GlobalEnvironment heap;
    private static ModuleEnvironment root;
    private Description desc;

    private final String prefix;
    private final ISourceLocation projectRoot;
    private final Class<?> clazz;

    static {
        try {
            heap = new GlobalEnvironment();
            root = heap.addModule(new ModuleEnvironment("___junit_test___", heap));
            evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), Reader.nullReader(), new PrintWriter(System.err, true), new PrintWriter(System.out, false), root, heap, getCommonMonitor());
        
            evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
            evaluator.getConfiguration().setErrors(true);
        } 
        catch (AssertionError e) {
            e.printStackTrace();
            throw e;
        }
    }  

    public RascalJUnitTestRunner(Class<?> clazz) {
        this.prefix = clazz.getAnnotation(RascalJUnitTestPrefix.class).value();
        this.projectRoot = inferProjectRoot(clazz);
        this.clazz = clazz;
        
        System.err.println("Rascal JUnit test runner uses Rascal version " + RascalManifest.getRascalVersionNumber());
        System.err.println("Rascal JUnit project root: " + projectRoot);
        

        if (projectRoot != null) {
            configureProjectEvaluator(evaluator, projectRoot);
        }
        else {
            throw new IllegalArgumentException("could not setup tests for " + clazz.getCanonicalName());
        }
    }
    
    public static void configureProjectEvaluator(Evaluator evaluator, ISourceLocation projectRoot) {
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        String projectName = new RascalManifest().getProjectName(projectRoot);
        reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
        reg.registerLogical(new TargetURIResolver(projectRoot, projectName));
        
        try {
            PathConfig pcfg = PathConfig.fromSourceProjectRascalManifest(projectRoot, RascalConfigMode.INTERPETER);
            
            for (IValue path : pcfg.getSrcs()) {

                evaluator.addRascalSearchPath((ISourceLocation) path); 
            }
            
            ClassLoader cl = new SourceLocationClassLoader(pcfg.getClassloaders(), ShellEvaluatorFactory.class.getClassLoader());
            evaluator.addClassLoader(cl);
        }
        catch (AssertionError e) {
            e.printStackTrace();
            throw e;
        }
        catch (IOException e) {
            System.err.println(e);
        }
    }

    public static ISourceLocation inferProjectRoot(Class<?> clazz) {
        try {
            String file = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
            if (file.endsWith(".jar")) {
                throw new IllegalArgumentException("can not run Rascal JUnit tests from within a jar file");
            }

            File current = new File(file);
            
            while (current != null && current.exists() && current.isDirectory()) {
                if (new File(current, "META-INF/RASCAL.MF").exists()) {
                    // this is perhaps the copy of RASCAL.MF in a bin/target folder;
                    // it would be better to find the source RASCAL.MF such that tests
                    // are run against the sources of test files rather than the ones accidentally
                    // copied to the bin folder.
                    
                    // TODO: if someone knows how to parametrize this nicely instead of hard-coding the
                    // mvn project setup, I'm all ears. It has to work from both the Eclipse JUnit runner 
                    // and MVN surefire calling contexts. 
                    if (current.getName().equals("classes") && current.getParentFile().getName().equals("target")) {
                        current = current.getParentFile().getParentFile();
                        continue; // try again in the source folder
                    }
                    
                    return URIUtil.createFileLocation(current.getAbsolutePath());
                }
                current = current.getParentFile();
            }
        }
        catch (URISyntaxException e) {
            System.err.println("[ERROR] can not infer project root:" + e);
            return null;
        }
        
        return null;
    }

    public static String computeTestName(String name, ISourceLocation loc) {
        return name + ": <" + loc.getOffset() +"," + loc.getLength() +">";
    }

    public static List<String> getRecursiveModuleList(ISourceLocation root, List<String> result) throws IOException {
        Queue<ISourceLocation> todo = new LinkedList<>();
        
        todo.add(root);

        while (!todo.isEmpty()) {
            ISourceLocation currentDir = todo.poll();

            if (!URIResolverRegistry.getInstance().exists(currentDir)) {
                // this happens due to searching in the entire classpath
                 continue;
            }

            String prefix = currentDir.getPath().replaceFirst(root.getPath(), "").replaceFirst("/", "").replaceAll("/", "::");
            for (ISourceLocation ent : URIResolverRegistry.getInstance().list(currentDir)) {
                if (ent.getPath().endsWith(".rsc")) {
                    if (prefix.isEmpty()) {
                        result.add(URIUtil.getLocationName(ent).replace(".rsc", ""));
                    }
                    else {
                        result.add(prefix + "::" + URIUtil.getLocationName(ent).replace(".rsc", ""));
                    }
                }
                else {
                    if (URIResolverRegistry.getInstance().isDirectory(ent)) {
                        todo.add(ent);
                    }
                }
            }
        }
        return result;

    }
    @Override
    public Description getDescription() {	
        Description desc = Description.createSuiteDescription(prefix);
        this.desc = desc;

        evaluator.job(Evaluator.LOADING_JOB_CONSTANT, 1, (jobName) -> {	
            try {
                List<String> modules = new ArrayList<>(10);
                evaluator.jobTodo(jobName, modules.size());
                for (String src : new RascalManifest().getSourceRoots(projectRoot)) {
                    getRecursiveModuleList(URIUtil.getChildLocation(projectRoot, src + "/" + prefix.replaceAll("::", "/")), modules);
                }
                
                Collections.shuffle(modules); // make sure the import order is different, not just the reported modules
                
                for (String module : modules) {
                    String name = prefix + "::" + module;
                    Description modDesc = Description.createSuiteDescription(name);

                    try {
                        evaluator.doNextImport(jobName, name);
                        List<AbstractFunction> tests = heap.getModule(name.replaceAll("\\\\","")).getTests();
                    
                        if (tests.isEmpty()) {
                            continue;
                        }
                        
                        desc.addChild(modDesc);

                        // the order of the tests aren't decided by this list so no need to randomly order them.
                        for (AbstractFunction f : tests) {
                            modDesc.addChild(Description.createTestDescription(clazz, computeTestName(f.getName(), f.getAst().getLocation())));
                        }
                    }
                    catch (Throwable e) {
                        desc.addChild(modDesc);

                        Description testDesc = Description.createTestDescription(clazz, name + " compilation failed", new CompilationFailed() {
                            @Override
                            public Class<? extends Annotation> annotationType() {
                                return getClass();
                            }
                        });

                        modDesc.addChild(testDesc);
                    }
                }

                return true;
            } catch (IOException e) {
                Description testDesc = Description.createTestDescription(clazz, prefix + " compilation failed: " + e.getMessage(), new CompilationFailed() {
                            @Override
                            public Class<? extends Annotation> annotationType() {
                                return getClass();
                            }
                        });

                desc.addChild(testDesc);

                evaluator.warning("Could not create tests suite: " + e, URIUtil.rootLocation("unknown"));
                
                return false;
            } 
        });

        return desc;
    }

    @Override
    public void run(final RunNotifier notifier) {
        if (desc == null) {
            desc = getDescription();
        }
        notifier.fireTestRunStarted(desc);

        for (Description mod : desc.getChildren()) {
            // TODO: this will never match because we are on the level of module descriptions now.
            // This the reason that modules with errors in them silently succeed with 0 tests run.
            if (mod.getAnnotations().stream().anyMatch(t -> t instanceof CompilationFailed)) {
                notifier.fireTestFailure(new Failure(desc, new IllegalArgumentException(mod.getDisplayName() + " had importing errors")));
                break;
            }

            // TODO: we lose the link here with the test Descriptors for specific test functions
            // and this impacts the accuracy of the reporting (only module name, not test name which failed)
            Listener listener = new Listener(notifier, mod);
            TestEvaluator runner = new TestEvaluator(evaluator, listener);
            runner.test(mod.getDisplayName());
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

        private Description getDescription(String name, ISourceLocation loc) {
            String testName = computeTestName(name, loc);

            for (Description child : module.getChildren()) {
                if (child.getMethodName().equals(testName)) {
                    return child;
                }
            }

            throw new IllegalArgumentException(name + " test was never registered");
        }


        @Override
        public void start(String context, int count) {
            notifier.fireTestRunStarted(module);
        }

        @Override
        public void ignored(String test, ISourceLocation loc) {
            notifier.fireTestIgnored(getDescription(test, loc));
        }

        @Override
        public void report(boolean successful, String test, ISourceLocation loc,	String message, Throwable t) {
            Description desc = getDescription(test, loc);
            notifier.fireTestStarted(desc);

            if (!successful) {
                notifier.fireTestFailure(new Failure(desc, t != null ? t : new Exception(message != null ? message : "no message")));
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
