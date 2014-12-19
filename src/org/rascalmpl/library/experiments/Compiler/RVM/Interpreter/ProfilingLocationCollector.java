package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;

public class ProfilingLocationCollector implements ILocationCollector, ILocationReporter<IList> {

	volatile ISourceLocation src;
	
	Profiler profiler;
	
	ProfilingLocationCollector(){
		profiler = new Profiler(this);
	}
	
	@Override
	public void registerLocation(ISourceLocation src) {
		this.src = src;
	}

	public ISourceLocation getLocation() {
		return src;
	}

	public void start() {
		profiler.start();
	}

	@Override
	public void printData(PrintWriter out) {
		profiler.pleaseStop();
		profiler.report(out);
	}

	@Override
	public IList getData() {
		profiler.pleaseStop();
		IList data = profiler.getProfileData();
		return data;
	}

}
