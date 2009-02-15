package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchFunctionError extends Error {
	
	private static final long serialVersionUID = -3215674987633177L;
	 
	public NoSuchFunctionError(String message) {
		super(null, message);
	}
	
	public NoSuchFunctionError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public NoSuchFunctionError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
