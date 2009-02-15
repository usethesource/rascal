package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class SyntaxError extends RascalException {
	 
	public SyntaxError(String message) {
		super(null, message);
	}
	
	public SyntaxError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public SyntaxError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
