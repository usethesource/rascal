package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.Reader;
import java.lang.StackWalker.Option;
import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.shell.ShellEvaluatorFactory;

import io.usethesource.vallang.ISourceLocation;

public class ParallelEvaluatorsTests {
    private static final IRascalMonitor monitor = new NullRascalMonitor();
    
    private static final String[] testModules = new String[] {
        "lang::rascal::tests::library::ValueIO",
        "lang::rascal::tests::library::Map",
        "lang::rascal::tests::concrete::Parsing"
    };

    private static Evaluator freshEvaluator() {
        var evaluator = ShellEvaluatorFactory.getBasicEvaluator(Reader.nullReader(), new PrintWriter(System.err, true), new PrintWriter(System.out), monitor, "___test___");
        
        evaluator.setTestResultListener(new ITestResultListener() {
            @Override
            public void start(String context, int count) { 
                
            }

            @Override
            public void report(boolean successful, String test, ISourceLocation loc, String message,
                Throwable exception) {
                
                if (exception != null) {
                    evaluator.warning("Got exception: " + exception, loc);
                    exception.printStackTrace(evaluator.getOutPrinter());
                    throw new RuntimeException(exception);
                }
            }

            @Override
            public void ignored(String test, ISourceLocation loc) { 
                
            }

            @Override
            public void done() {
                
            }
        });
        return evaluator;
    }

    @Test
    public void testMultipleEvaluators() {
        int cores = Math.max(4, Runtime.getRuntime().availableProcessors() / 2);
        var allStarted = new CyclicBarrier(cores + 1);
        var allDone = new CyclicBarrier(cores + 1);
        var result = new AtomicBoolean(true);
        var close = new AtomicBoolean(false);
        var error = new AtomicReference<Exception>(null);
        var currentModule = new AtomicReference<>("");
        for (int i = 0; i < cores; i++) {
            var runner = new Thread(() -> {
                try {
                    while (true) {
                        var evaluator = freshEvaluator();
                        allStarted.await(); // wait for all threads to be at the same point
                        if (close.get()) {
                            return;
                        }
                        var currentTarget = currentModule.get();
                        
                        evaluator.doImport(monitor, currentTarget);
                        if (!evaluator.runTests(monitor, Optional<String>.empty())) {
                            result.set(false);
                        }
                        allDone.await();
                    }
                }
                catch (Exception failure) {
                    error.set(failure);
                    result.set(false);
                }
                finally {
                    try {
                        allDone.await(1, TimeUnit.SECONDS); // just to be sure, if nobody is waiting, we just stop.
                    } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                        //ignore ;
                    }
                }

            }, "Evaluator parallel stress test " + i);
            runner.setDaemon(true);
            runner.start();
        }

        try {
            for (var mod : testModules) {
                currentModule.set(mod);
                allStarted.await();
                try {
                    allDone.await(10, TimeUnit.MINUTES);
                }
                catch (TimeoutException e) {
                    throw new RuntimeException("One of the threads got stuck", error.get());
                }
                assertNull(mod, error.get());
                assertTrue(mod, result.get());
            }
        }
        catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        finally {
            close.set(true);
            monitor.endAllJobs();
        }
    }
    
}
