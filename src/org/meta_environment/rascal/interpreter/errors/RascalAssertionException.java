package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class RascalAssertionException extends RascalException{
	private static final long serialVersionUID = 5709066049663349481L;
	
	public RascalAssertionException(String message) {
		super(message);
	}

	public RascalAssertionException(String message, AbstractAST ast) {
		super(message, ast);
	}
	/*
	public RascalAssertionError(String message, Throwable cause) {
		super(message, cause);
	}
	*/
}
