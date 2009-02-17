package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class SubscriptError extends Error {
	private static final long serialVersionUID = 77321541118811177L;
	
	public SubscriptError(String message, AbstractAST node) {
		super("SubscriptError", message, node);
	}
	
	public SubscriptError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
