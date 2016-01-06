package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;

public class DebugFrameObserver implements IFrameObserver {

	private final PrintWriter stdout;

	public DebugFrameObserver(RascalExecutionContext rex){
		this.stdout = rex.getStdOut();
	}
	
	@Override
	public boolean observe(Frame frame) {
		stdout.println("observe: " + frame.src);
		return true;
	}
	
}
