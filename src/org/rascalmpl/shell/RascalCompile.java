package org.rascalmpl.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jline.terminal.Terminal;
import org.rascalmpl.ast.Name;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.streams.StreamUtil;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.jar.JarURIResolver;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

/**
 * Commandline tool for Rascal checking and compilation
 */
public class RascalCompile extends AbstractCommandlineTool {
    public static void main(String[] args) throws URISyntaxException, Exception {    
        RascalShell.setupJavaProcessForREPL();
        
        var term = RascalShell.connectToTerminal();
        var monitor = IRascalMonitor.buildConsoleMonitor(term);
        var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
        var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);
           
        var parser = new CommandlineParser(out);
		var kwParams = reproduceCheckerMainParameterTypes();
        
        Map<String,IValue> parsedArgs = parser.parseKeywordCommandLineArgs("RascalCompile", args, kwParams);
        
		RascalCompile m = new RascalCompile(parsedArgs, monitor, err, out, term);

		if (((IBool) parsedArgs.get("parallel")).getValue()) {
		
		}
		else {

		}
    }

    private final Terminal term;
    private final PrintWriter err;
    private final PrintWriter out;
    private final IRascalMonitor monitor;
    
    private RascalCompile(IRascalMonitor monitor, PrintWriter err, PrintWriter out, Terminal term) {
        this.monitor = monitor;
        this.err = err;
        this.out = out;
        this.term = term;
    }

	/**
	 * @return the keyword parameters and their types of Checker::main
	 */
	private static Type reproduceCheckerMainParameterTypes() {
		var tf = TypeFactory.getInstance();
		var ll = tf.listType(tf.sourceLocationType());
		var l = tf.sourceLocationType();
		var b = tf.boolType();
		var i = tf.integerType();

		return tf.tupleType(
			PathConfig.PathConfigType, "pcfg",
			ll, "modules",
			b, "logPathConfig", 
			b, "logImports", 
			b, "verbose",
			b, "logWrittenFiles",
			b, "warnUnused",
			b, "warnUnusedFormals"
			b, "warnUnusedVariables",
			b, "warnUnusedPatternFormals",
			b, "infoModuleChecked",
			b, "errorsAsWarnings", 
			b, "warningsAsErrors",
			b, "parallel",
			i, "parallelMax",
			ll, "parallelPreChecks");
	}

    private int parallelAmount() {
	    // check available CPUs
		long result = Runtime.getRuntime().availableProcessors();
		if (result < 2) {
			return 1;
		}
		// check available memory
		result = Math.min(result, Runtime.getRuntime().maxMemory() / (2 * 1024 * 1024));
		if (result < 2) {
			return 1;
		}
		return (int) Math.min(parallelMax, result);
	}

	private int runChecker(boolean verbose, Map<String, IValue> params)
			throws IOException, URISyntaxException, Exception {
	    if (!parallel || todoList.size() <= 10 || parallelAmount() <= 1) {
	    	return runCheckerSingleThreaded(verbose, todoList, srcLocs, libLocs, binLoc, generatedSourcesLoc);
		}
		else {
			return runCheckerMultithreaded(verbose, todoList, prechecks, srcLocs, libLocs, binLoc, generatedSourcesLoc);
		}
	}

    private void runMain(boolean verbose, List<Path> todoList, List<Path> srcLocs, List<Path> libLocs, Path binLoc, Path generatedSources) {

    }

	private int runCheckerMultithreaded(boolean verbose, List<Path> todoList, List<Path> prechecks, List<Path> srcs, List<Path> libs, Path bin, Path generatedSourcesLoc) throws Exception {
		todoList.removeAll(prechecks);
		List<List<Path>> chunks = splitTodoList(todoList);
		chunks.add(0, prechecks);
		List<Path> tmpBins = chunks.stream().map(handleExceptions(l -> Files.createTempDirectory("rascal-checker"))).collect(Collectors.toList());
		List<Path> tmpGeneratedSources = chunks.stream().map(handleExceptions(l -> Files.createTempDirectory("rascal-sources"))).collect(Collectors.toList());
		int result = 0;

		Map<String,String> extraParameters = Map.of("modules", todoList.stream().map(Object::toString).collect(Collectors.joining(File.pathSeparator)));

		try {
			List<Process> processes = new LinkedList<>();
			Process prechecker = runMain(verbose, chunks.get(0), srcs, libs, tmpGeneratedSources.get(0), tmpBins.get(0), extraParameters, true);

			result += prechecker.waitFor(); // block until the process is finished

			// add the result of this pre-build to the libs of the parallel processors to reuse .tpl files
			libs.add(tmpBins.get(0));

			// starts the processes asynchronously
			for (int i = 1; i < chunks.size(); i++) {
				processes.add(runMain(verbose, chunks.get(i), srcs, libs, tmpGeneratedSources.get(i), tmpBins.get(i), extraParameters, i <= 1));
			}

			// wait until _all_ processes have exited and print their output in big chunks in order of process creation
			for (int i = 0; i < processes.size(); i++) {
				if (i <= 1) {
					// the first process has inherited our IO
					result += processes.get(i).waitFor();
				} else {
					// the other IO we read in asynchronously
					result += readStandardOutputAndWait(processes.get(i));
				}
			}

			// merge the output tpl folders, no matter how many errors have been detected
			mergeOutputFolders(bin, tmpBins);

			// we also merge the generated sources (not used at the moment)
			mergeOutputFolders(generatedSourcesLoc, tmpGeneratedSources);

			if (result > 0) {
				throw new RuntimeException("Checker found errors");
			}

			return result;
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to prepare temporary directories for the checker.");
		}
		catch (InterruptedException e) {
		    throw new RuntimeException("Checker was interrupted");
		}
	}

	private int readStandardOutputAndWait(Process p) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
    		String line;
    		while ((line = reader.readLine()) != null) {
      			System.out.println(line);
    		}

			return p.waitFor();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private int runCheckerSingleThreaded(boolean verbose, List<Path> todoList, List<Path> srcLocs, List<Path> libLocs, Path binLoc, Path generated) throws URISyntaxException, IOException {
		out.println("Running single checker process");
		try {
            main("lang::rascalcore::check::Checker", new String[] {"org/rascalmpl/compiler", "org/rascalmpl/typepal"}, args);
			return runMain(verbose, todoList, srcLocs, libLocs, generated, binLoc, extraParameters, true).waitFor();
		} catch (InterruptedException e) {
			out.println("Checker was interrupted!");
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void mergeOutputFolders(Path bin, List<Path> binFolders) throws IOException {
        String job = "Copying files from to "+ bin;           

        try {
            monitor.jobStart(job);

	    	for (Path tmp : binFolders) {        
			    mergeOutputFolders(bin, tmp);
            }
        }
        finally {
            monitor.jobEnd(job, true);
        }
	}

	private static void mergeOutputFolders(Path dst, Path src) throws IOException {
		Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                Files.createDirectories(dst.resolve(src.relativize(dir).toString()));
                return FileVisitResult.CONTINUE;
            }

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.move(file, dst.resolve(src.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

	/**
	 * Divide number of modules evenly over available cores.
	 * TodoList is sorted to keep modules close that are in the same folder.
	 */
	private List<List<Path>> splitTodoList(List<Path> todoList) {
		todoList.sort(Path::compareTo); // improves cohesion of a chunk
		int chunkSize = todoList.size() / parallelAmount();
		List<List<Path>> result = new ArrayList<>();

		for (int from = 0; from <= todoList.size(); from += chunkSize) {
			result.add(Collections.unmodifiableList(todoList.subList(from, Math.min(from + chunkSize, todoList.size()))));
		}

		return result;
	}

    /**
     * See handleExceptions method
     */
    @FunctionalInterface
	private interface FunctionWithException<T, R, E extends Exception> {
    	R apply(T t) throws E;
	}

    /**
     * Utility function for handling exceptions while streaming. Any checked exception is caught
     * and rethrown as a RuntimeException with the original exception as the cause.
     */
    private <T, R, E extends Exception> Function<T, R> handleExceptions(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
		};
	}
}