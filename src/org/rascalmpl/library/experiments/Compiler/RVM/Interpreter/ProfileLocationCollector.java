package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISourceLocation;

public class ProfileLocationCollector implements ILocationCollector, ILocationReporter<IList> {

	private volatile ISourceLocation src;
	
	private Profiler profiler;
	
	public ProfileLocationCollector(){
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
	
	public void restart(){
		profiler = new Profiler(this, profiler.getRawData());
	}
	
	public void stop() {
		profiler.pleaseStop();
	}

	@Override
	public void report(IList data, PrintWriter out) {
		profiler.pleaseStop();
		profiler.report(out);
	}
	
	public void report(PrintWriter out) {
		profiler.pleaseStop();
		profiler.report(out);
	}

	@Override
	public IList getData() {
		profiler.pleaseStop();
		IList data = profiler.getProfile();
		return data;
	}

}
