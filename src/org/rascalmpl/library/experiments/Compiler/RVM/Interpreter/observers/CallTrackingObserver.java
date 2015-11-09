package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;

public class CallTrackingObserver implements IFrameObserver {

	private final PrintWriter stdout;

	public CallTrackingObserver(PrintWriter stdout){
		this.stdout = stdout;
	}

	@Override
	public void enter(Frame frame) {
		frame.printEnter(stdout); 
		stdout.flush();
	}

	@Override
	public void leave(Frame frame, Object rval) {
		 frame.printLeave(stdout, rval);
	}
}
