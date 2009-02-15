package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class ExpressionError extends RascalException {
	 
	public ExpressionError(String message) {
		super(null, message);
	}
	
	public ExpressionError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public ExpressionError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
