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
import io.usethesource.vallang.IList;
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


	private static Set<ModuleDependencyNode> calculateModuleDependencies(IList modules, IConstructor pcfg, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out, int parAmount) throws IOException {
		var eval = initializeEvaluator("CalculateOrder", imports, term, monitor, err, out);
		var order = run(() -> (ISet)eval.call(monitor, "calculateModuleDependencies", modules, pcfg, vf.integer(parAmount)), vf.set(), err);
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

	private static double log2(double number) {
		return Math.log(number) / Math.log(2);

	}
	private static class ModuleDependencyNode {
		private final IList myModules;
		private final List<ModuleDependencyNode> dependants = new ArrayList<>();
		private final Set<ModuleDependencyNode> dependencies = ConcurrentHashMap.newKeySet();
		private final long fileSize;

		public ModuleDependencyNode(ISet myModules) {
			this.myModules = myModules.stream().collect(vf.listWriter());
			this.fileSize = myModules.stream()
				.map(ISourceLocation.class::cast)
				.mapToLong(value -> {
					try {
						return reg.size(value);
					}
					catch (IOException e) {
						return 0;
					}
				})
				.sum();
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

		public ChunkRunner(IConstructor pcfg, BlockingQueue<ModuleDependencyNode> tasks, Queue<ModuleDependencyNode> completed, Queue<Integer> doneRunning) throws URISyntaxException, IOException {
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
				var eval = Lazy.defer(() -> {
					try {
						return initializeEvaluator(mainModule, imports, term, monitor, err, out);
					} catch (IOException e) {
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
					
					var chunkArgs = new HashMap<>(originalArguments);
					chunkArgs.put("modules", modules);
					chunkArgs.put("pcfg", this.pcfg);
					var start = System.currentTimeMillis();
					result += runMain(eval.get(), mainModule, "main", chunkArgs, monitor, err);
					var took = System.currentTimeMillis() - start;
					if (took > (10 * 1000)) {
						err.println("*** ⌛ Type checking: " + modules + " took " + String.format("%.1f", took / 1000.) + "s");
						err.flush();
					}
					promoteTpls();
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

	private static int parallelScheduler(Map<String, IValue> parsedArgs, IList preChecks, int parAmount, String mainModule, String[] imports, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) throws URISyntaxException, IOException {
		var modules = (IList) parsedArgs.get("modules");
		if (modules.isEmpty()) {
			return 0;
		}
		var pcfg = (IConstructor)parsedArgs.get("pcfg");

		monitor.jobStart("Calculate dependency graph");
		var moduleDependencyGraph = calculateModuleDependencies(modules, pcfg, term, monitor, err, out, parAmount);
		monitor.jobEnd("Calculate dependency graph", true);

		// we order the work based on how many upstream modules depend on it
		// in that way we optimize for the most work impactfull modules first
		var tasks = new PriorityBlockingQueue<>(parAmount,
			Comparator.comparingInt(ModuleDependencyNode::fileSizeBucket).reversed()
				.thenComparing(Comparator.comparingInt(ModuleDependencyNode::upstreamSize).reversed()));
		var finished = new LinkedBlockingQueue<ModuleDependencyNode>();
		var done = new LinkedBlockingQueue<Integer>();

		// startup the runners
		for (int c = 0; c < parAmount; c++) {
			var runner = new ChunkRunner(pcfg, tasks, finished, done);
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