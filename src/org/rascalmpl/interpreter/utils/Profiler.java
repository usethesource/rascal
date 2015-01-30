/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Count other = (Count) obj;
		if (ticks != other.ticks)
			return false;
		return true;
	}
	
}

public class Profiler extends Thread {
	private Evaluator eval;
	private HashMap<ISourceLocation,Count> data;
	private volatile boolean running;
	private long resolution = 1;
	
	public Profiler(Evaluator ev){
		this.eval = ev;
		this.data = new HashMap<ISourceLocation,Count>();
		running = true;
	}
	
	@Override
	public void run(){
		while(running) {
			AbstractAST current = eval.getCurrentAST();
			if (current != null) {
				ISourceLocation stat = current.getLocation();
				if(stat != null){
					Count currentCount = data.get(stat);
					if(currentCount == null)
						data.put(stat, new Count());
					else
						currentCount.increment();
				}
			}
			try {
				sleep(resolution);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
	
	private List<Map.Entry<ISourceLocation, Count>> sortData(){
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
		TypeFactory TF = TypeFactory.getInstance();
		Type elemType = TF.tupleType(TF.sourceLocationType(), TF.integerType());
		IValueFactory VF = ValueFactoryFactory.getValueFactory();
		IListWriter w = VF.listWriter(elemType);
		for(Map.Entry<ISourceLocation, Count> e : sortData()){
			w.insert(VF.tuple(e.getKey(), VF.integer(e.getValue().getTicks())));
		}
		return w.done();
	}
	
	public void report() {
	  List<Map.Entry<ISourceLocation, Count>> sortedData = sortData();

	  int maxURL = 1;
	  long nTicks = 0;

	  for(Map.Entry<ISourceLocation, Count> e : sortedData){
	    int sz = e.getKey().getURI().toString().length();
	    if(sz > maxURL)
	      maxURL = sz;
	    nTicks += e.getValue().getTicks();
	  }
	  PrintWriter out = eval.getStdOut();
	  String URLFormat = "%" + maxURL + "s";
	  out.printf("PROFILE: %d data points, %d ticks, tick = %d milliSecs\n", data.size(), nTicks, resolution);
	  out.printf(URLFormat + "%8s%9s  %s\n", " Source File", "Ticks", "%", "Source");

	  for(Map.Entry<ISourceLocation, Count> e : sortedData){
	    String L = e.getKey().getURI().toString();

	    int ticks = e.getValue().getTicks();
	    double perc = (ticks * 100.0)/nTicks;

	    String source = String.format("%s", e.getKey().toString());

	    out.printf(URLFormat + "%8d%8.1f%%  %s\n", L, ticks, perc, source);
	  }
	  
	  // Make sure that our output is seen:
	  out.flush();
	}

}
