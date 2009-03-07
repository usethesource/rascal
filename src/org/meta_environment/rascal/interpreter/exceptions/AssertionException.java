package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class AssertionException extends RascalException{
	private static final long serialVersionUID = 5709066049663349481L;

	public AssertionException(String message, AbstractAST ast) {
		super("AssertionException", message, ast);
	}
}
