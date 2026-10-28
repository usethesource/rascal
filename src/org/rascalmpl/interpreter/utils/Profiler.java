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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.utils.profiler.DummyFormatter;
import org.rascalmpl.interpreter.utils.profiler.DummySampler;
import org.rascalmpl.interpreter.utils.profiler.Sampler;
import org.rascalmpl.interpreter.utils.profiler.TempFileSampler;
import org.rascalmpl.semantics.dynamic.Expression.CallOrTree;
import org.rascalmpl.semantics.dynamic.Expression.QualifiedName;
import org.rascalmpl.semantics.dynamic.QualifiedName.Default;
import org.rascalmpl.semantics.dynamic.Statement.Expression;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

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
		this.sampler = createSampler(ev);
		this.ast = new HashMap<>();
		this.frame = new HashMap<>();
		this.names = new HashMap<>();
		running = true;
	}

	private static Sampler createSampler(Evaluator ev) {
		if (ev.getConfiguration().getProfileFmtProperty() instanceof DummyFormatter) {
			return new DummySampler(); // Don't sample when there won't be formatting
		}

		try {
			return new TempFileSampler(ev);
		} catch (IOException e) {
			return new DummySampler(); // Don't sample when the temp file is problematic
		}
	}
	
	@Override
	public void run(){
		while(running) {
			sampler.produceSample();

			AbstractAST current = eval.getCurrentAST();
			Environment env = eval.getCurrentEnvt();
			String name = env.getName();
			
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
		reportSamples();
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

	private void reportSamples() {
		var fmt = eval.getConfiguration().getProfileFmtProperty();
		var dir = eval.getConfiguration().getProfileDirProperty();
		var file = dir.resolve("profile" + dateTimeHint() + functionNameHint(eval.getCurrentAST()) + "." + fmt.getExtension());
		var link = dir.resolve("profile-latest." + fmt.getExtension());

		// Create file
		var message = "";
		try (var writer = Files.newBufferedWriter(file)) {
			sampler.consumeSamples(samples -> fmt.format(writer, samples));
			message = URIUtil.createFileLocation(file).toString();
		} catch (URISyntaxException | IOException e){
			message = String.format("Failed to write data (%s)", e);
		} finally {
			sampler.close();
		}

		// Create symbolic link (preferred) or copy (fallback) with a predictable name (e.g., useful if a separate
		// post-processing script/tool needs to be run on the file, such as `flamegraph.pl`).
		try {
			Files.createSymbolicLink(link, file);
		} catch (IOException e1) {
			try {
				Files.copy(file, link, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e2) {
				// Minor inconvenience but not of crucial importance; ok to ignore.	
			}
		}

		// Print to out
		var out = eval.getOutPrinter();
		out.printf("FORMATTED PROFILE: %s\n", message);
		out.flush();
	}

	private static String dateTimeHint() {
		var dateTime = Instant.now().atZone(ZoneId.systemDefault()).toString();
		dateTime = dateTime.replaceAll("[:\\-]", "");
		dateTime = dateTime.substring(0, 15);
		return "-" + dateTime;
	}

	private static String functionNameHint(AbstractAST ast) {
		// Traverse `ast`
		if (ast instanceof Expression) {
			ast = ((Expression) ast).hasExpression() ? ((Expression) ast).getExpression() : ast;
		}
		if (ast instanceof CallOrTree) {
			ast = ((CallOrTree) ast).hasExpression() ? ((CallOrTree) ast).getExpression() : ast;
		}
		if (ast instanceof QualifiedName) {
			ast = ((QualifiedName) ast).hasQualifiedName() ? ((QualifiedName) ast).getQualifiedName() : ast;
		}
		
		// Compute the hint (if possible)
		var hint = "";
		if (ast instanceof Default) {
			hint = ((Default) ast).lastName();
			hint = hint.replaceAll("[\\\\]", "");
			hint = "-" + hint;
		}
		return hint;
	}
}
