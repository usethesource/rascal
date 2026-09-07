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

import java.io.IOException;
import java.io.PrintWriter;
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
import java.util.Vector;
import java.util.stream.Collectors;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
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

/**
 * Representation of a flame graph for a single profile, populated with raw
 * samples while the profiler is running. To take a raw sample, call
 * {@link FlameGraph#tick(Evaluator)}; it takes a snapshot of the current call
 * stack of the provided evaluator and stores it in memory. To write the flame
 * graph, call {@link FlameGraph#write()}; it converts the raw samples in memory
 * into the {@code .cpuprofile} format of the Chrome Dev Tools protocol and
 * writes them to disk. The resulting file can be opened, for instance, in VS
 * Code using the "Flame Chart Visualizer for JavaScript Profiles" extension, or
 * in Chrome using the built-in developer tools.
 *
 * @see https://chromedevtools.github.io/devtools-protocol/tot/Profiler/
 */
class FlameGraph {
	private final List<Tick> ticks = new ArrayList<>();

	public void tick(Evaluator evaluator) {
		var tick = Tick.of(evaluator);
		if (tick != null) {
			ticks.add(tick);
		}
	}

	public Path write() {
		var dateTime = Instant.now().atZone(ZoneId.systemDefault()).toString();
		dateTime = dateTime.replaceAll("[:\\-]", "");
		dateTime = dateTime.substring(0, 15);
		dateTime = "-" + dateTime;
		
		var hint = ""; // Name of root function call (i.e., second call frame of the first stack trace)
		if (!ticks.isEmpty() && ticks.get(0).stackTrace.size() > 1) {
			hint = ticks.get(0).stackTrace.get(1).functionName; // Skip first call frame (`$`)
			hint = hint.replaceAll("[\\\\]", "");
			hint = "-" + hint;
		}

		try {
			var path = Path.of("rascal" + dateTime + hint + ".cpuprofile");
			var csq = Profile.of(ticks).toJson(0);
			Files.writeString(path, csq);
			return path;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Representation of a raw sample
	 */
	public static class Tick {
		public final long timestamp; // Microseconds
		public final List<CallFrame> stackTrace;

		public Tick(long timestamp, List<CallFrame> stackTrace) {
			assert !stackTrace.isEmpty();
			this.timestamp = timestamp;
			this.stackTrace = stackTrace;
		}

		public boolean isInitializer() {
			var functionName = stackTrace.get(0).functionName;
			return Objects.equals("kwp initializer", functionName) ||
				Objects.equals("keyword parameter initializer", functionName);
		}

		public static Tick of(Evaluator evaluator) {
			var timeStamp = System.nanoTime() / 1000;
			var stackTrace = new ArrayList<CallFrame>();

			// Instead of calling `evaluator.getStackTrace`,The following bit of
			// custom code to traverse the stack of environments is useful to
			// immediately get the stack trace in the right iteration order
			// (bottom-to-top instead of top-to-bottom).
			var e = evaluator.getCurrentEnvt();
			while (e != null) {
				stackTrace.add(0, CallFrame.of(e));
				e = e.getCallerScope();
			}

			return stackTrace.isEmpty() ? null : new Tick(timeStamp, stackTrace);
		}

		public static long timeDelta(Tick early, Tick late) {
			return (early == null || late == null) ? 0 : (late.timestamp - early.timestamp);
		}

		public static List<Long> timeDeltas(List<Tick> ticks) {
			var timeDeltas = new ArrayList<Long>();
			for (var i = 0; i < ticks.size(); i++) {
				var early = i == 0 ? null : ticks.get(i - 1);
				var late = ticks.get(i);
				timeDeltas.add(timeDelta(early, late));
			}
			return timeDeltas;
		}
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

		public String toJson(int tabs) {
			var b = new StringBuilder();
			appendln(b, tabs, "{");
			appendln(b, tabs + 1, "\"nodes\": [");
			appendln(b, tabs + 2, nodes.stream().map(n -> n.toJson(tabs + 2).stripTrailing()).collect(Collectors.joining("," + System.lineSeparator())).strip());
			appendln(b, tabs + 1, "],");
			appendln(b, tabs + 1, "\"startTime\": " + startTime + ",");
			appendln(b, tabs + 1, "\"endTime\": " + endTime + ",");
			appendln(b, tabs + 1, "\"samples\": " + samples + ",");
			appendln(b, tabs + 1, "\"timeDeltas\": " + timeDeltas);
			appendln(b, tabs, "}");
			return b.toString();
		}

		public static Profile of(List<Tick> ticks) {
			ticks.removeIf(Tick::isInitializer); // Avoid a bit of pollution
			assert !ticks.isEmpty();
			
			var nodes = new LinkedHashSet<ProfileNode>(); // Iterable by insertion order
			var startTime = ticks.get(0).timestamp;
			var endTime = ticks.get(ticks.size() - 1).timestamp;
			var samples = new ArrayList<Integer>();
			var timeDeltas = Tick.timeDeltas(ticks);

			// Initialize `nodes` and `samples` by converting lists of call
			// frames (stack traces) to a more efficient tree representation
			// (prefix sharing), rooted at `root`, required by the CDT protocol.
			var root = new ProfileNode(null); // Dummy root node
			for (var tick : ticks) {

				// Update the tree representation by iteratively adding children
				// to nodes for the list of call frames (stack trace)
				var node = root;
				for (var callFrame : tick.stackTrace) {
					node = node.addChildIfAbsent(callFrame);
					nodes.add(node);
				}

				// Register the last call frame (top of the stack trace)
				samples.add(node.id);
			}

			return new Profile(nodes, startTime, endTime, samples, timeDeltas);
		}
	}

	/**
	 * Representation of a profile node in the format of the Chrome DevTools
	 * protocol
	 *
	 * @see https://chromedevtools.github.io/devtools-protocol/tot/Profiler/#type-ProfileNode
	 */
	private static class ProfileNode {
		private static int nextId = 0;

		public final int id = nextId++; // CDT protocol
		public final CallFrame callFrame; // CDT protocol
		public final List<Integer> children = new ArrayList<>(); // CDT protocol

		// Internal map to keep track of the (non-)existence of child nodes for
		// call frames. Needs to be kept consistent with `children`.
		private final Map<CallFrame, ProfileNode> childNodes = new LinkedHashMap<>();

		public ProfileNode(CallFrame callFrame) {
			this.callFrame = callFrame;
		}

		public ProfileNode addChildIfAbsent(CallFrame callFrame) {
			var node = childNodes.computeIfAbsent(callFrame, ProfileNode::new);
			if (children.size() != childNodes.size()) { // `node` was newly created
				children.add(node.id);
			}
			return node;
		}

		public String toJson(int tabs) {
			var b = new StringBuilder();
			appendln(b, tabs, "{");
			appendln(b, tabs + 1, "\"id\": " + id + ",");
			appendln(b, tabs + 1, "\"callFrame\": " + callFrame.toJson(tabs + 1).strip() + ",");
			appendln(b, tabs + 1, "\"children\": " + children);
			appendln(b, tabs, "}");
			return b.toString();
		}
	}


	/**
	 * Representation of a call frame in the format of the Chrome DevTools
	 * protocol
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

		public String toJson(int tabs) {
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

		public static CallFrame of(Environment e) {
			var functionName = functionNameOf(e);
			var scriptId = "";
			var url = "";
			var lineNumber = -1;
			var columnNumber = -1;

			var location = e.getCallerLocation();
			if (location != null && location.hasLineColumn()) {
				scriptId = location.toString();
				url = Objects.equals("file", location.getScheme()) ? location.getPath() : scriptId;
				lineNumber = location.getBeginLine() - 1;
				columnNumber = location.getBeginColumn();
			}

			return new CallFrame(functionName, scriptId, url, lineNumber, columnNumber);
		}

		private static String functionNameOf(Environment e) {
			// Conceptually, each call frame in a stack trace of the interpreter
			// is itself represented as a linked list of environments. To get
			// the "right" name of a call frame for the purpose of constructing
			// a flame graph, we need to find the name of the *penultimate*
			// environment in the list (as the final environment represents the
			// module), unless the call frame concerns an anonymous function.
			var name = e.getName();
			var parent = e.getParent();
			if (Objects.equals("Anonymous Function", name)) {
				return name + " (" + e.getCreatorLocation() + ")"; 
			} else if (parent == null || parent == e.getRoot()) {
				return name;
			} else {
				return functionNameOf(parent);
			}
		}
	}

	private static StringBuilder appendln(StringBuilder b, int tabs, String s) {
		b.append(" ".repeat(tabs * 2));
		b.append(s);
		b.append(System.lineSeparator());
		return b;
	}
}

public class Profiler extends Thread {
	private Evaluator eval;
	private volatile boolean running;
	private long resolution = 1;
	private final Map<ISourceLocation,Count> ast;
	private final Map<ISourceLocation, Count> frame;
	private final Map<ISourceLocation, String> names;
	private final FlameGraph flameGraph = new FlameGraph();
	
	public Profiler(Evaluator ev){
		super("Rascal-Sampling-Profiler");
		this.eval = ev;
		this.ast = new HashMap<>();
		this.frame = new HashMap<>();
		this.names = new HashMap<>();
		running = true;
	}
	
	@Override
	public void run(){
		while(running) {
			AbstractAST current = eval.getCurrentAST();
			Environment env = eval.getCurrentEnvt();
			String name = env.getName();
			
			if (current != null) {
				flameGraph.tick(eval);

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
		reportFlameGraph();
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

	private void reportFlameGraph() {
		var path = flameGraph.write();
		var out = eval.getOutPrinter();
		out.printf("FLAME GRAPH: %s\n", path == null ? "N/A" : path);
		out.flush();
	}
}
