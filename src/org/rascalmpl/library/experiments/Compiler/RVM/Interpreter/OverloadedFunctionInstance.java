package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class OverloadedFunctionInstance {
	
	final Integer[] functions;
	final Frame env;
	
	public OverloadedFunctionInstance(Integer[] functions, Frame env) {
		this.functions = functions;
		this.env =  env;
	}

}
