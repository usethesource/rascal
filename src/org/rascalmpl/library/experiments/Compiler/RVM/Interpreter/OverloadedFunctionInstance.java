package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class OverloadedFunctionInstance {
	
	final int[] functions;
	final int[] constructors;
	final Frame env;
	
	public OverloadedFunctionInstance(int[] functions, int[] constructors, Frame env) {
		this.functions = functions;
		this.constructors = constructors;
		this.env =  env;
	}

}
