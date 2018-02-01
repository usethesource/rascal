package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;

public class FunctionEnterBreakpoint extends BreakPoint {
	private final String functionName;
	
	FunctionEnterBreakpoint(int id, String functionName){
		super(id);
		
		this.functionName = functionName;
	}
	
	@Override
	void println(PrintWriter stdout){
		stdout.println(id + "\t" + isEnabled() + "\tEnter\t" + ignore + "\t" + functionName);
	}
	
	@Override
	public boolean matchOnEnter(Frame frame) {
		return enabled &&  ignoreOrBreak(frame.function.getPrintableName().equals(functionName));
	}
	
	@Override
	public String toString(){
	    return "FunctionEnterBreakpoint " + functionName;
	}
}
