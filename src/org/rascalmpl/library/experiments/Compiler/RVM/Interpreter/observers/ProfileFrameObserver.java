package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;

public class ProfileFrameObserver implements IFrameObserver {

	private final PrintWriter stdout;
	private volatile ISourceLocation src;
	
	private Profiler profiler;
	
	public ProfileFrameObserver(RascalExecutionContext rex){
		this.stdout = rex.getStdOut();
		profiler = new Profiler(this);
		profiler.start();
	}
	
	@Override
	public boolean observe(Frame frame) {
		if(frame != null){
			this.src = frame.src;
		}
		return true;
	}
	
	@Override
	public boolean enter(Frame frame) {
		if(frame != null){
			this.src = frame.src;
		}
		return true;
	}
	
	@Override
	public boolean leave(Frame frame, Object rval) {
		if(frame != null){
			this.src = frame.src;
		}
		return true;
	}
	
	@Override
	public void stopObserving() {
		profiler.stopCollecting();
	}

	@Override
	public void startObserving() {
		profiler.startCollecting();
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
	public void report(IList data) {
		profiler.pleaseStop();
		profiler.report(stdout);
	}
	
	@Override
	public void report() {
		profiler.pleaseStop();
		profiler.report(stdout);
	}

	@Override
	public IList getData() {
		profiler.pleaseStop();
		IList data = profiler.getProfile();
		return data;
	}
}
