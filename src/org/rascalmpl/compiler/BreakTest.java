package org.rascalmpl.compiler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.TestEvaluator;
import org.rascalmpl.shell.RascalShell;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.test.infrastructure.RascalJUnitTestRunner;

import io.usethesource.vallang.ISourceLocation;

public class BreakTest {

    private static final String BREAKING_MODULE = "lang::rascalcore::check::tests::ChangeScenarioTests";

    public static void main(String[] args) throws IOException {
        RascalShell.setupJavaProcessForREPL();
        
        var term = RascalShell.connectToTerminal();
        var monitor = IRascalMonitor.buildConsoleMonitor(term);
        var error = monitor instanceof PrintWriter ? (PrintWriter) monitor : new PrintWriter(System.err, false);
        try {
            for (int round = 0; round < 100; round++) {
                if (runTest(monitor, error, "round-"+round)) {
                    break;
                }
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

            evaluator.job(name, 2, () -> {
                evaluator.doNextImport(name, BREAKING_MODULE);
                new TestEvaluator(evaluator, new ITestResultListener() {

                    @Override
                    public void start(String context, int count) {
                    }

                    @Override
                    public void report(boolean successful, String test, ISourceLocation loc, String message,
                        Throwable exception) {
                            if (!successful) {
                                failed.set(true);
                                err.println("!!!!! Failed test: " + test);
                                err.println("message: " + message);
                            }
                    }

                    @Override
                    public void ignored(String test, ISourceLocation loc) {
                    }

                    @Override
                    public void done() {
                    }
                }).test();
                return null;
            });
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
