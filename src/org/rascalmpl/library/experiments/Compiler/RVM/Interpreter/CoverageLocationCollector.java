package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.values.ValueFactoryFactory;

public class CoverageLocationCollector implements ILocationCollector, ILocationReporter<ISet> {

	private final HashSet<ISourceLocation> data;
	
	public CoverageLocationCollector(){
		this.data = new HashSet<ISourceLocation>();
	}
	
	@Override
	public void registerLocation(ISourceLocation src) {
		//System.err.println("registerLocation: " + src);
		data.add(src);
		//System.err.println("data:"  + data);
	}
	
	@Override
	public ISet getData(){
		ISetWriter w = ValueFactoryFactory.getValueFactory().setWriter();
		//System.err.println("getData: " + data);
		for (ISourceLocation src : data) {
			//System.err.println("getData: " + src);
			w.insert(src);
		}
		return w.done();
	}

	@Override
	public void report(ISet data, PrintWriter out) {
		Iterator<IValue> iter = data.iterator();
		out.println(data.size() > 0 ? "COVERAGE:" : "NO COVERAGE DATA");
		while(iter.hasNext()){
			out.printf("%s\n",  iter.next());
		}
	}
	
	public void report(PrintWriter out) {
		out.println(data.size() > 0 ? "COVERAGE:" : "NO COVERAGE DATA");
		for(ISourceLocation src : data){
			out.printf("%s\n",  src);
		}
	}

}
