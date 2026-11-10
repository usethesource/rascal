package org.rascalmpl.shell;

import engineering.swat.watch.DaemonThreadPool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.streams.StreamUtil;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.jar.JarURIResolver;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.impl.persistent.SetWriter;
import io.usethesource.vallang.io.StandardTextWriter;
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
	
	public static int runMain(Map<String,IValue> parsedArgs, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) throws IOException, URISyntaxException {
			boolean isParallel = isTrueParameter(parsedArgs, "parallel");
			int parAmount = parallelAmount(intParameter(parsedArgs, "parallelMax", 10).intValue());
			IList modules = listParameter(parsedArgs, "modules");
			IList preChecks = isParallel ? listParameter(parsedArgs, "parallelPreChecks") : vf.list();
			preChecks = allRascalSourceFiles(preChecks, vf.list());
			removeParallelismArguments(parsedArgs);

			if (!isParallel || modules.size() <= 5 || parAmount <= 1) {		
				return main(mainModule, imports, parsedArgs, term, monitor, err, out);
			}
			else {
				return parallelScheduler(parsedArgs, preChecks, parAmount, mainModule, imports , term, monitor, err, out);
			}
	}


	private static Set<ModuleDependencyNode> calculateModuleDependencies(IList modules, IConstructor pcfg, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) throws IOException {
		var eval = initializeEvaluator("CalculateOrder", imports, term, monitor, err, out);
		var order = run(() -> (ISet)eval.call(monitor, "calculateModuleDependencies", modules, pcfg), vf.set(), err);
		var result = new HashMap<ISet, ModuleDependencyNode>();
		for (var e: order) {
			var from = (ISet)(((ITuple)e).get(0));
			var to = (ISet)(((ITuple)e).get(1));
			var nodeFrom = result.computeIfAbsent(from, ModuleDependencyNode::new);
			var nodeTo = result.computeIfAbsent(to, ModuleDependencyNode::new);
			nodeFrom.addDependency(nodeTo);
		}
		return new HashSet<>(result.values());
	}
	private static class ModuleDependencyNode {
		private final IList myModules;
		private final List<ModuleDependencyNode> dependants = new ArrayList<>();
		private final Set<ModuleDependencyNode> dependencies = ConcurrentHashMap.newKeySet();

		public ModuleDependencyNode(ISet myModules) {
			this.myModules = myModules.stream().collect(vf.listWriter());
		}

		public void addDependency(ModuleDependencyNode on) {
			dependencies.add(on);
			on.addDependant(this);
		}
		private void addDependant(ModuleDependencyNode depender) {
			dependants.add(depender);
		}

		public void finishedChecking(ModuleDependencyNode node) {
			dependencies.remove(node);
		}

		public boolean readyForChecking() {
			return dependencies.isEmpty();
		}

		public IList getModules() {
			return myModules;
		}

		public int size() {
			return myModules.size();
		}

		public int upstreamSize() {
			return dependants.size();
		}
	}

	private static class Runner {
		private final ISourceLocation commonBin;
		private final ISourceLocation bin;
		private final IConstructor pcfg;
		private final BlockingQueue<ModuleDependencyNode> tasks;
		private final BlockingQueue<ModuleDependencyNode> completed;
		private final Queue<Integer> doneRunning;

		public Runner(IConstructor pcfg, BlockingQueue<ModuleDependencyNode> tasks, BlockingQueue<ModuleDependencyNode> completed, Queue<Integer> doneRunning) throws URISyntaxException, IOException {
			this.tasks = tasks;
			this.completed = completed;
			this.doneRunning = doneRunning;
			this.bin = URIUtil.createFileLocation(Files.createTempDirectory("rascal-checker"));
			var libs = (IList) pcfg.asWithKeywordParameters().getParameter("libs");
			if (libs == null) {
				libs = vf.list();
			}
			this.commonBin = (ISourceLocation)pcfg.asWithKeywordParameters().getParameter("bin");
			this.pcfg = pcfg.asWithKeywordParameters()
				.setParameter("libs", libs.append(commonBin))
				.asWithKeywordParameters()
				.setParameter("bin", this.bin);
		}

		private void promoteTpls() throws IOException {
			// move everything from our own bin into the common bin
			var from = reg.resolveIfLogical(bin);
			var to = reg.resolveIfLogical(commonBin);
			if (from.getScheme().equals("file") && to.getScheme().equals("file")) {
				// fast path, we can move instead of copy
				var src = Paths.get(from.getURI());
				var dst = Paths.get(to.getURI());
				Files.walkFileTree(src, new SimpleFileVisitor<Path>() {

					private Path toTarget(Path cur) {
						return dst.resolve(src.relativize(cur));
					}
					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						// create target directory if it does not exist
						Files.createDirectories(toTarget(dir));
						return super.preVisitDirectory(dir, attrs);
					}
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.move(file, toTarget(file), StandardCopyOption.REPLACE_EXISTING);
						return super.visitFile(file, attrs);
					}
				});
			}
			else {
				reg.copy(from, to, true, true);
			}
		}

		void start(String mainModule, String[] imports, Map<String, IValue> originalArguments, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {
			int result = 0;
			try {
				var eval = initializeEvaluator(mainModule, imports, term, monitor, err, out);
				while (true) {
					var newTask = tasks.take();
					if (newTask.size() == 0) {
						// we should stop
						return;
					}
					var chunkArgs = new HashMap<>(originalArguments);
					chunkArgs.put("modules", newTask.getModules());
					chunkArgs.put("pcfg", this.pcfg);
					result = runMain(eval, mainModule, "main", chunkArgs, monitor, err);
					if (result != 0) {
						return;
					}
					promoteTpls();
					completed.add(newTask);
				}
			} catch(Throwable ex) {
				result++;
				err.println("The compiler crashed: " + ex.getMessage());
				err.println(ex);
				ex.printStackTrace(err);
			} finally {
				doneRunning.add(result);
			}
		}
		

	}

	private static int parallelScheduler(Map<String, IValue> parsedArgs, IList preChecks, int parAmount, String mainModule, String[] imports, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) throws URISyntaxException, IOException {
		var modules = (IList) parsedArgs.get("modules");
		if (modules.isEmpty()) {
			return 0;
		}
		var pcfg = (IConstructor)parsedArgs.get("pcfg");

		monitor.jobStart("Calculate dependency graph");
		var moduleDependencyGraph = calculateModuleDependencies(modules, pcfg, term, monitor, err, out);
		monitor.jobEnd("Calculate dependency graph", true);

		var tasks = new LinkedBlockingQueue<ModuleDependencyNode>();
		var finished = new LinkedBlockingQueue<ModuleDependencyNode>();
		var done = new LinkedBlockingQueue<Integer>();

		// startup the runners
		for (int c = 0; c < parAmount; c++) {
			var runner = new Runner(pcfg, tasks, finished, done);
			var t = new Thread(() -> runner.start(mainModule, imports, parsedArgs, term, monitor, err, out), "compiler-" + c);
			t.setDaemon(true);
			t.start();
		}

		// start scheduling work for the runners
		var readyForPickup = moduleDependencyGraph.stream()
			.filter(ModuleDependencyNode::readyForChecking)
			.sorted(Comparator.comparingInt(ModuleDependencyNode::upstreamSize))
			.collect(Collectors.toCollection(ArrayList::new));

		return monitor.job("Compiling modules in parallel", moduleDependencyGraph.size(), (name, step) -> {
			int crashed = 0;
			while (!moduleDependencyGraph.isEmpty()) {
				// process new work ready for pickup
				if (!readyForPickup.isEmpty()) {
					readyForPickup.sort(Comparator.comparingInt(ModuleDependencyNode::upstreamSize));
					tasks.addAll(readyForPickup);
					readyForPickup.clear();
				}

				// we now watch for finished jobs
				// but sometimes they can crash, so detect that as well
				// especially if all of them have crashed
				ModuleDependencyNode jobDone;
				try {
					while ((jobDone = finished.poll(100, TimeUnit.MILLISECONDS)) != null) {
						moduleDependencyGraph.remove(jobDone);
						for (var u: jobDone.dependants) {
							u.finishedChecking(jobDone);
							if (u.readyForChecking()) {
								readyForPickup.add(u);
							}
						}
						step.accept("", 1);
					}
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				while (done.poll() != null) {
					// early exits
					crashed++;
				}
				if (crashed == parAmount) {
					err.println("All parallel compilers crashed, exiting");
					err.flush();
					return crashed;
				}
			}
			// we're done
			// lets send the empty lists as a way to signal we're done
			for (int c = 0; c < parAmount; c++) {
				tasks.add(new ModuleDependencyNode(vf.set()));
			}

			// now we wait for them to finish
			// which can take quite a while
			for (int c = crashed; c < parAmount; c++) {
				try {
					crashed += done.take();
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			return crashed;
		});
	}



	private static int parallelMain(Map<String, IValue> parsedArgs, IList preChecks, int parAmount, String mainModule, String[] imports, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) throws URISyntaxException, IOException {
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
		out.flush();

		// Split the remaining work as evenly as possible
		modules = modules.subtract(preChecks);
		final var chunks = splitTodoList(parAmount, modules);
		final var bins = chunks.stream()
			.map(handleExceptions(l -> Files.createTempDirectory("rascal-checker")))
			.map(handleExceptions(p -> URIUtil.createFileLocation(p)))
			.collect(vf.listWriter());
		final var bin = (ISourceLocation) parsedArgs.get("pcfg").asWithKeywordParameters().getParameter("bin");

		out.println("After prelude:");
		printTpls(bin, out);
		out.flush();

		// Copy the pre-checked bin files to the temp bins for incremental reuse.
		// Note there may be more output modules than listed in the preChecks list.
		bins.stream()
			.map(ISourceLocation.class::cast)
			.forEach(handleConsumerExceptions(b -> reg.copy(bin, b, true, true)));

		// a cachedThreadPool lazily spins-up threads, but eagerly cleans them up
		// this might help with left-over threads to get more memory and finish sooner.
		final ExecutorService exec = DaemonThreadPool.buildConstrainedCached("rascal-compile", parAmount);

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
			.forEach(handleConsumerExceptions(b -> {
				printTpls(b, out);
 				reg.copy(b, bin, true, true);
			}));

		out.flush();

		return sum;

	}

	private static void printTpls(ISourceLocation root, PrintWriter out) {
		try {
			var st = IRascalValueFactory.getInstance().setWriter();
			printTpls(root, root, st);
			new StandardTextWriter(false).write(st.done(), out);
			out.flush();
		}
		catch (IOException e) {
		}
	}
	private static void printTpls(ISourceLocation root, ISourceLocation current, ISetWriter out) throws IOException {
		var reg = URIResolverRegistry.getInstance();
		for (var e: reg.list(current)) {
			if (reg.isFile(e)) {
				if (URIUtil.getExtension(e).equals("tpl")) {
					out.append(URIUtil.relativize(root, e));
				}
			}
			else {
				printTpls(root, e, out);
			}
		}
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
}