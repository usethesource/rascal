package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.streams.StreamUtil;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

/**
 * Commandline tool for Rascal checking and compilation
 */
public class RascalCompile extends AbstractCommandlineTool {
	private static final String mainModule ="lang::rascalcore::check::Checker";
	private static final String[] imports = {"org/rascalmpl/compiler", "org/rascalmpl/typepal"};
	private static final URIResolverRegistry reg = URIResolverRegistry.getInstance();
	private static final IRascalValueFactory vf = IRascalValueFactory.getInstance();

    public static void main(String[] args) throws URISyntaxException, Exception {    
        RascalShell.setupJavaProcessForREPL();
        
        var term = RascalShell.connectToTerminal();
        var monitor = IRascalMonitor.buildConsoleMonitor(term);
        var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
        var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);
           
        var parser = new CommandlineParser(out);
		var kwParams = reproduceCheckerMainParameterTypes();
        
        Map<String,IValue> parsedArgs = parser.parseKeywordCommandLineArgs("RascalCompile", args, kwParams);

		int parAmount = parallelAmount(intParameter(parsedArgs, "parallelMax").intValue());
		IList modules = listParameter(parsedArgs, "modules");
		
		try {
			if (isTrueParameter(parsedArgs, "parallel") || modules.size() <= 10 || parAmount <= 1) {
				System.exit(main(mainModule, imports , args, term, monitor, err, out));
			}
			else {
				System.exit(parallelMain(parsedArgs, parAmount, mainModule, imports , args, term, monitor, err, out));
			}
		}
		catch (Throwable e) {
			// this should have been handled inside main or parallelMain, but to be sure we log any exception here
			// and exit with status code 1, such that no errors can be lost to the calling process (typically `mvn compile`).
			err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
    }

	private static int parallelMain(Map<String, IValue> parsedArgs, int parAmount, String mainModule, String[] imports, String[] args, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {
		var preChecks = listParameter(parsedArgs, "parallelPreChecks");
		IList modules = (IList) parsedArgs.get("modules");

		if (modules.isEmpty()) {
			return 0;
		}

		// first run the pre-checks
		parsedArgs.put("modules", preChecks);
		out.println("Prechecking " + preChecks.size() + " modules ");
		if (main(mainModule, imports, parsedArgs, term, monitor, err, out) != 0) {
			System.exit(1);
		}
		out.println("Precheck is done.");

		// split the remaining work
		modules = modules.subtract(preChecks);
		final var chunks = splitTodoList(parAmount, modules.stream().map(ISourceLocation.class::cast).collect(Collectors.toList()));
		final var bins = chunks.stream()
			.map(handleExceptions(l -> Files.createTempDirectory("rascal-checker")))
			.map(handleExceptions(p -> URIUtil.createFileLocation(p)))
			.collect(vf.listWriter());
		final var bin = locParameter(parsedArgs, "bin");

		// add the bin files of the pre-checks to the libs of each parallel build
		parsedArgs.put("libs", listParameter(parsedArgs, "libs").append(bin));

		// clears the thread and its memory when the work is done, might help left-over workers to run faster.
		final ExecutorService exec = Executors.newCachedThreadPool();
		
		// spawn `parAmount` workers, one for each chunk
		Stream<Future<Integer>> workers = IntStream.range(0, parAmount)
			.mapToObj(i -> exec.submit(() -> {
				var chunk = chunks.get(i);
				// assign the chunk to the 'modules' parameter
				parsedArgs.put("modules", chunk);				
				// assign a private bin folder
				parsedArgs.put("bin", bins.get(i));

				err.println("Starting worker " + i + " on " + chunk.size() + " modules.");
				// run the compiler
				return main(mainModule, imports, parsedArgs, term, monitor, err, out);
			}));
		
			// wait for all the workers and reduce their integer return values to a sum
			var sum = workers
				.map(handleExceptions(f -> f.get()))
				.reduce(0, Integer::sum);

			// copy the resulting binary files to the main bin folder
			bins.stream()
				.map(ISourceLocation.class::cast)
				.forEach(handleConsumerExceptions(b -> reg.copy(b, bin, true, true)));

			return sum;

	}

	private static boolean isTrueParameter(Map<String, IValue> args, String arg) {
		return isTrue(args.get(arg));
	}

	private static IList listParameter(Map<String, IValue> args, String arg) {
		return args.get(arg) == null ? vf.list() : (IList) args.get(arg);
	}

	private static IInteger intParameter(Map<String, IValue> args, String arg) {
		return args.get(arg) == null ? vf.integer(0) : (IInteger) args.get(arg);
	}

	private static ISourceLocation locParameter(Map<String, IValue> args, String arg) {
		return args.get(arg) == null ? URIUtil.unknownLocation() : (ISourceLocation) args.get(arg);
	}

	private static boolean isTrue(IValue x) {
		return x != null ? ((IBool) x).getValue() : false;
	}
	
	/**
	 * Warning: this method has to be kept up-to-date with the commandline parameters of Checkes::main.
	 * @return all the keyword parameters and their types of Checker::main, 
	 * plus these additional ones:
	 * * `int parallelMax = 5`, 
	 * * `bool parallel = false` and 
	 * * `list[loc] parallelPreChecks = []`.
	 */
	private static Type reproduceCheckerMainParameterTypes() {
		var tf = TypeFactory.getInstance();
		var ll = tf.listType(tf.sourceLocationType());
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
			b, "warnUnusedFormals",
			b, "warnUnusedVariables",
			b, "warnUnusedPatternFormals",
			b, "infoModuleChecked",
			b, "errorsAsWarnings", 
			b, "warningsAsErrors",
			b, "parallel",
			i, "parallelMax",
			ll, "parallelPreChecks");
	}

    private static int parallelAmount(int parallelMax) {
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

	/**
	 * Divide number of modules evenly over available cores.
	 * TodoList is sorted to keep modules close that are in the same folder.
	 */
	private static List<IList> splitTodoList(int parAmount, List<ISourceLocation> todoList) {
		todoList.sort((a,b) -> a.getPath().compareTo(b.getPath())); // improves cohesion of a chunk
		int chunkSize = todoList.size() / parAmount;
		List<IList> result = new ArrayList<>();

		for (int from = 0; from <= todoList.size(); from += chunkSize) {
			result.add(toIList(todoList.subList(from, Math.min(from + chunkSize, todoList.size()))));
		}

		return result;
	}

	private static <T extends IValue> IList toIList(Collection<T> coll) {
		return toList(coll.stream());
	}

	private static <T extends IValue> IList toList(Stream<T> stream) {
		return stream.collect(vf.listWriter());
	}

    /**
     * See handleExceptions method
     */
    @FunctionalInterface
	private interface FunctionWithException<T, R, E extends Exception> {
    	R apply(T t) throws E;
	}

	/**
     * See handleExceptions method
     */
    @FunctionalInterface
	private interface ConsumerWithException<T, E extends Exception> {
    	void apply(T t) throws E;
	}

    /**
     * Utility function for handling exceptions while streaming. Any checked exception is caught
     * and rethrown as a RuntimeException with the original exception as the cause.
     */
    private static <T, R, E extends Exception> Function<T, R> handleExceptions(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
		};
	}

	/**
     * Utility function for handling exceptions while streaming. Any checked exception is caught
     * and rethrown as a RuntimeException with the original exception as the cause.
     */
    private static <T, E extends Exception> Consumer<T> handleConsumerExceptions(ConsumerWithException<T, E> fe) {
        return arg -> {
            try {
                fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
		};
	}
}