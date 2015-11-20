package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.value.ISourceLocation;

public class FunctionEntryBreakpoint extends BreakPoint {
	private final String functionName;
	private boolean alreadyInFunction = false;
	
	FunctionEntryBreakpoint(int id, String functionName){
		super(id);
		
		this.functionName = functionName;
		this.alreadyInFunction = false;
	}
	
	@Override void reset(){
		alreadyInFunction = false;
	}
	
	@Override
	void println(PrintWriter stdout){
		stdout.println(id + "\t" + isEnabled() + "\t" + functionName);
	}
	
	@Override
	public boolean matchOnObserve(Frame frame) {
		if(alreadyInFunction){
			return false;
		}
		if(frame.function.getPrintableName().equals(functionName)){
			alreadyInFunction = true;
			return true;
		}
		alreadyInFunction = false;
		return false;
	}

	@Override
	public boolean matchOnEnter(Frame frame) {
		return frame.function.getPrintableName().equals(functionName);
	}

	@Override
	public boolean matchOnLeave(Frame frame) {
		return false;
	}
}
