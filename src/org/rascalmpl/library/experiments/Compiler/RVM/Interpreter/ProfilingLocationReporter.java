package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class ProfilingLocationReporter implements ILocationReporter {

	ISourceLocation src;
	
	Profiler profiler;
	
	ProfilingLocationReporter(){
		profiler = new Profiler(this);
	}
	
	@Override
	public void setLocation(ISourceLocation src) {
		this.src = src;
	}

	public ISourceLocation getLocation() {
		return src;
	}

	public void start() {
		profiler.start();
	}

	public void report(PrintWriter out) {
		profiler.pleaseStop();
		profiler.report(out);
	}

	@Override
	public void stop() {
		profiler.pleaseStop();
		profiler = null;
	}

}
