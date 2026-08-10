package org.rascalmpl.shell;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.streams.StreamUtil;
import org.rascalmpl.test.infrastructure.JUnitXMLReportListener;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;

import engineering.swat.watch.DaemonThreadPool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

/**
 * Runs all the tests in the srcs folders
 * Given a -project parameter, the PathConfig will be constructed automatically
 */
public class RascalTest extends AbstractCommandlineTool {
    private static final IRascalValueFactory vf = IRascalValueFactory.getInstance();

    public static void main(String[] args) {
        try {
            RascalShell.setupJavaProcessForREPL();
            
            var term = RascalShell.connectToTerminal();
            var monitor = IRascalMonitor.buildConsoleMonitor(term);
            var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
            var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);
        
            var parser = new CommandlineParser(out);
            var parsedArgs = parser.parseKeywordCommandLineArgs("RascalTest", args, parameterTypes());  
            var pcfgCons = (IConstructor) parsedArgs.get("pcfg");
            PathConfig pcfg = pcfgCons != null ? new PathConfig(pcfgCons) : new PathConfig();

            var projectRoot = pcfg.getProjectRoot().getScheme().equals("unknown") ? URIUtil.rootLocation("cwd") : pcfg.getProjectRoot();
            boolean reporting = vf.bool(true).equals(parsedArgs.get("reporting"));
            boolean isParallel = isTrueParameter(parsedArgs, "parallel");
            int parAmount = parallelAmount(intParameter(parsedArgs, "parallelMax", 10).intValue());
            IList preChecks = isParallel ? listParameter(parsedArgs, "parallelPreChecks") : vf.list();
            // expand directories in preChecks
            preChecks = allRascalSourceFiles(preChecks, vf.list());
            IList modules = allRascalSourceFiles(pcfg.getSrcs(), pcfg.getIgnores());

            if (isParallel && parAmount > 1) {
                System.exit(runParallelTests(modules, preChecks, monitor, projectRoot, pcfg, term, err, out, reporting, parAmount));
            }
            else {
                System.exit(runTestsForModules(modules, monitor, projectRoot, pcfg, term, err, out, reporting));
            }
        }
        catch (IOException | URISyntaxException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } 
    }

    private static int runParallelTests(IList modules, IList preChecks, IRascalMonitor monitor, ISourceLocation projectRoot,
        PathConfig pcfg, Terminal term, PrintWriter err, PrintWriter out, boolean reporting, int parAmount) throws URISyntaxException {
        // first we run the pre-checks
        if (preChecks.size() > 0) {
            if (runTestsForModules(preChecks, monitor, projectRoot, pcfg, term, err, out, reporting) != 0) {
                return 1;
            }
        }

        // then we split up the module names over a number of runners 
        modules = modules.subtract(preChecks);
        List<IList> chunks = splitTodoList(parAmount, modules);

        // a cachedThreadPool lazily spins-up threads, but eagerly cleans them up
		// this might help with left-over threads to get more memory and finish sooner.
		final ExecutorService exec = DaemonThreadPool.buildConstrainedCached("rascal-test", parAmount);
		
		// the for loop eagerly spawns `parAmount` workers, one for each chunk
		List<Future<Integer>> workers = new ArrayList<>(parAmount);
		for (int i = 0; i < parAmount; i++) {
			final int index = i;
			final var chunk = chunks.get(index);;
			
			workers.add(exec.submit(() -> {
				out.println("Starting worker " + index + " on " + chunk.size() + " modules.");
				return runTestsForModules(chunk, monitor, projectRoot, pcfg, term, err, out, reporting);
			}));
		}
		
		// wait for all the workers and reduce their integer return values to a sum
		return workers.stream()
			.map(handleExceptions(f -> f.get()))
			.reduce(0, Integer::sum);
    }

    /**
     * Thread-safe execution of tests in given modules in a specific project
     * @return exit code where 0 means all test succeeded and not 0 means at least one test failed or error'ed
     */
    private static int runTestsForModules(IList modules, IRascalMonitor monitor, ISourceLocation projectRoot, PathConfig pcfg, Terminal term, PrintWriter err, PrintWriter out, boolean reporting) {
        try {
            var modNames = sourceFilesToModuleNames(modules, pcfg);

            // using our own evaluator makes this thread safe.
            var eval = ShellEvaluatorFactory.getDefaultEvaluatorForPathConfig(projectRoot, pcfg, term.reader(), out, err, monitor);
            if (modNames.size() == 0) {
                eval.warning("The module list for testing is empty.", projectRoot);
            }
            eval.doImport(monitor, modNames.stream().map(IString.class::cast).map(s -> s.getValue()).toArray(String[]::new));

            if (reporting) {
                eval.setTestResultListener(new JUnitXMLReportListener(URIUtil.getChildLocation(projectRoot, "target"), eval.getHeap().moduleFiles()));
            }

            // run only the selected modules and not imported ones to avoid race conditions on the output xml files
            return modNames.stream()
                .map(IString.class::cast)
                .map(modName -> eval.runTests(monitor, Optional.of(modName.getValue())))
                .anyMatch(x -> !x)
                ? 1 : 0;
        }
        catch (Throw e) {
            try {
                err.println(e.getException());
                e.getTrace().prettyPrintedString(err, new StandardTextWriter());
            }
            catch (IOException ioe) {
                err.println(ioe.getMessage());
            }

            return 1;
        }
        catch (Throwable e) {
            e.printStackTrace();
            return 1;
        }
    }

    private static Type parameterTypes() {
		var tf = TypeFactory.getInstance();
		var ll = tf.listType(tf.sourceLocationType());
		
		return tf.tupleType(
			PathConfig.PathConfigType, "pcfg",
            tf.boolType(), "reporting",
            tf.boolType(), "parallel",
            tf.integerType(), "parallelMax",
			ll, "parallelPreChecks"
		);
	}
}
