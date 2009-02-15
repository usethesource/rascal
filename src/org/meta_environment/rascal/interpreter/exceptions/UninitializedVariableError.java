package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class UninitializedVariableError extends RascalException {
	 
	public UninitializedVariableError(String message) {
		super(null, message);
	}
	
	public UninitializedVariableError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public UninitializedVariableError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
