package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class RascalTypeException extends RascalException {
	
    
	public RascalTypeException(String message) {
		super(message);
	}
	
	public RascalTypeException(String message, AbstractAST node) {
		super(message, node);
	}
	
	public RascalTypeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
	
	
}
