package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

public class InternalCompilerError extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InternalCompilerError(String msg) {
		super(msg);
	}
	
	public InternalCompilerError(String msg, Throwable cause) {
		super(msg, cause);
	}

	public InternalCompilerError(String msg, Frame currentFrame, Throwable cause) {
        super(msg, cause);
    }
	
	public InternalCompilerError(String msg, Frame currentFrame) {
		super(msg);
	}
	
	public InternalCompilerError(String msg, PrintWriter out, Frame currentFrame) {
		super(msg);
	}
	
	
	public void printStackTrace(Frame currentFrame, PrintWriter stdout) {
		
		if(currentFrame != null){
			stdout.println("Call stack (most recent first):");

			for(Frame f = currentFrame; f != null; f = f.previousCallFrame) {
				//stdout.println("at " + f.function.name);
				stdout.println("\t" + f);
			}
		} else {
			stdout.println("No call stack available");
		}
	}

}
