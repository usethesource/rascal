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
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.NullRascalMonitor;
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

            evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), System.in, System.err, System.out, root, heap);
            evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
            evaluator.getConfiguration().setErrors(true);
        } 
        catch (AssertionError e) {
            e.printStackTrace();
            throw e;
        }
    }  

    public RascalJUnitTestRunner(Class<?> clazz) throws URISyntaxException {
        System.err.println("HALLO " + clazz);
        if (clazz.getSimpleName().equals("RunAllRascalTests"))  { 
            String root = System.getProperty("rascal.project.root");
            if (root == null) {
                throw new RuntimeException("No -Drascal.project.root provided to identify location of RASCAL.MF");
            }
            this.projectRoot = URIUtil.createFileLocation(root);
            this.prefix="";
        }
        else {
            this.prefix = clazz.getAnnotation(RascalJUnitTestPrefix.class).value();
            this.projectRoot = RascalJUnitTestRunner.inferProjectRoot(clazz);
        }

        this.clazz = clazz;
        
        System.err.println("Rascal Junit test runner uses Rascal version " + RascalManifest.getRascalVersionNumber());
        System.err.println("Rascal JUnit Project root: " + projectRoot);
        
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
                System.err.println("Adding evaluator search path: " + path);
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
            System.err.println("root: " + root);
            System.err.println("current: " + currentDir);
            System.err.println("cut:" + currentDir.getPath().replaceFirst(root.getPath(), ""));
            String prefix = currentDir.getPath().replaceFirst(root.getPath(), "");
            if (prefix.startsWith("/")) {
                prefix = prefix.substring(1);
            }
            prefix = prefix.replaceAll("/", "::");
            System.err.println("prefix: " + prefix);
            for (ISourceLocation ent : URIResolverRegistry.getInstance().list(currentDir)) {
                System.err.println("ent:" + ent);
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
        Description desc = Description.createSuiteDescription(prefix.equals("") ? "Rascal tests" : prefix);
        this.desc = desc;

        try {
            List<String> modules = new ArrayList<>(10);
            for (String src : new RascalManifest().getSourceRoots(projectRoot)) {
                getRecursiveModuleList(URIUtil.getChildLocation(projectRoot, src + "/" + prefix.replaceAll("::", "/")), modules);
            }
            
            Collections.shuffle(modules); // make sure the import order is different, not just the reported modules

            for (String module : modules) {
                System.err.println("Loading " + module);
                String name = (prefix.isEmpty() ? "" : (prefix + "::")) + module;
                Description modDesc = Description.createSuiteDescription(name);

                try {
                    System.err.println("Loading module:" + name);
                    evaluator.doImport(new NullRascalMonitor(), name);
                    List<AbstractFunction> tests = heap.getModule(name.replaceAll("\\\\","")).getTests();
                
                    if (tests.isEmpty()) {
                        System.err.println("\tskipping. Module has no tests.");
                        continue;
                    }
                    
                    System.err.println("\t adding " + tests.size() + " tests for " + name);
                    desc.addChild(modDesc);

                    // the order of the tests aren't decided by this list so no need to randomly order them.
                    for (AbstractFunction f : tests) {
                        modDesc.addChild(Description.createTestDescription(clazz, computeTestName(f.getName(), f.getAst().getLocation())));
                    }
                }
                catch (Throwable e) {
                    System.err.println("[ERROR] " + e);
                    desc.addChild(modDesc);

                    Description testDesc = Description.createTestDescription(clazz, name + "compilation failed", new CompilationFailed() {
                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return getClass();
                        }
                    });

                    modDesc.addChild(testDesc);
                }
            }

            return desc;
        } catch (IOException e) {
            System.err.println("[ERROR] Could not create tests suite: " + e);
            throw new RuntimeException("could not create test suite", e);
        } 
    }

    @Override
    public void run(final RunNotifier notifier) {
        if (desc == null) {
            desc = getDescription();
        }
        notifier.fireTestRunStarted(desc);

        for (Description mod : desc.getChildren()) {
            if (mod.getAnnotations().stream().anyMatch(t -> t instanceof CompilationFailed)) {
                notifier.fireTestFailure(new Failure(desc, new IllegalArgumentException(mod.getDisplayName() + " had importing errors")));
                continue;
            }

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
