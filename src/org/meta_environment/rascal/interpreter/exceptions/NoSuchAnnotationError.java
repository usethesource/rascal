package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchAnnotationError extends RascalException {
	 
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
