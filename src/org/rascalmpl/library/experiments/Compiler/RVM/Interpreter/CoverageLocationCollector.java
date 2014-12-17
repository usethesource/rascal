package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.HashSet;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.values.ValueFactoryFactory;

public class CoverageLocationCollector implements ILocationCollector {

	private HashSet<ISourceLocation> data;
	
	public CoverageLocationCollector(){
		this.data = new HashSet<ISourceLocation>();
	}
	
	@Override
	public void registerLocation(ISourceLocation src) {
		data.add(src);
	}
	
	public ISet get(){
		ISetWriter w = ValueFactoryFactory.getValueFactory().setWriter();
		for (ISourceLocation src : data) {
			w.insert(src);
		}
		return w.done();
	}

	@Override
	public void print(PrintWriter out) {
		
		for(ISourceLocation src : data){
			out.printf("%s\n",  src );
		}

	}

}
