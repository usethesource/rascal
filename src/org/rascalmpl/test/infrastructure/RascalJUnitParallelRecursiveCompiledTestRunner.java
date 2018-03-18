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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.library.experiments.Compiler.Commands.Rascal;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.TestExecutor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.lang.rascal.boot.IKernel;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.uptr.IRascalValueFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class RascalJUnitParallelRecursiveCompiledTestRunner extends Runner {

    private static final int NUMBER_OF_WORKERS = Math.max(1, Math.min(4,Math.min(Runtime.getRuntime().availableProcessors() - 1,  (int)(Runtime.getRuntime().maxMemory()/ 1024*1024*450))));
    private static final IValueFactory VF = IRascalValueFactory.getInstance();

    private final PathConfig pcfg;
    private final Map<String, Integer> testsPerModule = new ConcurrentHashMap<>();
    
    private final String[] prefixes;

    private final String rootName;
    private Description rootDesc = null;

    private final Deque<String> modules = new ConcurrentLinkedDeque<>();
    private final Deque<Description> descriptionsGenerated = new ConcurrentLinkedDeque<>();
    private final Deque<Description> workList = new ConcurrentLinkedDeque<>();
    private final Deque<Consumer<RunNotifier>> results = new ConcurrentLinkedDeque<>();

    private final Semaphore importsCompleted = new Semaphore(0);
    private final CyclicBarrier waitForRunning = new CyclicBarrier(NUMBER_OF_WORKERS + 1);
    private final Semaphore workersCompleted = new Semaphore(0);
    
    public RascalJUnitParallelRecursiveCompiledTestRunner(Class<?> clazz) {
        System.out.println("Running parallel test with " + NUMBER_OF_WORKERS + " runners");
        System.out.flush();
        rootName = clazz.getName();
       
        this.prefixes = clazz.getAnnotation(RecursiveRascalParallelTest.class).value();
        for (int i = 0; i < prefixes.length; i++) {
            prefixes[i] = prefixes[i].replaceAll("\\\\", "");
        }
        
        try {
            pcfg = RascalJUnitCompiledTestRunner.initializePathConfig();
            pcfg.addLibLoc(URIUtil.correctLocation("project", "rascal", "bin"));
        }
        catch (IOException e) {
            throw new RuntimeException("Project rascal should exists", e);
        }
    }

    @Override
    public synchronized Description getDescription() {
        if (rootDesc == null) {
            long start = System.nanoTime();
            fillModuleWorkList();
            long stop = System.nanoTime();
            reportTime("Iterating modules", start, stop);
            startModuleImporters();

            start = System.nanoTime();
            rootDesc = Description.createSuiteDescription(rootName);
            processIncomingModuleDescriptions(rootDesc);
            stop = System.nanoTime();
            reportTime("Importing/typececking modules, looking for tests", start, stop);
            assert descriptionsGenerated.isEmpty();
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

    private void runTests(RunNotifier notifier) {
        startModuleTesters();
        try {
            waitForRunning.await();
        }
        catch (InterruptedException | BrokenBarrierException e1) {
            throw new RuntimeException(e1);
        }
        int completed = 0;
        while (completed < NUMBER_OF_WORKERS || !results.isEmpty()) {
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

    private static void reportTime(String job, long start, long stop) {
        System.out.println(job + ": " + ((stop-start)/1000_000) + "ms");
        System.out.flush();
    }

    private void processIncomingModuleDescriptions(Description rootDesc) {
        int completed = 0;
        List<Description> received = new ArrayList<>();
        while (completed < NUMBER_OF_WORKERS || !descriptionsGenerated.isEmpty()) {
            try {
                if (importsCompleted.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                    completed++;
                }
            }
            catch (InterruptedException e) {
            }
            // consume stuff in the current queue
            Description newDescription;
            while ((newDescription = descriptionsGenerated.poll()) != null) {
                rootDesc.addChild(newDescription);
                received.add(newDescription);
            }
        }
        Collections.shuffle(received);
        workList.addAll(received);
    }

    private void startModuleImporters() {
        for (int i = 0; i < NUMBER_OF_WORKERS; i++) {
            new ModuleImporter("JUnit Rascal Compiled Importer" + (i + 1)).start();
        }
    }
    private void startModuleTesters() {
        for (int i = 0; i < NUMBER_OF_WORKERS; i++) {
            new ModuleTester("JUnit Rascal Compiled Runner " + (i + 1)).start();
        }
        
    }

    private void fillModuleWorkList() {
        for (String prefix: prefixes) {
            String prefixLoc = "/" + prefix.replaceAll("::", "/");
            List<String> newModules = new ArrayList<>();

            for (IValue loc : pcfg.getSrcs()) {
                try {
                    RascalJUnitCompiledTestRunner.getRecursiveModuleList(URIUtil.getChildLocation((ISourceLocation) loc,  prefixLoc), newModules);
                }
                catch (IOException  e) {
                }
            }
            
            Collections.shuffle(newModules); // make sure to spread out the work a bit, so that the workers are not always checking out the same directory
            
            newModules.stream()
                .map(m -> ((prefix.isEmpty() ? "" : prefix + "::") + m))
                .forEach(m -> modules.add(m)); ;
        }
    }

    public class ModuleImporter extends Thread {
        private IKernel kernel;

        public ModuleImporter(String name) {
            super(name);
        }
        
        @Override
        public void run() {
            initializeKernel();
            processModules();
        }

        
        private void processModules() {
            try {
                URIResolverRegistry resolver = URIResolverRegistry.getInstance();

                String qualifiedName;
                while ((qualifiedName = modules.poll()) != null) {
                    try {
                        Description modDesc = RascalJUnitCompiledTestRunner.createModuleDescription(resolver, qualifiedName, kernel, pcfg);
                        if (modDesc != null) {
                            descriptionsGenerated.add(modDesc);
                            if (modDesc.getAnnotation(CompilationFailed.class) == null) {
                                testsPerModule.put(qualifiedName, modDesc.getChildren().size());
                            }
                        }
                    }
                    catch (URISyntaxException e) {
                        System.err.println("Error creating module desc for " + qualifiedName);
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                importsCompleted.release();
            }
        }


        private void initializeKernel() {
            try {
                // kernel loading is not thread safe, yet
                synchronized (IKernel.class) {
                    kernel = Java2Rascal.Builder.bridge(VF, new PathConfig(), IKernel.class)
                        .trace(false)
                        .profile(false)
                        .verbose(false)
                        .build();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }  

    }

    private class ModuleTester extends Thread {

        public ModuleTester(String name) {
            super(name);
        }
        
        @Override
        public void run() {
            try {
                waitForRunning.await();

                RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(pcfg).build();
                Description mod;
                while ((mod = workList.poll()) != null) {
                    long start = System.nanoTime();
                    // START HERE!

                    if (mod.getAnnotation(CompilationFailed.class) != null) {
                        Failure failed = new Failure(mod, new IllegalArgumentException(mod.getDisplayName() + " had compilation errors"));
                        results.push(notifier -> notifier.fireTestFailure(failed));
                        continue;
                    }

                    ISourceLocation binary = null;
                    RVMCore rvmCore = null;

                    try {
                        binary = Rascal.findBinary(pcfg.getBin(), mod.getDisplayName());
                        rvmCore = ExecutionTools.initializedRVM(binary, rex);
                    } catch (IOException e1) {
                        Failure failed = new Failure(mod, e1);
                        results.push(notifier -> notifier.fireTestFailure(failed));
                        continue;
                    }

                    Listener listener = new Listener(mod);
                    TestExecutor runner = new TestExecutor(rvmCore, listener, rex);
                    try {
                        runner.test(mod.getDisplayName(), testsPerModule.get(mod.getClassName())); 
                        rvmCore.getStdOut().flush();
                        rvmCore.getStdErr().flush();
                        listener.done();
                    } 
                    catch (Throwable e) {
                        // Something went totally wrong while running the compiled tests, force all tests in this suite to fail.
                        System.err.println("RascalJunitCompiledTestrunner.run: " + mod.getMethodName() + " unexpected exception: " + e.getMessage());
                        e.printStackTrace(System.err);
                        Failure failed = new Failure(mod, e);
                        results.push(notifier -> notifier.fireTestFailure(failed));
                        continue;
                    }
                    long stop = System.nanoTime();
                    long duration = (stop - start) / 1000_000;
                    if (duration > 10_000) {
                        // longer that 10s
                        System.err.println("Testing module " + mod.getClassName() + " took: " + duration + "ms");
                        System.err.flush();
                    }
                }
            }
            catch (InterruptedException | BrokenBarrierException e2) {
                throw new RuntimeException(e2);
            }
            finally {
                workersCompleted.release();
            }
        }
    }

    @Override
    public int testCount(){
        getDescription();
        return testsPerModule.values().stream().mapToInt(i -> i).sum();
    }



    private final class Listener implements ITestResultListener {
        private final Description module;

        private Listener(Description module) {
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
            Description desc = getDescription(test, loc);
            results.add(notifier -> notifier.fireTestIgnored(desc));
        }

        @Override
        public void start(String context, int count) {
            results.add(notifier -> notifier.fireTestRunStarted(module));
        }

        @Override
        public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable t) {
            Description desc = getDescription(test, loc);
            results.add(notifier -> {
                notifier.fireTestStarted(desc);

                if (!successful) {
                    notifier.fireTestFailure(new Failure(desc, t != null ? t : new AssertionError(message == null ? "test failed" : message)));
                }
                else {
                    notifier.fireTestFinished(desc);
                }
            });
        }

        @Override
        public void done() {
            results.add(notifier -> notifier.fireTestRunFinished(new Result()));
        }
    }
}
