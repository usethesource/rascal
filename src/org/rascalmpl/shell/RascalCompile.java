package org.rascalmpl.shell;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.streams.StreamUtil;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.util.Lazy;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.ITuple;
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

	public static int runMain(Map<String,IValue> parsedArgs, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) throws IOException, URISyntaxException {
			boolean isParallel = isTrueParameter(parsedArgs, "parallel");
			int parAmount = parallelAmount(intParameter(parsedArgs, "parallelMax", 10).intValue());
		    boolean parallelGraphBased = isTrueParameter(parsedArgs, "parallelGraphBased");
			IList modules = listParameter(parsedArgs, "modules");
			IList preChecks = isParallel ? listParameter(parsedArgs, "parallelPreChecks") : vf.list();
			preChecks = allRascalSourceFiles(preChecks, vf.list());
			removeParallelismArguments(parsedArgs);

			if (!isParallel || modules.size() <= 5 || parAmount <= 1) {
				return main(mainModule, imports, parsedArgs, term, monitor, err, out);
			}
			else {
				return parallelScheduler(parsedArgs, preChecks, parallelGraphBased, parAmount, mainModule, imports , term, monitor, err, out);
			}
	}


	private static Set<ModuleDependencyNode> calculateModuleDependencies(
	        IList modules, IList preChecks, IConstructor pcfg, int parAmount, boolean useImportGraph,
			Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) throws IOException {
		var eval = initializeEvaluator("CalculateOrder", imports, term, monitor, err, out);
		var order = run(() -> (ISet)eval.call(monitor, "calculateModuleDependencies", modules, pcfg), vf.set(), err);
		if (useImportGraph) {
    		var result = new HashMap<ISet, ModuleDependencyNode>();
    		for (var e: order) {
    			var from = (ISet)(((ITuple)e).get(0));
    			var to = (ISet)(((ITuple)e).get(1));
    			if (!from.isEmpty()) {
    				var nodeFrom = result.computeIfAbsent(from, ModuleDependencyNode::new);
    				if (!to.isEmpty()) {
    					var nodeTo = result.computeIfAbsent(to, ModuleDependencyNode::new);
    					nodeFrom.addDependency(nodeTo);
    				}
    			}
    		}
    		return new HashSet<>(result.values());
		}
		else {
		    var sizes = calculateFileSizes(order);
			var preChecksSet = preChecks.stream().collect(vf.setWriter());
			var chunks = run(() -> (IList)eval.call(monitor, "chunkGraph", order, preChecksSet, sizes, vf.integer(parAmount)), vf.list(), err);
			var result = chunks.stream()
				.map(ISet.class::cast)
				.map(c -> new ModuleDependencyNode(c, sizes))
				.collect(Collectors.toCollection(HashSet::new));
			if (!preChecksSet.isEmpty()) {
				var preCheck = new ModuleDependencyNode(preChecksSet);
				result.forEach(n -> n.addDependency(preCheck));
				result.add(preCheck);
			}
			return result;
		}
	}

	private static IMap calculateFileSizes(ISet order) throws IOException {
		var sizes = vf.mapWriter();
		for (var e: order) {
			var from = (ISet)(((ITuple)e).get(0));
			var to = (ISet)(((ITuple)e).get(1));
			var newFiles = from.union(to).stream()
				.map(ISourceLocation.class::cast).filter(f -> sizes.get(f) == null)
				.collect(Collectors.toSet());
			for (var f : newFiles) {
				sizes.put(f, vf.integer(reg.size(f)));
			}
		}
		return sizes.done();
	}

	private static double log2(double number) {
		return Math.log(number) / Math.log(2);

	}
	private static class ModuleDependencyNode {
		private final IList myModules;
		private final List<ModuleDependencyNode> dependants = new ArrayList<>();
		private final Set<ModuleDependencyNode> dependencies = ConcurrentHashMap.newKeySet();
		private final long fileSize;


		public ModuleDependencyNode(ISet myModules) {
			this(myModules, vf.map());

		}
		public ModuleDependencyNode(ISet myModules,IMap sizes) {
			this.myModules = myModules.stream().collect(vf.listWriter());
			this.fileSize = myModules.stream()
				.map(ISourceLocation.class::cast)
				.mapToLong(file -> {
					var result = sizes.get(file);
					if (result instanceof IInteger) {
						return ((IInteger)result).longValue();
					}
					// otherwise fallback to the io lookup
					try {
						return reg.size(file);
					}
					catch (IOException e) {
						return 0;
					}
				})
				.sum();
		}

		public ModuleDependencyNode addDependency(ModuleDependencyNode on) {
			dependencies.add(on);
			on.addDependant(this);
			return this;
		}
		private ModuleDependencyNode addDependant(ModuleDependencyNode depender) {
			dependants.add(depender);
			return this;
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


		public int fileSizeBucket() {
			return (int)Math.round(log2(fileSize));

		}
		public int upstreamSize() {
			return dependants.size();
		}
	}

	private static class ChunkRunner {
		private final ISourceLocation commonBin;
		private final ISourceLocation bin;
		private final IConstructor pcfg;
		private final BlockingQueue<ModuleDependencyNode> tasks;
		private final Queue<ModuleDependencyNode> completed;
		private final Queue<Integer> doneRunning;
		private final AtomicInteger active;

		public ChunkRunner(IConstructor pcfg, BlockingQueue<ModuleDependencyNode> tasks, Queue<ModuleDependencyNode> completed, Queue<Integer> doneRunning, AtomicInteger active) throws URISyntaxException, IOException {
			this.tasks = tasks;
			this.completed = completed;
			this.doneRunning = doneRunning;
			this.active= active;
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
				var eval = Lazy.defer(() -> {
					try {
						return initializeEvaluator(mainModule, imports, term, monitor, err, out);
					}
					catch (IOException e) {
					    throw new RuntimeException(e);
					}
				});
				while (true) {
					var newTask = tasks.take();
					if (newTask.size() == 0) {
						// we should stop
						return;
					}
					// as we've just build a full import graph
					// we need to filter out modules that were not requested to build
					// we cannot do this earlier, as it might be in the middle of a chain of dependencies that we do not want to miss
					var modules = newTask.getModules().intersect((IList)originalArguments.get("modules"));
					if (modules.isEmpty()) {
						// we do not have to do anything, as the module never needed to be checked
						completed.add(newTask);
						continue;
					}

					try {
						active.incrementAndGet();
						var chunkArgs = new HashMap<>(originalArguments);
						chunkArgs.put("modules", modules);
						chunkArgs.put("pcfg", this.pcfg);
						result += runMain(eval.get(), mainModule, "main", chunkArgs, monitor, err);
						promoteTpls();
					} finally {
						active.decrementAndGet();
					}
					completed.add(newTask);
				}
			} catch(Throwable ex) {
				result++;
				err.println("The compiler crashed: " + ex.getMessage());
				err.println(ex);
				ex.printStackTrace(err);
				err.flush();
			} finally {
				doneRunning.add(result);
			}
		}


	}

	private static int parallelScheduler(Map<String, IValue> parsedArgs, IList preChecks, boolean parallelGraphBased, int parAmount, String mainModule, String[] imports, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) throws URISyntaxException, IOException {
		var modules = (IList) parsedArgs.get("modules");
		if (modules.isEmpty()) {
			return 0;
		}
		var pcfg = (IConstructor)parsedArgs.get("pcfg");

		monitor.jobStart("Calculate dependency graph");
		var moduleDependencyGraph = calculateModuleDependencies(modules, preChecks, pcfg, parAmount, parallelGraphBased, term, monitor, err, out);
		monitor.jobEnd("Calculate dependency graph", true);

		// we order the work based on how many upstream modules depend on it
		// in that way we optimize for the most work impactfull modules first
		var tasks = new PriorityBlockingQueue<>(parAmount,
			Comparator.comparingInt(ModuleDependencyNode::fileSizeBucket).reversed()
				.thenComparing(Comparator.comparingInt(ModuleDependencyNode::upstreamSize).reversed()));
		var finished = new LinkedBlockingQueue<ModuleDependencyNode>();
		var done = new LinkedBlockingQueue<Integer>();
		var activeRunners = new AtomicInteger(0);

		// startup the runners
		for (int c = 0; c < parAmount; c++) {
			var runner = new ChunkRunner(pcfg, tasks, finished, done, activeRunners);
			var t = new Thread(() -> runner.start(mainModule, imports, parsedArgs, term, monitor, err, out), "Rascal Compiler " + (c + 1));
			t.setDaemon(true);
			t.start();
		}


		// start scheduling work for the runners
		return monitor.job("Compiling modules in parallel", moduleDependencyGraph.size(), (name, step) -> {
			// start with all the leafs
			for (var mod : moduleDependencyGraph) {
				if (mod.readyForChecking()) {
					tasks.add(mod);
				}
			}

			// we keep track of the crashed to know if all runners are "out"
			int crashed = 0;
			while (!moduleDependencyGraph.isEmpty()) {
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
								tasks.add(u);
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
					err.println("One of the compiler crashed early");
					err.flush();
					crashed++;
				}
				if (crashed == parAmount || (activeRunners.get() == 0 && !moduleDependencyGraph.isEmpty())) {
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
			b, "parallelGraphBased",
			i, "parallelMax",
			ll, "parallelPreChecks");
	}
}
