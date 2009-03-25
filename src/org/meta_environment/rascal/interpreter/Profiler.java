package org.meta_environment.rascal.interpreter;

import java.net.URL;
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
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.ast.Statement;

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
	private HashMap<Statement,Count> data;
	private volatile boolean running;
	private long resolution = 1;
	
	Profiler(Evaluator ev){
		this.eval = ev;
		this.data = new HashMap<Statement,Count>();
		running = true;
	}
	
	@Override
	public void run(){
		while(running){
			Statement stat = eval.getCurrentStatement();
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
	
	private List<Map.Entry<Statement, Count>> sortData(){
		 List<Map.Entry<Statement, Count>> sortedData = new Vector<Entry<Statement, Count>>(data.entrySet());

        java.util.Collections.sort(sortedData, new Comparator<Map.Entry<Statement, Count>>(){
			public int compare(Entry<Statement, Count> entry1,
					Entry<Statement, Count> entry2) {
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
		for(Map.Entry<Statement, Count> e : sortData()){
			w.insert(VF.tuple(e.getKey().getLocation(), VF.integer(e.getValue().getTicks())));
		}
		return w.done();
	};
	
	public void report(){
		
		List<Map.Entry<Statement, Count>> sortedData = sortData();
        
        int maxURL = 1;
        long nTicks = 0;
        
        for(Map.Entry<Statement, Count> e : sortedData){
        	URL url = e.getKey().getLocation().getURL();
        	int sz = url.toString().length();
        	if(sz > maxURL)
        		maxURL = sz;
        	nTicks += e.getValue().getTicks();
        }
        String URLFormat = "%" + maxURL + "s";
        System.err.printf("PROFILE: %d data points, %d ticks, tick = %d milliSecs\n", data.size(), nTicks, resolution);
        System.err.printf(URLFormat + "%11s%8s%9s  %s\n", " Source File", "Lines", "Ticks", "%", "Source");
    	
        for(Map.Entry<Statement, Count> e : sortedData){
        	ISourceLocation L = e.getKey().getLocation();
        	
        	String url = L.getURL().toString();
        	String filePrefix = "file://";
        	if(url.startsWith(filePrefix))
        		url = url.substring(filePrefix.length());
        	
        	int bgn = L.getBeginLine();
        	int end = L.getEndLine();
        	String range = (end==bgn) ? Integer.toString(bgn) : bgn + ".." + end;
        	
        	int ticks = e.getValue().getTicks();
        	Double perc = (ticks * 100.0)/nTicks;
        	
        	String source = String.format("%-30.30s", e.getKey().toString().replaceFirst("^[\\s]+", "").replaceAll("[\\s]+", " "));
 
        	System.err.printf(URLFormat + "%11s%8d%8.1f%%  %s\n", url, range, ticks, perc, source);
        }
	}

}
