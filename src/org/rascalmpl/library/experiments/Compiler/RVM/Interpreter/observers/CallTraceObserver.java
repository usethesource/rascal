package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;

public class CallTraceObserver implements IFrameObserver {

	private final PrintWriter stdout;

	public CallTraceObserver(RascalExecutionContext rex){
		this.stdout = rex.getStdOut();
	}

	@Override
	public boolean enter(Frame frame) {
		frame.printEnter(stdout); 
		stdout.flush();
		return true;
	}

	@Override
	public boolean leave(Frame frame, Object rval) {
	    if (frame != null) {
	        frame.printLeave(stdout, rval);
	    }
	    
	    return true;
	}
}
