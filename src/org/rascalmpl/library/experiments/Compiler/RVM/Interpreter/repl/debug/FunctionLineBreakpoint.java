package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;

public class FunctionLineBreakpoint extends BreakPoint {
	private final String functionName;
	private final int lino;
	
	FunctionLineBreakpoint(int id, String functionName, int lino){
		super(id);
		this.functionName = functionName;
		this.lino = lino;
	}
	
	@Override
	void println(PrintWriter stdout){
		stdout.println(id + "\t" + isEnabled() + "\tFunctionLine\t" + ignore + "\t" + functionName + ":" + lino);
	}
	
	@Override
	public boolean matchOnObserve(Frame frame) {		
		return enabled && ignoreOrBreak(frame.function.getPrintableName().equals(functionName) && shouldBreakAt(lino, frame.src));
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
        return "FunctionLineBreakpoint " + functionName + ", line " + lino;
    }
}
