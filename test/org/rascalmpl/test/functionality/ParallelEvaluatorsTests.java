package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.repl.TerminalProgressBarMonitor;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import jline.TerminalFactory;

public class ParallelEvaluatorsTests {
    private static final TerminalProgressBarMonitor monitor = new TerminalProgressBarMonitor(System.out, TerminalFactory.get());
    
    private static final String[] testModules = new String[] {
        "lang::rascal::tests::library::ValueIO",
        "lang::rascal::tests::library::Map",
        "lang::rascal::tests::concrete::Parsing"
    };

    private static Evaluator freshEvaluator() {
        var heap = new GlobalEnvironment();
        var root = heap.addModule(new ModuleEnvironment("___test___", heap));
        
        var evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), System.in, System.err, monitor,  root, heap);
        evaluator.setMonitor(monitor);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());        
        evaluator.setTestResultListener(new ITestResultListener() {
            private String context;

            @Override
            public void start(String context, int count) { 
                this.context = context;
                monitor.jobStart(context, count);
            }

            @Override
            public void report(boolean successful, String test, ISourceLocation loc, String message,
                Throwable exception) {
                monitor.jobStep(context, "Run " + test, 1);
                if (exception != null) {
                    evaluator.warning("Got exception: " + exception, loc);
                    exception.printStackTrace(evaluator.getOutPrinter());
                    throw new RuntimeException(exception);
                }
            }

            @Override
            public void ignored(String test, ISourceLocation loc) { 
                monitor.jobStep(context,"Ignored " + test, 1);
            }

            @Override
            public void done() {
                monitor.jobEnd(context, true);
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
                        if (!evaluator.runTests(monitor)) {
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

            });
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
        }



    }
    
}
