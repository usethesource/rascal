/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

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
	
}

public class Profiler extends Thread {
	private Evaluator eval;
	private HashMap<AbstractAST,Count> data;
	private volatile boolean running;
	private long resolution = 1;
	
	public Profiler(Evaluator ev){
		this.eval = ev;
		this.data = new HashMap<AbstractAST,Count>();
		running = true;
	}
	
	@Override
	public void run(){
		while(running){
			AbstractAST stat = eval.getCurrentAST();
			if(stat != null){
				Count currentCount = data.get(stat);
				if(currentCount == null)
					data.put(stat, new Count());
				else
					currentCount.increment();
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
	
	private List<Map.Entry<AbstractAST, Count>> sortData(){
		 List<Map.Entry<AbstractAST, Count>> sortedData = new Vector<Entry<AbstractAST, Count>>(data.entrySet());

        java.util.Collections.sort(sortedData, new Comparator<Map.Entry<AbstractAST, Count>>(){
			public int compare(Entry<AbstractAST, Count> entry1,
					Entry<AbstractAST, Count> entry2) {
				 return (entry1.getValue().equals(entry2.getValue()) ? 0 : 
					 (entry1.getValue().getTicks() < entry2.getValue().getTicks() ? 1 : -1));
			}
        });
        return sortedData;
	}
	
	public IList getProfileData(){
		TypeFactory TF = TypeFactory.getInstance();
		Type elemType = TF.tupleType(TF.sourceLocationType(), TF.integerType());
		Type listType = TF.listType(elemType);
		IValueFactory VF = ValueFactoryFactory.getValueFactory();
		IListWriter w = listType.writer(VF);
		for(Map.Entry<AbstractAST, Count> e : sortData()){
			w.insert(VF.tuple(e.getKey().getLocation(), VF.integer(e.getValue().getTicks())));
		}
		return w.done();
	}
	
	public void report(){
		
		List<Map.Entry<AbstractAST, Count>> sortedData = sortData();
        
        int maxURL = 1;
        long nTicks = 0;
        
        for(Map.Entry<AbstractAST, Count> e : sortedData){
        	URI url = e.getKey().getLocation().getURI();
        	int sz = url.toString().length();
        	if(sz > maxURL)
        		maxURL = sz;
        	nTicks += e.getValue().getTicks();
        }
        PrintWriter out = eval.getStdOut();
        String URLFormat = "%" + maxURL + "s";
        synchronized(out){
	        out.printf("PROFILE: %d data points, %d ticks, tick = %d milliSecs\n", data.size(), nTicks, resolution);
	        out.printf(URLFormat + "%11s%8s%9s  %s\n", " Source File", "Lines", "Ticks", "%", "Source");
	    	
	        for(Map.Entry<AbstractAST, Count> e : sortedData){
	        	ISourceLocation L = e.getKey().getLocation();
	        	
	        	String uri = L.getURI().toString();
	        	String filePrefix = "file://";
	        	if(uri.startsWith(filePrefix))
	        		uri = uri.substring(filePrefix.length());
	        	
	        	int bgn = L.getBeginLine();
	        	int end = L.getEndLine();
	        	String range = (end==bgn) ? Integer.toString(bgn) : bgn + ".." + end;
	        	
	        	int ticks = e.getValue().getTicks();
	        	double perc = (ticks * 100.0)/nTicks;
	        	
	        	String source = String.format("%-30.30s", e.getKey().toString().replaceFirst("^[\\s]+", "").replaceAll("[\\s]+", " "));
	 
	        	out.printf(URLFormat + "%11s%8d%8.1f%%  %s\n", uri, range, ticks, perc, source);
	        }
        }
	}

}
