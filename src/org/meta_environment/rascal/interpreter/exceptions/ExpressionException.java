package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class ExpressionException extends RascalException {
	private static final long serialVersionUID = 5711122111221677771L;
	
	public ExpressionException(String message, AbstractAST node) {
		super("ExpressionException", message, node);
	}
	
}
