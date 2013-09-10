package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class OverloadedFunctionInstance {
	
	final int[] functions;
	final Frame env;
	
	public OverloadedFunctionInstance(int[] functions, Frame env) {
		this.functions = functions;
		this.env =  env;
	}

}
