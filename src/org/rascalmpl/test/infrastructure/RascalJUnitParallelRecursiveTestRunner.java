/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Davy Landman  - Davy.Landman@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.infrastructure;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.TestEvaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;

/**
 * Rascal modules can be tested separately from each other. This runner includes all modules and nested modules and runs them spread over a workpool
 * The size of the workppol is limited by to following:
 * - 1 worker per 300MB available memory (-Xmx)
 * - only as much workers as there are cpu's (-1 to keep room for the GC)
 * - a maximum of 4 since after that the cache misses in the interpreter hurt more than we get benefit
 * @author Davy Landman
 *
 */
public class RascalJUnitParallelRecursiveTestRunner extends Runner {

    private final int numberOfWorkers;
    private final Semaphore importsCompleted = new Semaphore(0);
    private final Semaphore waitForRunning = new Semaphore(0);
    private final Semaphore workersCompleted = new Semaphore(0);

    private final String[] prefixes;
    private final Queue<String> modules = new ConcurrentLinkedQueue<>();
    private final Queue<Description> descriptions = new ConcurrentLinkedQueue<>();
    private final Queue<Consumer<RunNotifier>> results = new ConcurrentLinkedQueue<>();

    private Description rootDesc;
    
    private final ISourceLocation projectRoot;

    public RascalJUnitParallelRecursiveTestRunner(Class<?> clazz) {
        System.err.println("Rascal JUnit uses Rascal version " + RascalManifest.getRascalVersionNumber());
        
        this.projectRoot = RascalJUnitTestRunner.inferProjectRoot(clazz);
        System.err.println("Rascal JUnit Project root: " + projectRoot);
        
        int numberOfWorkers = Math.min(4, Runtime.getRuntime().availableProcessors() - 1);
        System.out.println("Number of workers based on CPU: " + numberOfWorkers);
        if (numberOfWorkers > 1) {
            numberOfWorkers = Math.min(numberOfWorkers, (int)(Runtime.getRuntime().maxMemory()/ (1024*1024*300L)));
            System.out.println("Number of workers based on memory: " + numberOfWorkers + " (" + Runtime.getRuntime().maxMemory() / (1024*1024) + ")");
        }

        if (numberOfWorkers < 1) {
            this.numberOfWorkers = 1;
        }
        else {
            this.numberOfWorkers = numberOfWorkers;
        }
        System.out.println("Running parallel test with " + this.numberOfWorkers + " runners");
        System.out.flush();
        this.prefixes = clazz.getAnnotation(RecursiveRascalParallelTest.class).value();
    }


    @Override
    public Description getDescription() {
        if (rootDesc == null) {
            fillModuleWorkList();
            startModuleTesters();

            rootDesc = Description.createSuiteDescription(projectRoot.toString());
            processIncomingModuleDescriptions(rootDesc);

            assert descriptions.isEmpty();
        }
        return rootDesc;
    }

    @Override
    public void run(RunNotifier notifier) {
        assert rootDesc != null;
        notifier.fireTestRunStarted(rootDesc);
        runTests(notifier);
        notifier.fireTestRunFinished(new Result());
    }

    private void processIncomingModuleDescriptions(Description rootDesc) {
        int completed = 0;
        while (completed < numberOfWorkers) {
            try {
                if (importsCompleted.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                    completed++;
                }
            }
            catch (InterruptedException e) {
            }
            // consume stuff in the current queue
            Description newDescription;
            while ((newDescription = descriptions.poll()) != null) {
                rootDesc.addChild(newDescription);
            }
        }
    }

    private void startModuleTesters() {
        for (int i = 0; i < numberOfWorkers; i++) {
            new ModuleTester("JUnit Rascal Evaluator " + (i + 1)).start();
        }
    }
    
    private void fillModuleWorkList() {
        for (String prefix: prefixes) {
            try {
                List<String> result = new ArrayList<>();
                for (String src : new RascalManifest().getSourceRoots(projectRoot)) {
                    RascalJUnitTestRunner.getRecursiveModuleList(URIUtil.getChildLocation(projectRoot, src + "/" + prefix.replaceAll("::", "/")), result);
                }
                
                result.stream().map(m -> prefix + "::" + m).forEach(n -> modules.add(n));
            }
            catch (IOException e) {
            }
        }
    }

    private void runTests(RunNotifier notifier) {
        waitForRunning.release(numberOfWorkers);
        int completed = 0;
        while (completed < numberOfWorkers) {
            try {
                if (workersCompleted.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                    completed++;
                }
            }
            catch (InterruptedException e) {
            }
            Consumer<RunNotifier> newResult;
            while ((newResult = results.poll()) != null) {
                newResult.accept(notifier);
            }
        }

        RascalJUnitTestRunner.getCommonMonitor().endAllJobs();
        assert results.isEmpty();
    }


    public class ModuleTester extends Thread {
        private GlobalEnvironment heap;
        private ModuleEnvironment root;
        
        private PrintWriter stderr;
        private PrintWriter stdout;
        private Evaluator evaluator;
        private final List<Description> testModules = new ArrayList<>();

        public ModuleTester(String name) {
            super(name);
        }

        @Override
        public void run() {
            initializeEvaluator();
            processModules();
            runTests();
        }

        private void runTests() {
            try {
                if (waitForRunSignal()) {
                    evaluator.job("Testing modules", testModules.size(), (String jn) ->  {
                        for (Description mod: testModules) {
                            evaluator.jobStep(jn, mod.getDisplayName(), 1);
                            Listener trl = new Listener(mod, stdout);
                            
                            if (mod.getAnnotations().stream().anyMatch(t -> t instanceof CompilationFailed)) {
                                results.add(notifier -> {
                                    notifier.fireTestStarted(mod);
                                    notifier.fireTestFailure(new Failure(mod, new IllegalArgumentException(mod.getDisplayName() + " had import/compilation errors")));
                                });
                                continue;
                            }
                            
                            TestEvaluator runner = new TestEvaluator(evaluator, trl);
                            runner.test(mod.getDisplayName());
                            
                            stdout.flush();
                            stderr.flush();
                        }

                        return true;
                    });
                }
            
            }
            finally {
                workersCompleted.release();
            }
        }

        private boolean waitForRunSignal() {
            try {
                waitForRunning.acquire();
                return true;
            }
            catch (InterruptedException e) {
                return false;
            }
        }

        private void processModules() {
            evaluator.job("Importing test modules", 0, (jn) -> {
                try {
                    String module;

                    while ((module = modules.poll()) != null) {
                        evaluator.jobTodo(jn, 1);
                       
                        try {
                            evaluator.doImport(evaluator.getMonitor(), module);
                        }
                        catch (Throwable e) {
                            synchronized(stdout) {
                                evaluator.warning("Could not import " + module + " for testing...", null);
                                evaluator.warning(e.getMessage(), null);
                                e.printStackTrace(evaluator.getOutPrinter());
                            } 
                            
                            // register a failing module to make sure we report failure later on. 
                            
                            Description testDesc = Description.createTestDescription(getClass(), module, new CompilationFailed() {
                                @Override
                                public Class<? extends Annotation> annotationType() {
                                    return getClass();
                                }
                            });

                            testModules.add(testDesc);
                            descriptions.add(testDesc);

                            continue;
                        }
                        finally {
                            evaluator.jobStep(jn, "Imported " + module);
                        }

                        ModuleEnvironment moduleEnv = heap.getModule(module.replaceAll("\\\\",""));
                        if (!moduleEnv.getTests().isEmpty()) {
                            Description modDesc = Description.createSuiteDescription(module);
                            for (AbstractFunction f : moduleEnv.getTests()) {
                                modDesc.addChild(Description.createTestDescription(getClass(), RascalJUnitTestRunner.computeTestName(f.getName(), f.getAst().getLocation())));
                            }
                            descriptions.add(modDesc);
                            testModules.add(modDesc);
                        }
                    }
                    // let's shuffle them
                    Collections.shuffle(testModules); 
                }
                finally {
                    importsCompleted.release();
                }

                return true;
            });
        }

        private void initializeEvaluator() {
            heap = new GlobalEnvironment();
            root = heap.addModule(new ModuleEnvironment("___junit_test___", heap));
            
            evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), System.in, System.err, System.out, root, heap, RascalJUnitTestRunner.getCommonMonitor());
            stdout = new PrintWriter(evaluator.getStdOut());
            stderr = new PrintWriter(evaluator.getStdErr());

            evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
            evaluator.getConfiguration().setErrors(true);

            RascalJUnitTestRunner.configureProjectEvaluator(evaluator, projectRoot);
        }

    }

    public class Listener implements ITestResultListener {
        private final PrintWriter stderr;
        private final Description module;

        public Listener(Description module, PrintWriter stderr) {
            this.module = module;
            this.stderr = stderr;
        }

        private Description getDescription(String name, ISourceLocation loc) {
            String testName = RascalJUnitTestRunner.computeTestName(name, loc);

            for (Description child : module.getChildren()) {
                if (child.getMethodName().equals(testName)) {
                    return child;
                }
            }

            throw new IllegalArgumentException(name + " test was never registered");
        }

        @Override
        public void start(String context, int count) {
            
        }

        @Override
        public void done() {
            
        }

        @Override
        public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable exception) {
            Description desc = getDescription(test, loc);
            results.add(notifier -> {
                notifier.fireTestStarted(desc);

                if (!successful) {
                    if (exception != null) {
                        synchronized(stderr) {
                            exception.printStackTrace(stderr);
                        }
                    }
                    notifier.fireTestFailure(new Failure(desc, exception != null ? exception : new Exception(message != null ? message : "no message")));
                }
                else {
                    notifier.fireTestFinished(desc);
                }
            });
        }

        @Override
        public void ignored(String test, ISourceLocation loc) {
            Description desc = getDescription(test, loc);
            results.add(notifier -> notifier.fireTestIgnored(desc));
        }
    }
}
