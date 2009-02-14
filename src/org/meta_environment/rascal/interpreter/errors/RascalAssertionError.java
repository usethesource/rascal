package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class RascalAssertionError extends RascalError {
	private static final long serialVersionUID = 5709066049663349481L;
	
	public RascalAssertionError(String message) {
		super(message);
	}

	public RascalAssertionError(String message, AbstractAST ast) {
		super(message, ast);
	}
	
	public RascalAssertionError(String message, Throwable cause) {
		super(message, cause);
	}
}
