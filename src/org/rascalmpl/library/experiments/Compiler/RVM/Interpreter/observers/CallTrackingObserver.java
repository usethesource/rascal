package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;

public class CallTrackingObserver implements IFrameObserver {

	private final PrintWriter stdout;

	public CallTrackingObserver(PrintWriter stdout){
		this.stdout = stdout;
	}

	@Override
	public boolean enter(Frame frame) {
		frame.printEnter(stdout); 
		stdout.flush();
		return true;
	}

	@Override
	public boolean leave(Frame frame, Object rval) {
		 frame.printLeave(stdout, rval);
		 return true;
	}
}
