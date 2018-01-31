package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class Failure {
	
	private static final Failure instance = new Failure(); 
	
	public static Failure getInstance() {
		return instance;
	}

}
