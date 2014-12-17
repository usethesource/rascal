package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

public class CompilerError extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CompilerError(String msg) {
	
		super("Internal compiler error: " + msg);
	}
	
	public CompilerError(String msg, Frame currentFrame) {
		super("Internal compiler error: " + msg);
		printStackTrace(currentFrame, new PrintWriter(System.out));
	}
	
	public CompilerError(String msg, PrintWriter out, Frame currentFrame) {
		super("Internal compiler error: " + msg);
		printStackTrace(currentFrame, out);
	}
	
	public void printStackTrace(Frame currentFrame, PrintWriter stdout) {
		
		if(currentFrame != null){
			stdout.println("Call stack (most recent first):");

			for(Frame f = currentFrame; f != null; f = f.previousCallFrame) {
				//stdout.println("at " + f.function.name);
				stdout.println("\t in " + f);
			}
		} else {
			stdout.println("No call stack available");
		}
	}

}
