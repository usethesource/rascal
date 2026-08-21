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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
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
	private final List<Sample> samples = new ArrayList<>();
	private long startTime = -1;
	private long endTime = -1;
	private long previousTime = -1;

	public void start() {
		assert startTime == -1 && endTime == -1;
		startTime = time();
		previousTime = startTime;
	}

	public void end() {
		assert startTime > -1 && endTime == -1;
		endTime = time();
	}

	public void sample(Evaluator eval) {
		assert startTime > -1 && endTime == -1;
		var currentTime = time();
		var previousTime = this.previousTime;
		this.previousTime = currentTime;

		var delta = currentTime - previousTime;
		var frames = eval.getCallStack(); // Fresh value
		samples.add(new Sample(delta, frames));
	}

	public void write() {
		assert startTime > -1 && endTime > -1;
		
		var nodeIds = new ArrayList<Integer>();
		var timeDeltas = new ArrayList<Long>();

		var root = new Node(null);
		for (var s : samples) {
			var curr = root;
			for (var f : s.frames) {
				curr = curr.getChild(new CallFrame(f));
			}

			nodeIds.add(curr.id);
			timeDeltas.add(s.delta);
		}

		// Build
		var b = new StringBuilder();
		appendln(b, "{");
		appendln(b, "  \"nodes\": [");
		appendln(b, root.getDescendants().stream().map(Cpuinfo::toJson).collect(Collectors.joining(", ")));
		appendln(b, "  ],");
		appendln(b, "  \"startTime\": " + startTime + ",");
		appendln(b, "  \"endTime\": " + endTime + ",");
		appendln(b, "  \"samples\": " + nodeIds + ",");
		appendln(b, "  \"timeDeltas\": " + timeDeltas);
		appendln(b, "}");

		// Write
		var s = b.toString();
		try {
			Files.writeString(Path.of("foobar.json"), s);
			Files.writeString(Path.of("foobar-" + System.currentTimeMillis() + ".cpuprofile"), s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static long time() {
		return System.nanoTime() / 1000; // Microseconds
	}

	private static String toJson(Node node) {
		var b = new StringBuilder();
		appendln(b, "{");
		appendln(b, "  \"id\": " + node.id + ",");
		appendln(b, "  \"callFrame\": {");
		appendln(b, "    \"functionName\": \"" + node.frame.functionName + "\",");
		appendln(b, "    \"url\": \"" + node.frame.url + "\",");
		appendln(b, "    \"lineNumber\": " + node.frame.lineNumber + ",");
		appendln(b, "    \"columnNumber\": " + node.frame.columnNumber + "");
		appendln(b, "  },");
		appendln(b, "  \"children\": [" + node.children.values().stream().map(n -> String.valueOf(n.id)).collect(Collectors.joining(", ")) + "]");
		appendln(b, "}");
		return b.toString();
	}

	private static void appendln(StringBuilder b, String s) {
		b.append(s);
		b.append(System.lineSeparator());
	}

	private static class Sample {
		private final long delta;
		private final Stack<IRascalFrame> frames;

		private Sample(long delta, Stack<IRascalFrame> frames) {
			this.delta = delta;
			this.frames = frames;
		}
	}

	private static class Node {
		private static int nextId = 0;

		private final int id;
		private final CallFrame frame;
		private final Map<CallFrame, Node> children;

		public Node(CallFrame frame) {
			this.id = nextId++;
			this.frame = frame;
			this.children = new LinkedHashMap<>();
		}

		public Node getChild(CallFrame frame) {
			return children.computeIfAbsent(frame, Node::new);
		}

		public List<Node> getDescendants() {
			var descendants = new ArrayList<Node>();
			for (var c : children.values()) {
				descendants.add(c);
				descendants.addAll(c.getDescendants());
			}
			return descendants;
		}
	}

	private static class CallFrame {
		private final String functionName;
		private final String url;
		private final int lineNumber;
		private final int columnNumber;

		public CallFrame(IRascalFrame frame) {
			var name = frame.getName();
			var location = frame.getCallerLocation();

			if (name != null) {
				this.functionName = name;
			} else {
				this.functionName = "(root)";
			}

			if (location != null) {
				// this.url = "file".equals(location.getScheme()) ? location.getPath() : location.toString();
				this.url = location.toString();
				this.lineNumber = location.getBeginLine() - 1;
				this.columnNumber = location.getBeginColumn();
			} else {
				this.url = "";
				this.lineNumber = -1;
				this.columnNumber = -1;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((functionName == null) ? 0 : functionName.hashCode());
			result = prime * result + ((url == null) ? 0 : url.hashCode());
			result = prime * result + lineNumber;
			result = prime * result + columnNumber;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CallFrame other = (CallFrame) obj;
			if (functionName == null) {
				if (other.functionName != null)
					return false;
			}
			else if (!functionName.equals(other.functionName))
				return false;
			if (url == null) {
				if (other.url != null)
					return false;
			}
			else if (!url.equals(other.url))
				return false;
			if (lineNumber != other.lineNumber)
				return false;
			if (columnNumber != other.columnNumber)
				return false;
			return true;
		}
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
		cpuinfo.start();
		while(running) {
			AbstractAST current = eval.getCurrentAST();
			Environment env = eval.getCurrentEnvt();
			String name = env.getName();

			flameGraph.sample(eval);
			cpuinfo.sample(eval);
			
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
		cpuinfo.end();
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
		cpuinfo.write();
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
