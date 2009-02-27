package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class SyntaxError extends Error {
	private static final long serialVersionUID = 333331541118811177L;
	 
	//TODO: remove
	public SyntaxError(String message) {
		super("SyntaxError", message);
	}
	
	public SyntaxError(String message, AbstractAST node) {
		super("SyntaxError", message, node);
	}
}
