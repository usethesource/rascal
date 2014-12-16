package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class CoverageLocationReporter implements ILocationReporter {

	private HashMap<ISourceLocation,Integer> data;
	
	CoverageLocationReporter(){
		this.data = new HashMap<ISourceLocation,Integer>();
	}
	
	@Override
	public void setLocation(ISourceLocation src) {
		// TODO Auto-generated method stub
		Integer currentCount = data.get(src);
		if(currentCount == null)
			data.put(src, 1);
		else
			data.put(src, currentCount + 1);
	}

	@Override
	public void start() {
		// Nothing
	}

	@Override
	public void stop() {
		// Nothing
	}
	
	private List<Map.Entry<ISourceLocation, Integer>> sortData(){
		  List<Map.Entry<ISourceLocation, Integer>> sortedData = new Vector<Entry<ISourceLocation, Integer>>(data.entrySet());

		  java.util.Collections.sort(sortedData, new Comparator<Map.Entry<ISourceLocation, Integer>>(){
		    public int compare(Entry<ISourceLocation, Integer> entry1, Entry<ISourceLocation, Integer> entry2) {
		      return ((entry1.getValue() == entry2.getValue()) ? 0 : 
		              (entry1.getValue() < entry2.getValue() ? 1 : -1));
		    }
		  });
		  return sortedData;
		}

	@Override
	public void report(PrintWriter out) {
		
		for(Map.Entry<ISourceLocation, Integer> e : sortData()){
			out.printf("%8d: %s\n",  e.getValue(), e.getKey() );
		}

	}

}
