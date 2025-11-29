package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public static void main(String[] args) throws IOException {    
        RascalShell.setupJavaProcessForREPL();
        var term = RascalShell.connectToTerminal();
		var monitor = IRascalMonitor.buildConsoleMonitor(term);
		var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
		var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);
			
		try {
			var parser = new CommandlineParser(out);
			var kwParams = reproduceCheckerMainParameterTypes();
			var parsedArgs = parser.parseKeywordCommandLineArgs("RascalCompile", args, kwParams);

			System.exit(runMain(parsedArgs, term, monitor, err, out));
		}
		catch (Throwable e) {
			// this should have been handled inside main or parallelMain, but to be sure we log any exception here
			// and exit with status code 1, such that no errors can be lost to the calling process (typically `mvn compile`).
			err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
    }

	private static void removeParallelismArguments(Map<String, IValue> parsedArgs) {
		parsedArgs.remove("parallel");
		parsedArgs.remove("parallelMax");
		parsedArgs.remove("parallelPreChecks");
	}
	
	public static int runMain(Map<String,IValue> parsedArgs, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {
			boolean isParallel = isTrueParameter(parsedArgs, "parallel");
			int parAmount = parallelAmount(intParameter(parsedArgs, "parallelMax").intValue());
			IList modules = listParameter(parsedArgs, "modules");
			IList preChecks = isParallel ? listParameter(parsedArgs, "parallelPreChecks") : vf.list();
			removeParallelismArguments(parsedArgs);

			if (!isParallel || modules.size() <= 5 || parAmount <= 1) {		
				return main(mainModule, imports, parsedArgs, term, monitor, err, out);
			}
			else {
				return parallelMain(parsedArgs, preChecks, parAmount, mainModule, imports , term, monitor, err, out);
			}
	}

	private static int parallelMain(Map<String, IValue> parsedArgs, IList preChecks, int parAmount, String mainModule, String[] imports, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {
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

		// Split the remaining work as evenly as possible
		modules = modules.subtract(preChecks);
		final var chunks = splitTodoList(parAmount, modules);
		final var bins = chunks.stream()
			.map(handleExceptions(l -> Files.createTempDirectory("rascal-checker")))
			.map(handleExceptions(p -> URIUtil.createFileLocation(p)))
			.collect(vf.listWriter());
		final var bin = (ISourceLocation) parsedArgs.get("pcfg").asWithKeywordParameters().getParameter("bin");

		// Copy the pre-checked bin files to the temp bins for incremental reuse.
		// Note there may be more output modules than listed in the preChecks list.
		bins.stream()
			.map(ISourceLocation.class::cast)
			.forEach(handleConsumerExceptions(b -> reg.copy(bin, b, true, true)));

		// a cachedThreadPool lazily spins-up threads, but eagerly cleans them up
		// this might help with left-over threads to get more memory and finish sooner.
		final ExecutorService exec = Executors.newCachedThreadPool();
		
		// the for loop eagerly spawns `parAmount` workers, one for each chunk
		List<Future<Integer>> workers = new ArrayList<>(parAmount);
		for (int i = 0; i < parAmount; i++) {
			final int index = i;
			final var chunk = chunks.get(index);
			final var chunkBin = bins.get(index);
			final Map<String,IValue> chunkArgs = new HashMap<>(parsedArgs);
			chunkArgs.put("modules", chunk);				
			// update pcfg with new our bin folder
			var pcfg = chunkArgs.get("pcfg");
			pcfg = pcfg.asWithKeywordParameters().setParameter("bin", chunkBin);
			chunkArgs.put("pcfg", pcfg);

			workers.add(exec.submit(() -> {
				out.println("Starting worker " + index + " on " + chunk.size() + " modules.");
				return main(mainModule, imports, chunkArgs, term, monitor, err, out);
			}));
		}
		
		// wait for all the workers and reduce their integer return values to a sum
		var sum = workers.stream()
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

	private static List<IList> splitTodoList(int procs, IList modules) {
		List<ISourceLocation> todoList = modules.stream().map(ISourceLocation.class::cast).collect(Collectors.toList());
		todoList.sort((a,b) -> a.getPath().compareTo(b.getPath())); // improves cohesion of a chunk
		int chunkSize = todoList.size() / procs;
		int remainder = todoList.size() % procs;
		List<IList> result = new ArrayList<>((todoList.size() / chunkSize) + 1);

		// Divide the work evenly. The remainder elements are distributed
		// one-by-one over the prefix of the result list.
		for (int from = 0; from < todoList.size(); from += chunkSize + ((remainder-- > 0 ? 1 : 0))) {
			int to = from + chunkSize + ((remainder > 0) ? 1 : 0);
			result.add(toIList(todoList.subList(from, to)));
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