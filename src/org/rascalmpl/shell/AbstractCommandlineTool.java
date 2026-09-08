package org.rascalmpl.shell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.jar.JarURIResolver;
import org.rascalmpl.util.functional.ThrowingConsumer;
import org.rascalmpl.util.functional.ThrowingFunction;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;

/**
 * A reusable setup for any commandline tool in the Rascal project that runs a Rascal `main` function.
 */
public abstract class AbstractCommandlineTool {
    private static IRascalValueFactory vf = IRascalValueFactory.getInstance();

    /**
     * This method should be called by a `public static void main(String[] args)` method directly.
     * It parses the commandline parametres according to the signature of the provided main function name (and module).
     * 
     * @param mainModule     which main module must be imported to begin
     * @param sourceFolders  where to find Rascal source modules to load into the interpreter
     * @param args           the String[] args of the calling static main method
     */
    public static int main(String mainModule, String[] sourceFolders, String[] args, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {
        try {   
            var eval = ShellEvaluatorFactory.getBasicEvaluator(term.reader(), out, err, monitor);
            var rascalJar = JarURIResolver.jarify(PathConfig.resolveCurrentRascalRuntime());

            for (String folder : sourceFolders) {
                var src = URIUtil.getChildLocation(rascalJar, folder);
                if (URIResolverRegistry.getInstance().exists(src)) {
                    eval.addRascalSearchPath(src);
                }
                else {
                    throw new FileNotFoundException(src.toString());
                }
            }

            eval.doImport(monitor, mainModule);
            
            IValue result = eval.main(monitor, mainModule, "main", args);
            
            if (result == null) {
                // void main
                return 0;
            }
            else if (result.getType().isInteger()) {
                return ((IInteger) result).intValue();
            }
            else {
                return 0;
            }
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

    /**
     * A main for which the commandine parameters have already been parsed.
     */
    public static int main(String mainModule, String[] sourceFolders, Map<String, IValue> args, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {
        try {   
            var eval = ShellEvaluatorFactory.getBasicEvaluator(term.reader(), out, err, monitor);
            var rascalJar = JarURIResolver.jarify(PathConfig.resolveCurrentRascalRuntime());

            for (String folder : sourceFolders) {
                var src = URIUtil.getChildLocation(rascalJar, folder);
                if (URIResolverRegistry.getInstance().exists(src)) {
                    eval.addRascalSearchPath(src);
                }
                else {
                    throw new FileNotFoundException(src.toString());
                }
            }

            eval.doImport(monitor, mainModule);
            
            IValue result = eval.main(monitor, mainModule, "main", args);
            
            if (result == null) {
                // void main
                return 0;
            }
            else if (result.getType().isInteger()) {
                return ((IInteger) result).intValue();
            }
            else {
                return 0;
            }
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

    protected static boolean isTrueParameter(Map<String, IValue> args, String arg) {
		return isTrue(args.get(arg));
	}

	protected static IList listParameter(Map<String, IValue> args, String arg) {
		return args.get(arg) == null ? vf.list() : (IList) args.get(arg);
	}

	protected static IInteger intParameter(Map<String, IValue> args, String arg, int def) {
		return args.get(arg) == null ? vf.integer(def) : (IInteger) args.get(arg);
	}

	protected static boolean isTrue(IValue x) {
		return x != null ? ((IBool) x).getValue() : false;
	}

    protected static int parallelAmount(int parallelMax) {
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

    protected static void removeParallelismArguments(Map<String, IValue> parsedArgs) {
		parsedArgs.remove("parallel");
		parsedArgs.remove("parallelMax");
		parsedArgs.remove("parallelPreChecks");
	}

    protected static IList allRascalSourceFiles(IList sourceLocs, IList ignoredLocs) throws IOException {
		var result = vf.listWriter();
		allRascalSourceFiles(sourceLocs, ignoredLocs, result);
		return result.done();
	}

	private static void allRascalSourceFiles(IList sourceLocs, IList ignoredLocs, IListWriter result) throws IOException {
		for (IValue e : sourceLocs) {
            ISourceLocation f = (ISourceLocation) e;
			if (!ignoredLocs.contains(f)) {
				if (URIUtil.getExtension(f).equals("rsc")) {
					result.insert(f);
				}
				else if (URIResolverRegistry.getInstance().isDirectory(f)) {
					allRascalSourceFiles(Arrays.stream(URIResolverRegistry.getInstance().list(f)).collect(vf.listWriter()), ignoredLocs, result);
				}
			}
		}
	}

    protected static IList sourceFilesToModuleNames(IList modules, PathConfig pcfg) throws URISyntaxException {
        var modNames = vf.listWriter();
        for (IValue m : modules) {
            var l = (ISourceLocation) m;
            for (var src: pcfg.getSrcs()) {
                var rel = URIUtil.relativize((ISourceLocation) src, l);
                if (rel.getScheme().equals("relative")) {
                    rel = URIUtil.changeExtension(rel, "");
                    var mod = rel.getPath().substring(1).replaceAll("/", "::");
                    modNames.insert(vf.string(mod));
                }
            }
        }
        return modNames.done();
    }

    protected static List<IList> splitTodoList(int procs, IList modules) {
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

    protected static <T extends IValue> IList toIList(Collection<T> coll) {
		return toList(coll.stream());
	}

	protected static <T extends IValue> IList toList(Stream<T> stream) {
		return stream.collect(vf.listWriter());
	}

    /**
     * Utility function for handling exceptions while streaming. Any checked exception is caught
     * and rethrown as a RuntimeException with the original exception as the cause.
     */
    protected static <T, R, E extends Exception> Function<T, R> handleExceptions(ThrowingFunction<T, R, E> fe) {
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
    protected static <T, E extends Exception> Consumer<T> handleConsumerExceptions(ThrowingConsumer<T, E> fe) {
        return arg -> {
            try {
                fe.accept(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
		};
	}

}
