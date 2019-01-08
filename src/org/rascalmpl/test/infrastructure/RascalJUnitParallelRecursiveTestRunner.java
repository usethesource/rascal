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
import java.net.URISyntaxException;
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
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.TestEvaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * Rascal modules can be tested separatly from each other. This runner includes all modules and nested modules and runs them spread over a workpool
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

    private final IValueFactory VF = ValueFactoryFactory.getValueFactory();
    private Description rootDesc;
    private String rootName;


    public RascalJUnitParallelRecursiveTestRunner(Class<?> clazz) {
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
        rootName = clazz.getName();
        this.prefixes = clazz.getAnnotation(RecursiveRascalParallelTest.class).value();
    }


    @Override
    public Description getDescription() {
        if (rootDesc == null) {
            long start = System.nanoTime();
            fillModuleWorkList();
            long stop = System.nanoTime();
            reportTime("Iterating modules", start, stop);
            startModuleTesters();

            start = System.nanoTime();
            rootDesc = Description.createSuiteDescription(rootName);
            processIncomingModuleDescriptions(rootDesc);
            stop = System.nanoTime();
            reportTime("Importing modules, looking for tests", start, stop);
            assert descriptions.isEmpty();
        }
        return rootDesc;
    }

    @Override
    public void run(RunNotifier notifier) {
        assert rootDesc != null;
        notifier.fireTestRunStarted(rootDesc);
        long start = System.nanoTime();
        runTests(notifier);
        long stop = System.nanoTime();
        reportTime("Testing modules", start, stop);
        notifier.fireTestRunFinished(new Result());
    }

    private void reportTime(String job, long start, long stop) {
        System.out.println(job + ": " + ((stop-start)/1000_000) + "ms");
        System.out.flush();
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
                RascalJUnitTestRunner.getRecursiveModuleList(VF.sourceLocation("std", "", "/" + prefix.replaceAll("::", "/")))
                .stream()
                .map(m -> prefix + "::" + m)
                .forEach(m -> modules.add(m));
            }
            catch (IOException | URISyntaxException e) {
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
                    for (Description mod: testModules) {
                        Listener trl = new Listener(mod);
                        
                        if (mod.getAnnotation(CompilationFailed.class) != null) {
                            trl.report(false, mod.getDisplayName(), URIUtil.correctLocation("unknown", "", ""), "module " + mod.getDisplayName() + " has import/compilation problems", new IllegalArgumentException());
                            continue;
                        }
                        
                        long start = System.nanoTime();
                        TestEvaluator runner = new TestEvaluator(evaluator, trl);
                        runner.test(mod.getDisplayName());
                        long stop = System.nanoTime();
                        long duration = (stop - start) / 1000_000;
                        if (duration > 10_000) {
                            // longer that 10s
                            System.err.println("Testing module " + mod.getClassName() + " took: " + duration + "ms");
                            System.err.flush();
                        }
                        stdout.flush();
                        stderr.flush();
                    }
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
            String module;
            try {
                while ((module = modules.poll()) != null) {
                    try {
                        evaluator.doImport(new NullRascalMonitor(), module);
                    }
                    catch (Throwable e) {
                        synchronized(System.err) {
                            System.err.println("Could not import " + module + " for testing...");
                            System.err.println(e.getMessage());
                            e.printStackTrace(System.err);
                        } 
                        
                        // register a failing module to make sure we report failure later on. 
                        Description modDesc = Description.createTestDescription(getClass(), module, new CompilationFailed() {
                            @Override
                            public Class<? extends Annotation> annotationType() {
                                return getClass();
                            }
                        });

                        testModules.add(modDesc);
                        descriptions.add(modDesc);

                        continue;
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
        }

        private void initializeEvaluator() {
            heap = new GlobalEnvironment();
            root = heap.addModule(new ModuleEnvironment("___junit_test___", heap));

            stderr = new PrintWriter(System.err);
            stdout = new PrintWriter(System.out);
            evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
            evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
            evaluator.getConfiguration().setErrors(true);
        }

    }

    public class Listener implements ITestResultListener {

        private final Description module;

        public Listener(Description module) {
            this.module = module;
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
            // starting a module
        }
        @Override
        public void done() {
            // a module was done
        }

        @Override
        public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable exception) {
            Description desc = getDescription(test, loc);
            results.add(notifier -> {
                notifier.fireTestStarted(desc);

                if (!successful) {
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
