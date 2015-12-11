package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;

public class DebugFrameObserver implements IFrameObserver {

	private final PrintWriter stdout;

	public DebugFrameObserver(PrintWriter stdout){
		this.stdout = stdout;
	}
	
	@Override
	public boolean observe(Frame frame) {
		stdout.println("observe: " + frame.src);
		return true;
	}
	
}
