package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class ExpressionError extends Error {
	private static final long serialVersionUID = 5711122111221677771L;
	
	public ExpressionError(String message, AbstractAST node) {
		super("ExpressionError", message, node);
	}
	
	public ExpressionError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
