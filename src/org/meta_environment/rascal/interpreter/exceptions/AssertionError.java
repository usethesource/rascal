package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class AssertionError extends RascalException{
	private static final long serialVersionUID = 5709066049663349481L;
	
	public AssertionError(String message) {
		super(null, message);
	}

	public AssertionError(String message, AbstractAST ast) {
		super(message, null, ast);
	}
}
