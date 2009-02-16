package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class TypeError extends Error {
	private static final long serialVersionUID = 33333767154564288L;
    
	public TypeError(String message) {
		super(null, message);
	}
	
	public TypeError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public TypeError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
