package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class RascalTypeException extends RascalException {
	
    
	public RascalTypeException(String message) {
		super(message);
	}
	
	public RascalTypeException(String message, AbstractAST node) {
		super(message, node);
	}
	/*
	public RascalTypeError(String message, Throwable cause) {
		super(message, cause);
	}

	public boolean hasCause() {
		// TODO Auto-generated method stub
		return getCause() != null;
	}
	*/
}
