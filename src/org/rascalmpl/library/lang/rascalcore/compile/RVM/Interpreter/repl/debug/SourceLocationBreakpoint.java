package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import io.usethesource.vallang.ISourceLocation;

public class SourceLocationBreakpoint extends BreakPoint {
	private final ISourceLocation loc;
	
	SourceLocationBreakpoint(int id, ISourceLocation loc){
		super(id);
		this.loc = loc;
	}
	
	@Override
	void println(PrintWriter stdout){
		stdout.println(id + "\t" + isEnabled() + "Line\t" + ignore + "\t" + loc);
	}
	
	@Override
	public boolean matchOnObserve(Frame frame) {
		return enabled && ignoreOrBreak(containedIn(loc, frame.src));
	}

	@Override
	public boolean matchOnEnter(Frame frame) {
		return matchOnObserve(frame);
	}

	@Override
	public boolean matchOnLeave(Frame frame) {
		return matchOnObserve(frame);
	}
	
	@Override
    public String toString(){
        return "SourceLocationBreakpoint " + loc;
    }
}
