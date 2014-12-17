package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;

public class ProfilingLocationCollector implements ILocationCollector {

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
	public void print(PrintWriter out) {
		profiler.pleaseStop();
		profiler.report(out);
	}

	@Override
	public IValue get() {
		profiler.pleaseStop();
		return profiler.report();
	}

}
