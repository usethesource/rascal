package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;

public class RascalREPLDebugger implements IFrameObserver {

	@Override
	public void observe(Frame frame) {
		System.err.println("REPL observe: " + frame.src);
	}
	
}
