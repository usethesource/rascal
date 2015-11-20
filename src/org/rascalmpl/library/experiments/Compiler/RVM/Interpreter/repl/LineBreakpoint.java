package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.value.ISourceLocation;

public class LineBreakpoint extends BreakPoint {
	private final String path;
	private final int lino;
	
	LineBreakpoint(int id, String path, int lino){
		super(id);
		this.path = path;
		this.lino = lino;
	}
	
	@Override
	void reset () { }
	
	@Override
	void println(PrintWriter stdout){
		stdout.println(id + "\t" + isEnabled() + "\t" + path + ":" + lino);
	}
	
	@Override
	public boolean matchOnObserve(Frame frame) {
		return containedIn(path, lino, frame.src);
	}

	@Override
	public boolean matchOnEnter(Frame frame) {
		return containedIn(path, lino, frame.src);
	}

	@Override
	public boolean matchOnLeave(Frame frame) {
		return containedIn(path, lino, frame.src);
	}
}
