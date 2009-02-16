package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class AssertionError extends Error{
	private static final long serialVersionUID = 5709066049663349481L;
	
	public AssertionError(String message) {
		super("AssertionError", message);
	}

	public AssertionError(String message, AbstractAST ast) {
		super("AssertionError", message, ast);
	}
}
