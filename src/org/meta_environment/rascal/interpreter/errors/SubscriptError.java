package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class SubscriptError extends Error {
	 
	public SubscriptError(String message) {
		super(null, message);
	}
	
	public SubscriptError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public SubscriptError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
