package org.rascalmpl.library.experiments.CoreRascal.RVM;

public class FunctionInstance {
	
	final Function function;
	final Frame env;
	
	public FunctionInstance(Function function, Frame env) {
		this.function = function;
		this.env = env;
	}

}
