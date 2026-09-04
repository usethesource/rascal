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
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
import org.rascalmpl.debug.IRascalFrame;
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













class Cpuinfo {
	private final List<Tick> ticks = new ArrayList<>();

	public void tick(Evaluator evaluator) {
		ticks.add(Tick.of(evaluator));
	}

	public Path write() {
		var dateTime = Instant.now().atZone(ZoneId.systemDefault()).toString();
		dateTime = dateTime.replaceAll("[:\\-]", "");
		dateTime = dateTime.substring(0, 15);
		dateTime = "-" + dateTime;
		
		var hint = "";
		if (!ticks.isEmpty() && ticks.get(0).frames.size() > 1) {
			hint = ticks.get(0).frames.get(1).functionName; // Skip initial call frame (`$`)
			hint = hint.replaceAll("[\\\\]", "");
			hint = "-" + hint;
		}

		try {
			var path = Path.of("profile" + dateTime + hint + ".cpuprofile");
			var csq = Profile.of(ticks).toJson(0);
			Files.writeString(path, csq);
			return path;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class Tick {
		public final long time; // Microseconds
		public final List<CallFrame> frames;

		public Tick(long time, List<CallFrame> frames) {
			this.time = time;
			this.frames = frames;
		}

		public boolean isKwpInitializer() {
			return !frames.isEmpty() && Objects.equals("kwp initializer", frames.get(0).functionName);
		}

		public static Tick of(Evaluator evaluator) {
			var time = System.nanoTime() / 1000;
			var frames = new ArrayList<CallFrame>();

			var callee = evaluator.getCurrentEnvt();
			while (callee != null) {
				var caller = callee.getCallerScope();
				frames.add(0, CallFrame.of(caller, callee));
				callee = caller;
			}

			return new Tick(time, frames);
		}

		public static long timeDelta(Tick early, Tick late) {
			return (early == null || late == null) ? 0 : (late.time - early.time);
		}

		public static List<Long> timeDeltas(List<Tick> ticks) {
			var deltas = new ArrayList<Long>();
			for (var i = 0; i < ticks.size(); i++) {
				var early = i == 0 ? null : ticks.get(i - 1);
				var late = ticks.get(i);
				deltas.add(timeDelta(early, late));
			}
			return deltas;
		}
	}

	// https://chromedevtools.github.io/devtools-protocol/tot/Profiler/#type-Profile
	public static class Profile {
		public final Set<ProfileNode> nodes;
		public final long startTime;
		public final long endTime;
		public final List<Integer> samples;
		public final List<Long> timeDeltas;

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
			ticks.removeIf(Tick::isKwpInitializer);
			assert !ticks.isEmpty();
			
			var nodes = new LinkedHashSet<ProfileNode>(); // Iterable by insertion order
			var startTime = ticks.get(0).time;
			var endTime = ticks.get(ticks.size() - 1).time;
			var samples = new ArrayList<Integer>();
			var timeDeltas = Tick.timeDeltas(ticks);

			var root = new ProfileNode(null); // Dummy root node
			for (var current : ticks) {
				var lineage = root.addLineage(current.frames.iterator());
				nodes.addAll(lineage);
				samples.add(lineage.get(lineage.size() - 1).id);
			}

			return new Profile(nodes, startTime, endTime, samples, timeDeltas);
		}
	}

	// https://chromedevtools.github.io/devtools-protocol/tot/Profiler/#type-ProfileNode
	private static class ProfileNode {
		private static int nextId = 0;

		public final int id = nextId++;
		public final CallFrame frame;
		public final List<Integer> children = new ArrayList<>();

		private final Map<CallFrame, ProfileNode> nodes = new LinkedHashMap<>();

		public ProfileNode(CallFrame frame) {
			this.frame = frame;
		}

		public List<ProfileNode> addLineage(Iterator<CallFrame> frames) {
			if (frames.hasNext()) {
				var node = nodes.computeIfAbsent(frames.next(), ProfileNode::new);
				var lineage = node.addLineage(frames);
				children.add(node.id);
				lineage.add(0, node);
				return lineage;
			} else {
				return new ArrayList<>();
			}
		}

		public String toJson(int tabs) {
			var b = new StringBuilder();
			appendln(b, tabs, "{");
			appendln(b, tabs + 1, "\"id\": " + id + ",");
			appendln(b, tabs + 1, "\"callFrame\": " + frame.toJson(tabs + 1).strip() + ",");
			appendln(b, tabs + 1, "\"children\": " + children);
			appendln(b, tabs, "}");
			return b.toString();
		}
	}

	// https://chromedevtools.github.io/devtools-protocol/tot/Runtime/#type-CallFrame
	public static class CallFrame {
		public final String functionName;
		public final String scriptId;
		public final String url;
		public final int lineNumber;
		public final int columnNumber;

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
				return
					Objects.equals(functionName, frame.functionName) &&
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

		public static CallFrame of(Environment caller, Environment callee) {
			var functionName = callee.getName();
			var scriptId = "$"; // TODO
			var url = "";
			var lineNumber = -1;
			var columnNumber = -1;
			
			if (caller != null) {
				var location = caller.getCreatorLocation();
				if (location != null && location.hasLineColumn()) {
					url = Objects.equals("file", location.getScheme()) ? location.getPath() : location.toString();
					lineNumber = location.getBeginLine() - 1;
					columnNumber = location.getBeginColumn();
				}
			}

			return new CallFrame(functionName, scriptId, url, lineNumber, columnNumber);
		}
	}

	private static final int TAB_SIZE = 2;

	private static StringBuilder appendln(StringBuilder b, int tabs, String s) {
		b.append(" ".repeat(tabs * TAB_SIZE));
		b.append(s);
		b.append(System.lineSeparator());
		return b;
	}
}















class FlameGraph {
	private final Map<String, Count> counts = new HashMap<>();

	void sample(Evaluator eval) {
		var frames = eval.getCallStack().stream();
		var folded = frames.map(FlameGraph::getFrameTitle).collect(Collectors.joining(";"));
		var count = counts.computeIfAbsent(folded, k -> new Count());
		count.increment();
	}

	private static String getFrameTitle(IRascalFrame frame) {
		var title = frame.getName();
		var callerLocation = frame.getCallerLocation();
		if (callerLocation != null) {
			title += " at " + callerLocation;
		}
		return title;
	}

	void write() {
		var name = "flameGraph";
		var out = Path.of(name + ".out");
		var err = Path.of(name + ".err");
		var svg = Path.of(name + ".svg");

		try {
			Files.writeString(out, "");
			for (var e : counts.entrySet()) {
				 // Newlines must be `\n` for `flamegraph.pl` to work
				var csq = String.format("%s %d\n", e.getKey(), e.getValue().getTicks());
				Files.writeString(out, csq, StandardOpenOption.APPEND);
			}

			var scriptKey = "org.rascalmpl.profiling.flameGraph.script";
			var scriptValue = System.getProperty(scriptKey);
			if (scriptValue != null) {
				var script = Path.of(scriptValue);
				if (Files.exists(script)) {

					ProcessBuilder processBuilder = new ProcessBuilder("perl", script.toString(), out.toString());
					processBuilder.redirectOutput(svg.toFile());
					processBuilder.redirectError(err.toFile());

					Process process = processBuilder.start();
					try {
						process.waitFor();
					} catch (InterruptedException e) {
						// Ignore; doesn't matter
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
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
	private final Cpuinfo cpuinfo = new Cpuinfo();
	
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
		// cpuinfo.start();
		while(running) {
			AbstractAST current = eval.getCurrentAST();
			Environment env = eval.getCurrentEnvt();
			String name = env.getName();

			flameGraph.sample(eval);
			// cpuinfo.addStackTrace(eval.getStackTrace());
			cpuinfo.tick(eval);
			
			if (current != null) {
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
		// cpuinfo.end();
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
		var path = cpuinfo.write();
		if (path != null) {
			System.out.println("Profile: " + path);
		}
		flameGraph.write();
		report("FRAMES", frame);
		eval.getOutPrinter().println();
		report("ASTS", ast);
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

}
