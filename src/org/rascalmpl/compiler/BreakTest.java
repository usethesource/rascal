package org.rascalmpl.compiler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.shell.RascalShell;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.test.infrastructure.RascalJUnitTestRunner;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.ISourceLocation;

public class BreakTest {

    // private static final String BREAKING_MODULE = "lang::rascalcore::check::tests::ChangeScenarioTests";
    // private static final String BREAKING_TEST = "fixedErrorsDisappear2";
    private static final String BREAKING_MODULE = "lang::rascalcore::check::tests::FunctionTCTests";
    private static final String BREAKING_TEST = "BoundOK1";

    // notCompatibleAfterChangingFunctionArgument
    // notCompatibleAfterChangingFunctionArgument

    private static final int PARALLEL_RUNS = 1; // set to 1 to avoid any multi-threading interactions, but it might take 20 rounds or something
    private static final int TRIES = 1000 / PARALLEL_RUNS;

    public static void main(String[] args) throws IOException, InterruptedException {
        RascalShell.setupJavaProcessForREPL();
        
        var term = RascalShell.connectToTerminal();
        var monitor = IRascalMonitor.buildConsoleMonitor(term);
        var error = monitor instanceof PrintWriter ? (PrintWriter) monitor : new PrintWriter(System.err, false);
        AtomicBoolean failed = new AtomicBoolean(false);
        try {
            AtomicInteger done = new AtomicInteger(0);
            for (int t = 0; t < PARALLEL_RUNS; t++) {
                var name = "Thread " + (t + 1);
                var tr = new Thread(() -> {
                    try {
                        if (crashTestFreshEval(monitor, error, name, failed)) {
                            failed.set(true);
                            error.flush();
                            System.err.println("We got a failure, exiting now!");
                            Thread.sleep(1000);
                        }
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.incrementAndGet();
                    }
                });
                tr.start();
            }
            while (done.get() < PARALLEL_RUNS && !failed.get()) {
                Thread.sleep(100);
            }
        } finally {
            error.flush();
            error.close();
        }
        if (failed.get()) {
            System.out.flush();
            System.err.flush();
            System.exit(1);
        }
    }
    
    static boolean crashTest(IRascalMonitor monitor, PrintWriter errorPrinter, String name, AtomicBoolean failed) {
        var output = new StringWriter();
        var iFailed = new AtomicBoolean(false);
        try (var err = new PrintWriter(output, true); var out= new PrintWriter(output, false)) {
            var projectRoot = RascalJUnitTestRunner.inferProjectRootFromClass(BreakTest.class);
            var evaluator = ShellEvaluatorFactory.getDefaultEvaluatorForLocation(projectRoot, Reader.nullReader(), err, out, monitor, "$test-"+name+"$");
            evaluator.getConfiguration().setErrors(true);
            // make sure we're writing to the outputs
            evaluator.overwritePrintWriter(out, err);

            evaluator.doImport(monitor, BREAKING_MODULE);
            try {
                monitor.job(name, TRIES, (jobname, step) -> {
                    String currentTest = "";
                    var tests = evaluator.getHeap().getModule(BREAKING_MODULE).getTests();
                    try {
                        tests = tests.stream()
                            .filter(t -> !t.hasTag("ignore"))
                            .collect(Collectors.toList());

                        monitor.jobTodo(jobname, tests.size() * TRIES);

                        for (int i = 0; i < TRIES; i++) {
                            if (failed.get()) {
                                return false;
                            }

                            Collections.shuffle(tests);

                            monitor.jobStep(jobname, "Running: try " + (i + 1));
                            for (var t: tests) {
                                currentTest = t.getName();
                                monitor.jobStep(jobname, "Running: try " + (i + 1) + " => " + currentTest);
                                var result = t.call();
                                if (!((IBool)result).getValue()) {
                                    throw new RuntimeException("Test " + currentTest +" returned false");
                                }
                            }
                        }

                    } catch (Throwable e ) {
                        err.println("❌ test fail :" + currentTest);
                        err.println(e);
                        e.printStackTrace(err);
                        iFailed.set(true);
                    }
                    return null;
                });
            } finally {
                // clean up memory
                var memoryModule = evaluator.getHeap().getModule("lang::rascalcore::check::TestConfigs");
                if (memoryModule != null ) {
                var testRoot = memoryModule.getFrameVariable("testRoot");
                try {
                    URIResolverRegistry.getInstance().remove((ISourceLocation)testRoot.getValue(), true);
                }
                catch (Throwable e) {
                    err.println("Failure to cleanup the cache");
                }
            }
            }

        }

        if (iFailed.get()) {
            errorPrinter.println("❌❌❌ Test run failed: " + name);
            errorPrinter.println("Job output:");
            errorPrinter.println(output.toString());
            failed.set(true);
            return true;
        }
        return false;
    }

    static boolean crashTestFreshEval(IRascalMonitor monitor, PrintWriter errorPrinter, String name, AtomicBoolean failed) {
        var output = new StringWriter();
        var iFailed = new AtomicBoolean(false);
        try (var err = new PrintWriter(output, true); var out= new PrintWriter(output, false)) {
            var projectRoot = RascalJUnitTestRunner.inferProjectRootFromClass(BreakTest.class);
            monitor.job(name, TRIES * 2, (jobname, step) -> {
                String currentTest = "";
                var tests = Collections.<AbstractFunction>emptyList();
                    try {
                        for (int i = 0; i < TRIES; i++) {
                            if (failed.get()) {
                                return false;
                            }
                            monitor.jobStep(jobname, "Try " + (i + 1));
                            var evaluator = ShellEvaluatorFactory.getDefaultEvaluatorForLocation(projectRoot, Reader.nullReader(), err, out, monitor, "$test-"+name+"$");
                            try {

                                evaluator.getConfiguration().setErrors(true);
                                // make sure we're writing to the outputs
                                evaluator.overwritePrintWriter(out, err);

                                evaluator.doImport(monitor, BREAKING_MODULE);

                                tests = evaluator.getHeap().getModule(BREAKING_MODULE).getTests()
                                    .stream()
                                    .filter(t -> !t.hasTag("ignore"))
                                    .collect(Collectors.toList());

                                if (i == 0) {
                                    monitor.jobTodo(jobname, tests.size() * TRIES);
                                }

                                Collections.shuffle(tests);

                                monitor.jobStep(jobname, "Running: try " + (i + 1));
                                for (var t: tests) {
                                    currentTest = t.getName();
                                    monitor.jobStep(jobname, "Running: try " + (i + 1) + " => " + currentTest);
                                    var result = t.call();
                                    if (!((IBool)result).getValue()) {
                                        throw new RuntimeException("Test " + currentTest +" returned false");
                                    }
                                }
                            } finally {
                                // clean up memory
                                var memoryModule = evaluator.getHeap().getModule("lang::rascalcore::check::TestConfigs");
                                if (memoryModule != null) {
                                    var testRoot = memoryModule.getFrameVariable("testRoot");
                                    try {
                                        URIResolverRegistry.getInstance().remove((ISourceLocation)testRoot.getValue(), true);
                                    }
                                    catch (Throwable e) {
                                        err.println("Failure to cleanup the cache");
                                    }
                                }
                            }
                        }

                } catch (Throwable e ) {
                    iFailed.set(true);
                    err.println("tests: " + Arrays.deepToString(tests.stream().map(AbstractFunction::getName).toArray()));
                    err.println("❌ test fail :" + currentTest);
                    err.println(e);
                    e.printStackTrace(err);
                }
                return null;
            });
        }

        if (iFailed.get()) {
            errorPrinter.println("❌❌❌ Test run failed: " + name);
            errorPrinter.println("Job output:");
            errorPrinter.println(output.toString());
            errorPrinter.flush();
            failed.set(true);
            return true;
        }
        return false;
    }
}
