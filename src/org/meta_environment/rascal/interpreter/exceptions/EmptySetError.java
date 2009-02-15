package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class EmptySetError extends RascalException {
	 
	public EmptySetError(String message) {
		super(null, message);
	}
	
	public EmptySetError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public EmptySetError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
