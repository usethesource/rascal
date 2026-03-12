package org.rascalmpl.compiler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.shell.RascalShell;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.test.infrastructure.RascalJUnitTestRunner;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.ISourceLocation;

public class BreakTest {

    private static final String BREAKING_MODULE = "lang::rascalcore::check::tests::ChangeScenarioTests";
    private static final String BREAKING_TEST = "fixedErrorsDisappear";

    private static final int PARALLEL_RUNS = 8; // set to 1 to avoid any multi-threading interactions, but it might take 20 rounds or something

    public static void main(String[] args) throws IOException, InterruptedException {
        RascalShell.setupJavaProcessForREPL();
        
        var term = RascalShell.connectToTerminal();
        var monitor = IRascalMonitor.buildConsoleMonitor(term);
        var error = monitor instanceof PrintWriter ? (PrintWriter) monitor : new PrintWriter(System.err, false);
        try {
            AtomicBoolean failed = new AtomicBoolean(false);
            AtomicInteger done = new AtomicInteger(0);
            for (int t = 0; t < PARALLEL_RUNS; t++) {
                var name = "T" + t;
                var tr = new Thread(() -> {
                    try {
                        monitor.job(name, 100, (jobName, step) -> {
                            for (int round = 0; round < 100; round++) {
                                step.accept("round " + round, 1);
                                if (runTest(monitor, error, jobName +"-round-" + round)) {
                                    failed.set(true);
                                    System.exit(1);
                                    break;
                                }
                                if (failed.get()) {
                                    break;

                                }
                            }
                            return null;
                        });
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
            error.close();
        }
    }
    
    static boolean runTest(IRascalMonitor monitor, PrintWriter errorPrinter, String name) {
        var output = new StringWriter();
        var failed = new AtomicBoolean(false);
        try (var err = new PrintWriter(output, true); var out= new PrintWriter(output, false)) {
            var projectRoot = RascalJUnitTestRunner.inferProjectRootFromClass(BreakTest.class);
            var evaluator = ShellEvaluatorFactory.getDefaultEvaluatorForLocation(projectRoot, Reader.nullReader(), err, out, monitor, "$test-"+name+"$");
            evaluator.getConfiguration().setErrors(true);
            // make sure we're writing to the outputs
            evaluator.overwritePrintWriter(out, err);

            evaluator.doImport(monitor, BREAKING_MODULE);

            try {
                for (int i = 0; i < 10; i++) {
                    evaluator.call(BREAKING_TEST);
                }
            } catch (Throwable e ) {
                failed.set(true);
                err.println("❌ test fail ");
                err.println(e);
            }
            // clean up memory
            var memoryModule = evaluator.getHeap().getModule("lang::rascalcore::check::TestShared");
            var testRoot = memoryModule.getFrameVariable("testRoot");
            try {
                URIResolverRegistry.getInstance().remove((ISourceLocation)testRoot.getValue(), true);
            }
            catch (IOException e) {
            }

        }

        if (failed.get()) {
            errorPrinter.println("❌❌❌ Test run failed: " + name);
            errorPrinter.println("Job output:");
            errorPrinter.println(output.toString());
            return true;
        }
        return false;
    }
}
