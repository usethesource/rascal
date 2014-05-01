package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class CompilerError extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CompilerError(String msg) {
		super("Internal compiler error: " + msg);
	}

}
