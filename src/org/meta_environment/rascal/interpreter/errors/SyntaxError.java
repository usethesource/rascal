package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class SyntaxError extends Error {
	private static final long serialVersionUID = 333331541118811177L;
	 
	public SyntaxError(String message) {
		super(null, message);
	}
	
	public SyntaxError(String message, AbstractAST node) {
		super("SyntaxError", message, node);
	}
	
	public SyntaxError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
