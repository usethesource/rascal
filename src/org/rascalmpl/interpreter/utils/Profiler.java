/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ideservices.GsonUtils;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.utils.Profiler.Fmt;
import org.rascalmpl.interpreter.utils.Sampler.Labeled;
import org.rascalmpl.interpreter.utils.Sampler.Sample;
import org.rascalmpl.semantics.dynamic.Statement.Expression;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.semantics.dynamic.Expression.CallOrTree;
import org.rascalmpl.semantics.dynamic.Expression.QualifiedName;
import org.rascalmpl.semantics.dynamic.QualifiedName.Default;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeStore;

import org.rascalmpl.values.ValueFactoryFactory;

class Count {
	int ticks;
	
	Count(){
		ticks = 1;
	}
	
	public void increment(){
		ticks += 1;
	}
	
	public int getTicks(){
		return ticks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ticks;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Count other = (Count) obj;
		if (ticks != other.ticks) {
			return false;
		}
		return true;
	}
}

public class Profiler extends Thread {
	private Evaluator eval;
	private volatile boolean running;
	private long resolution = 1;
	private final Map<ISourceLocation,Count> ast;
	private final Map<ISourceLocation, Count> frame;
	private final Map<ISourceLocation, String> names;
	private final Sampler sampler;
	
	public Profiler(Evaluator ev){
		super("Rascal-Sampling-Profiler");
		this.eval = ev;
		this.ast = new HashMap<>();
		this.frame = new HashMap<>();
		this.names = new HashMap<>();
		running = true;

		Sampler sampler = new DummySampler();
		if (!(ev.getConfiguration().getProfileFmtProperty() instanceof DummyFormatter)) {
			try {
				sampler = new TempFileSampler(ev);
			} catch (IOException e) {
				// Problem with the temp file, so resort to dummy sampler.
			}
		}
		this.sampler = sampler;
	}
	
	@Override
	public void run(){
		while(running) {
			AbstractAST current = eval.getCurrentAST();
			Environment env = eval.getCurrentEnvt();
			String name = env.getName();
			
			if (current != null) {
				sampler.addSample();

				ISourceLocation stat = current.getLocation();
				if(stat != null){
					Count currentCount = ast.get(stat);
					if (currentCount == null) {
						ast.put(stat, new Count());
						names.put(stat, name);
					} else {
						currentCount.increment();
					}
				}
					while (env.getParent() != null && !env.getParent().isRootScope() && !env.isFunctionFrame()) {
						env = env.getParent();
					}
				if (env != null) {
					Count currentCount = frame.get(env.getCreatorLocation());
					if (currentCount == null) {
						frame.put(env.getCreatorLocation(), new Count());
						names.put(env.getCreatorLocation(), env.getName());
					}
					else {
						currentCount.increment();
					}
				}
			}
			try {
				sleep(resolution);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void pleaseStop(){
		running = false;
	}
	
	/* Extract a list of entries from the collected data and
	 * sort it with descending tick values.
	 */
	
	private List<Map.Entry<ISourceLocation, Count>> sortData(Map<ISourceLocation,Count> data) {
	  List<Map.Entry<ISourceLocation, Count>> sortedData = new Vector<Entry<ISourceLocation, Count>>(data.entrySet());

	  java.util.Collections.sort(sortedData, new Comparator<Map.Entry<ISourceLocation, Count>>(){
	    public int compare(Entry<ISourceLocation, Count> entry1, Entry<ISourceLocation, Count> entry2) {
	      return ((entry1.getValue().getTicks() == entry2.getValue().getTicks()) ? 0 : 
	        (entry1.getValue().getTicks() < entry2.getValue().getTicks() ? 1 : -1));
	    }
	  });
	  return sortedData;
	}
	
	public IList getProfileData(){
		IValueFactory VF = ValueFactoryFactory.getValueFactory();
		IListWriter w = VF.listWriter();
		for(Map.Entry<ISourceLocation, Count> e : sortData(ast)){
			w.insert(VF.tuple(e.getKey(), VF.integer(e.getValue().getTicks())));
		}
		return w.done();
	}
	
	public void report() {
		report("FRAMES", frame);
		eval.getOutPrinter().println();
		report("ASTS", ast);
		eval.getOutPrinter().println();
		reportFormatted();
	}
	
	private void report(String title, Map<ISourceLocation, Count> data) {
	  List<Map.Entry<ISourceLocation, Count>> sortedData = sortData(data);

	  int maxName = 1;
	  long nTicks = 0;

	  for(Map.Entry<ISourceLocation, Count> e : sortedData){
	    int sz = names.get(e.getKey()).length();
	    if(sz > maxName) {
	      maxName = sz;
	    }
	    nTicks += e.getValue().getTicks();
	  }
	  
	  PrintWriter out = eval.getOutPrinter();
	  String nameFormat = "%" + maxName + "s";
	  out.printf(title + " PROFILE: %d data points, %d ticks, tick = %d milliSecs\n", ast.size(), nTicks, resolution);
	  out.printf(nameFormat + "%8s%9s  %s\n", " Scope", "Ticks", "%", "Source");

	  for (Map.Entry<ISourceLocation, Count> e : sortedData) {
	    String L = e.getKey().toString();
	    String name = names.get(e.getKey());
	    
	    int ticks = e.getValue().getTicks();
	    double perc = (ticks * 100.0)/nTicks;
	    
	    if (perc < 1.0) {
	    	break;
	    }
	    
	    String source = String.format("%s", L);

	    out.printf(nameFormat + "%8d%8.1f%%  %s\n", name, ticks, perc, source);
	  }
	  
	  // Make sure that our output is seen:
	  out.flush();
	}

	private void reportFormatted() {
		if (sampler != null) {
			// Prepare `dateTime` to put in the file name
			var dateTime = Instant.now().atZone(ZoneId.systemDefault()).toString();
			dateTime = dateTime.replaceAll("[:\\-]", "");
			dateTime = dateTime.substring(0, 15);
			dateTime = "-" + dateTime;
			
			// Prepare `hint` to put in the file name. It is the name of the root funciton call (if any).
			var hint = "";
			var ast = eval.getCurrentAST();
			if (ast instanceof Expression) {
				ast = ((Expression) ast).hasExpression() ? ((Expression) ast).getExpression() : ast;
			}
			if (ast instanceof CallOrTree) {
				ast = ((CallOrTree) ast).hasExpression() ? ((CallOrTree) ast).getExpression() : ast;
			}
			if (ast instanceof QualifiedName) {
				ast = ((QualifiedName) ast).hasQualifiedName() ? ((QualifiedName) ast).getQualifiedName() : ast;
			}
			if (ast instanceof Default) {
				hint = ((Default) ast).lastName();
				hint = hint.replaceAll("[\\\\]", "");
				hint = "-" + hint;
			}

			// Compute the path to the file
			var fmt = eval.getConfiguration().getProfileFmtProperty();
			var dir = Path.of(eval.getConfiguration().getProfileDirProperty());
			var file = dir.resolve("profile" + dateTime + hint + "." + fmt.getFormatExtension());

			// Compute the content of the file
			var formatted = sampler.getSamplesFormatted(fmt::format);
			sampler.close();

			// Write to file
			String message = null;
			try {
				Files.writeString(file, formatted);
				message = URIUtil.createFileLocation(file).toString();
			} catch (URISyntaxException | IOException e) {
				message = String.format("Failed to write data (%s)", file, e);
				e.printStackTrace();
			}

			// Print to out
			var out = eval.getOutPrinter();
			out.printf("FORMATTED (%s): %s\n", fmt.getFormatName().toUpperCase(), message);
			out.flush();
		}
	}

	public static Fmt FMT_NONE = new DummyFormatter();
	public static Fmt FMT_FOLDED = new FoldedFormatter();
	public static Fmt FMT_CPUPROFILE = new CpuprofileFormatter();

	/**
	 * Formatter of samples
	 */
	public static interface Fmt {
	    public String format(Stream<Sample> samples);

		public String getFormatName();

		public String getFormatExtension();
	}
}

/**
 * Representation of a sampler
 */
interface Sampler extends Closeable {

	public void close();

	public void addSample();

	public String getSamplesFormatted(Function<Stream<Sample>, String> formatter);

	/**
	 * Representation of a sample. Each sample consists of the time it was taken in nanoseconds and a representation of
	 * the call stack at that time.
	 */
	public static class Sample {
		public final long nanoTime;
		public final List<Labeled<ISourceLocation>> stack;

		public Sample(long nanoTime, List<Labeled<ISourceLocation>> stack) {
			assert !stack.isEmpty();
			this.nanoTime = nanoTime;
			this.stack = stack;
		}

		public boolean isInitializer() {
			var label = stack.get(0).label;
			return Objects.equals("kwp initializer", label) ||
				Objects.equals("keyword parameter initializer", label);
		}

		public IValue toIValue(IValueFactory vf) {
			var list = vf.listWriter();
			for (var frame : stack) {
				list.append(frame.toIValue(vf));
			}
			return vf.tuple(vf.integer(nanoTime), list.done());
		}

		public static Sample of(IValue v) {
			var nanoTime = ((IInteger) ((ITuple) v).get(0)).longValue();
			var stack = ((IList) ((ITuple) v).get(1)).stream()
				.map(Labeled::<ISourceLocation> of)
				.collect(Collectors.toList());
			return new Sample(nanoTime, stack);
		}

		public static Sample of(Evaluator evaluator) {
			var nanoTime = System.nanoTime();
			var stack = new ArrayList<Labeled<ISourceLocation>>();

			// Instead of calling `evaluator.getStackTrace`, the following bit of custom code to traverse the stack of
			// environments is useful to immediately get the stack trace in the right iteration order (bottom-to-top
			// instead of top-to-bottom).
			var e = evaluator.getCurrentEnvt();
			while (e != null) {
				stack.add(0, new Labeled<>(locationOf(e), functionNameOf(e)));
				e = e.getCallerScope();
			}

			return stack.isEmpty() ? null : new Sample(nanoTime, stack);
		}

		private static ISourceLocation locationOf(Environment e) {
			var location = e.getCallerLocation();
			return location == null ? URIUtil.unknownLocation() : location;
		}

		private static String functionNameOf(Environment e) {
			// Each call frame in a stack trace of the interpreter is itself represented as a linked list of
			// environments. To get the "right" name of a call frame (e.g., for the purpose of constructing a flame
			// chart), we need to find the name of the *penultimate* environment in the list (as the final environment
			// represents the module), unless the call frame concerns an anonymous function.
			var name = e.getName();
			var parent = e.getParent();
			if (Objects.equals("Anonymous Function", name)) {
				return name + " (" + e.getCreatorLocation() + ")"; 
			} else if (parent == null || parent == e.getRoot() || e.isFunctionFrame()) {
				return name;
			} else {
				return functionNameOf(parent);
			}
		}
	}

	/**
	 * Representation of a labeled `IValue`
	 */
	public static class Labeled<T extends IValue> {
		public final T value;
		public final String label;
		
		public Labeled(T value, String label) {
			this.value = value;
			this.label = label;
		}

		public IValue toIValue(IValueFactory vf) {
			return vf.tuple(value, vf.string(label));
		}

		@SuppressWarnings("unchecked")
		public static <T extends IValue> Labeled<T> of(IValue v) {
			var value = (T) ((ITuple) v).get(0);
			var label = ((IString) ((ITuple) v).get(1)).getValue();
			return new Labeled<T>(value, label);
		}
	}
}

/**
 * Dummy sampler that does nothing
 */
class DummySampler implements Sampler {

	@Override
	public void close() {
	}

	@Override
	public void addSample() {
	}

	@Override
	public String getSamplesFormatted(Function<Stream<Sample>, String> formatter) {
		return "N/A";
	}
}

/**
 * Sampler for the provided evaluator that temporarily stores samples on disk to avoid excessive memory usage (using an
 * auxiliary thread that encodes, writes, reads, and decodes, to avoid stalling the main thread).
 */
class TempFileSampler implements Sampler {
	private final Evaluator evaluator;
	private final TempFile<Sample> tempFile;

	public TempFileSampler(Evaluator evaluator) throws IOException {
		this.evaluator = evaluator;
		this.tempFile = new TempFile<>(
			// Configure the temporary file handle to store samples on disk as base 64-encoded `IValue`s
			decoded -> GsonUtils.base64Encode(decoded.toIValue(evaluator.getValueFactory())),
			encoded -> Sample.of(GsonUtils.<IValue> base64Decode(encoded, new TypeStore()))
		);
	}

	@Override
	public void close() {
		tempFile.close();
	}

	public void addSample() {
		var sample = Sample.of(evaluator);
		if (sample == null) {
			return;
		}
		tempFile.writeLine(sample);
	}

	public String getSamplesFormatted(Function<Stream<Sample>, String> formatter) {
		// The formatter runs on a lazily populated stream of samples, lazily read from the temporary file, to avoid the
		// need to temporarily keep all samples in an auxiliary collection.
		return tempFile.readLines().thenApply(samples -> {
			var filtered = samples.filter(Predicate.not(Sample::isInitializer)); // Avoid a bit of sample pollution
			var formatted = formatter.apply(filtered);
			samples.close();
			return formatted;
		}).join();
	}
}

/**
 * Handle for a freshly created temporary file, internally coupled with a freshly created auxiliary thread dedicated to:
 * (1) encoding and writing objects of type {@code T} to the temporary file; (2) reading and decoding objects.
 */
class TempFile<T> implements Closeable {
	private final ExecutorService executor;
	private final Path path;
	private final BufferedWriter writer;
	private final Function<T, String> encode;
	private final Function<String, T> decode;

	public TempFile(Function<T, String> encode, Function<String, T> decode) throws IOException {
		this.executor = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
		this.path = Files.createTempFile("", "");
		this.writer = Files.newBufferedWriter(path);
		this.encode = encode;
		this.decode = decode;
	}

	@Override 
	public void close() {
		schedule(() -> {
			writer.close();
			Files.delete(path);
			return null;
		});

		// Shutdown and await termination (or an interrupt)
		try {
			executor.shutdown();
			while (!executor.awaitTermination(100, TimeUnit.MILLISECONDS));
			return;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public CompletableFuture<Stream<T>> readLines() {
		return schedule(() -> {
			writer.flush();
			var reader = Files.newBufferedReader(path);
			var values = reader.lines().map(decode); 

			values.onClose(() -> {
				try {
					reader.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				} 
			});
			
			return values;
		});
	}

	public void writeLine(T value) {
		schedule(() -> {
			writer.write(encode.apply(value));
			writer.newLine();
			return null;
		});
	}

	private <U> CompletableFuture<U> schedule(Callable<U> c) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return c.call();
			} catch (Exception e) {
				throw new CompletionException(e);
			}
		}, executor);
	}
}

/**
 * Dummy formatter that does nothing
 */
class DummyFormatter implements Fmt {

	@Override 
	public String format(Stream<Sample> samples) {
		return "N/A";
	}

	@Override 
	public String getFormatName() {
		return "N/A";
	}

	@Override
	public String getFormatExtension() {
		return "txt";
	}
}

/**
 * Formatter for the {@code .folded} format of Brendan Gregg's original `flamegraph.pl` script. Files written in this
 * format can be post-processed by that script to SVG.
 * 
 * @see https://github.com/brendangregg/flamegraph
 */
class FoldedFormatter implements Fmt {

	@Override 
	public String format(Stream<Sample> samples) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override 
	public String getFormatName() {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public String getFormatExtension() {
		return "folded";
	}
}

/**
 * Formatter for the {@code .cpuprofile} format of the Chrome Dev Tools (CDT) protocol. Files written in this format can
 * be opened, for instance, in VS Code using the "Flame Chart Visualizer for JavaScript Profiles" extension, or in
 * Chrome using the built-in developer tools.
 *
 * @see https://chromedevtools.github.io/devtools-protocol/tot/Profiler/
 */
class CpuprofileFormatter implements Fmt {

	@Override
	public String format(Stream<Sample> samples) {
		return Profile.of(samples).format(0);
	}

	@Override
	public String getFormatName() {
		return "Cpuprofile";
	}

	@Override
	public String getFormatExtension() {
		return "cpuprofile";
	}

	/**
	 * Representation of a profile in the format of the Chrome DevTools protocol
	 *
	 * @see https://chromedevtools.github.io/devtools-protocol/tot/Profiler/#type-Profile
	 */
	public static class Profile {
		public final Set<ProfileNode> nodes; // CDT protocol
		public final long startTime; // CDT protocol
		public final long endTime; // CDT protocol
		public final List<Integer> samples; // CDT protocol
		public final List<Long> timeDeltas; // CDT protocol

		public Profile(Set<ProfileNode> nodes, long startTime, long endTime, List<Integer> samples, List<Long> timeDeltas) {
			this.nodes = nodes;
			this.startTime = startTime;
			this.endTime = endTime;
			this.samples = samples;
			this.timeDeltas = timeDeltas;
		}

		public String format(int tabs) {
			var b = new StringBuilder();
			appendln(b, tabs, "{");
			appendln(b, tabs + 1, "\"nodes\": [");
			appendln(b, tabs + 2, nodes.stream().map(n -> n.format(tabs + 2).stripTrailing()).collect(Collectors.joining("," + System.lineSeparator())).strip());
			appendln(b, tabs + 1, "],");
			appendln(b, tabs + 1, "\"startTime\": " + startTime + ",");
			appendln(b, tabs + 1, "\"endTime\": " + endTime + ",");
			appendln(b, tabs + 1, "\"samples\": " + samples + ",");
			appendln(b, tabs + 1, "\"timeDeltas\": " + timeDeltas);
			appendln(b, tabs, "}");
			return b.toString();
		}

		public static Profile of(Stream<Sample> rawSamples) {
			var nodes = new LinkedHashSet<ProfileNode>(); // Iterable by insertion order
			var startTime = 0L;
			var endTime = 0L;
			var samples = new ArrayList<Integer>();
			var timeDeltas = new ArrayList<Long>();

			// Auxiliary variables to help construct a tree representation of the call stacks in `rawSamples` (prefix
			// sharing), rooted at dummy node `root`, with ids generated by `nextId`, as required by the CDT protocol.
			var nextId = 0;
			var rootNode = new ProfileNode(nextId++, null);
			var prevTime = 0L;
			var currTime = 0L;

			var iterator = rawSamples.iterator();
			while (iterator.hasNext()) {
				var rawSample = iterator.next();
				prevTime = currTime;
				currTime = rawSample.nanoTime / 1000; // Microseconds

				// Update `nodes` by inserting a node into the tree representation for each frame of the call stack
				var node = rootNode;
				for (var rawFrame : rawSample.stack) {
					var child = new ProfileNode(nextId++, CallFrame.of(rawFrame));
					node = node.addChildIfAbsent(child);
					nodes.add(node);
				}

				// Update `startTime` and `endTime`
				if (startTime == 0L) {
					startTime = currTime;
					prevTime = currTime;
				}
				if (!iterator.hasNext()) {
					endTime = currTime;
				}

				// Update `samples` and `timeDeltas`
				samples.add(node.id); // Id of the top frame of the call stack
				timeDeltas.add(currTime - prevTime);
			}

			return new Profile(nodes, startTime, endTime, samples, timeDeltas);
		}
	}

	/**
	 * Representation of a profile node in the format of the Chrome DevTools protocol
	 *
	 * @see https://chromedevtools.github.io/devtools-protocol/tot/Profiler/#type-ProfileNode
	 */
	private static class ProfileNode {
		public final int id; // CDT protocol
		public final CallFrame callFrame; // CDT protocol
		public final List<Integer> children; // CDT protocol

		// Internal map to keep track of the (non-)existence of child nodes for call frames. Needs to be kept consistent
		// with `children`.
		private final Map<CallFrame, ProfileNode> childNodes = new LinkedHashMap<>();

		public ProfileNode(int id, CallFrame callFrame) {
			this.id = id;
			this.callFrame = callFrame;
			this.children = new ArrayList<>();
		}

		public ProfileNode addChildIfAbsent(ProfileNode child) {
			var put = childNodes.putIfAbsent(child.callFrame, child);
			if (put == null) {
				children.add(child.id); // Keep consistent
				return child;
			} else {
				return put;
			}
		}

		public String format(int tabs) {
			var b = new StringBuilder();
			appendln(b, tabs, "{");
			appendln(b, tabs + 1, "\"id\": " + id + ",");
			appendln(b, tabs + 1, "\"callFrame\": " + callFrame.format(tabs + 1).strip() + ",");
			appendln(b, tabs + 1, "\"children\": " + children);
			appendln(b, tabs, "}");
			return b.toString();
		}
	}

	/**
	 * Representation of a call frame in the format of the Chrome DevTools protocol
	 *
	 * @see https://chromedevtools.github.io/devtools-protocol/tot/Runtime/#type-CallFrame
	 */
	public static class CallFrame {
		public final String functionName; // CDT protocol
		public final String scriptId; // CDT protocol
		public final String url; // CDT protocol
		public final int lineNumber; // CDT protocol (0-based)
		public final int columnNumber; // CDT protocol (0-based)

		public CallFrame(String functionName, String scriptId, String url, int lineNumber, int columnNumber) {
			this.functionName = functionName;
			this.scriptId = scriptId;
			this.url = url;
			this.lineNumber = lineNumber;
			this.columnNumber = columnNumber;
		}

		@Override
		public int hashCode() {
			return functionName.hashCode() + scriptId.hashCode() + url.hashCode() + lineNumber + columnNumber;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof CallFrame) {
				var frame = (CallFrame) obj;
				return Objects.equals(functionName, frame.functionName) &&
					Objects.equals(scriptId, frame.scriptId) &&
					Objects.equals(url, frame.url) &&
					lineNumber == frame.lineNumber &&
					columnNumber == frame.columnNumber;
			}
			return false;
		}

		public String format(int tabs) {
			var b = new StringBuilder();
			appendln(b, tabs, "{");
			appendln(b, tabs + 1, "\"functionName\": \"" + functionName + "\",");
			appendln(b, tabs + 1, "\"scriptId\": \"" + scriptId + "\",");
			appendln(b, tabs + 1, "\"url\": \"" + url + "\",");
			appendln(b, tabs + 1, "\"lineNumber\": " + lineNumber + ",");
			appendln(b, tabs + 1, "\"columnNumber\": " + columnNumber + "");
			appendln(b, tabs, "}");
			return b.toString();
		}

		public static CallFrame of(Labeled<ISourceLocation> rawCallFrame) {
			var functionName = rawCallFrame.label;
			var scriptId = "";
			var url = "";
			var lineNumber = -1;
			var columnNumber = -1;

			var location = rawCallFrame.value;
			if (location != null && location.hasLineColumn()) {
				scriptId = location.toString();
				url = location.getURI().toString(); 
				lineNumber = location.getBeginLine() - 1;
				columnNumber = location.getBeginColumn();
			}

			return new CallFrame(functionName, scriptId, url, lineNumber, columnNumber);
		}
	}

	private static StringBuilder appendln(StringBuilder b, int tabs, String s) {
		b.append(" ".repeat(tabs * 2));
		b.append(s);
		b.append(System.lineSeparator());
		return b;
	}
}
