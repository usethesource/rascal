package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class IOError extends RascalException {
	 
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
