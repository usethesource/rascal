package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchAnnotationError extends Error {
	
	private static final long serialVersionUID = -7406667412199993333L;
	 
	public NoSuchAnnotationError(String message) {
		super(null, message);
	}
	
	public NoSuchAnnotationError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public NoSuchAnnotationError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
