package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchFieldError extends Error {
	 
	public NoSuchFieldError(String message) {
		super("NoSuchFieldError", message);
	}
	
	public NoSuchFieldError(String message, AbstractAST node) {
		super("NoSuchFieldError", message, node);
	}
	
	public NoSuchFieldError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
