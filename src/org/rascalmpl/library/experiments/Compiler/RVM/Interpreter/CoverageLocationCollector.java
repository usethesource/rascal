package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.HashSet;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
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
	public void printData(PrintWriter out) {
		for(ISourceLocation src : data){
			out.printf("%s\n",  src );
		}
	}

}
