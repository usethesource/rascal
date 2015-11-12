package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.value.ISourceLocation;

public class SourceLocationBreakpoint extends BreakPoint {
	private final ISourceLocation loc;
	
	SourceLocationBreakpoint(int id, ISourceLocation loc){
		super(id);
		this.loc = loc;
	}
	
	@Override
	void println(PrintWriter stdout){
		stdout.println(id + "\t" + isEnabled() + "\t" + loc);
	}
	
	@Override
	public boolean matchOnObserve(Frame frame) {
		return containedIn(loc, frame.src);
	}

	@Override
	public boolean matchOnEnter(Frame frame) {
		return containedIn(loc, frame.src);
	}

	@Override
	public boolean matchOnLeave(Frame frame) {
		return containedIn(loc, frame.src);
	}
}
