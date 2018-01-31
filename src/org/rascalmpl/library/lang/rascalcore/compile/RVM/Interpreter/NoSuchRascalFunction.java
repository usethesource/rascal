package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class NoSuchRascalFunction extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	NoSuchRascalFunction(String signature){
		super(signature);
	}
}
