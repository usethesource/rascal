package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class RascalTypeError extends RascalError {
	
    
	public RascalTypeError(String message) {
		super(message);
	}
	
	public RascalTypeError(String message, AbstractAST node) {
		super(message, node);
	}
	
	public RascalTypeError(String message, Throwable cause) {
		super(message, cause);
	}
}
