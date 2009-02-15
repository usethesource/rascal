package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class AssertionFails extends RascalException{
	private static final long serialVersionUID = 5709066049663349481L;
	
	public AssertionFails(String message) {
		super(null, message);
	}

	public AssertionFails(String message, AbstractAST ast) {
		super(message, null, ast);
	}
}
