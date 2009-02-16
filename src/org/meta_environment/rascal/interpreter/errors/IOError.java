package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class IOError extends Error {
	private static final long serialVersionUID = 674066674121144282L;
	 
	public IOError(String message) {
		super(null, message);
	}
	
	public IOError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public IOError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
