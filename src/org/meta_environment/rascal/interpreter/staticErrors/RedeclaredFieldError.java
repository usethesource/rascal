package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class RedeclaredFieldError extends StaticError {
	private static final long serialVersionUID = -8731371039162112981L;

	public RedeclaredFieldError(String name, AbstractAST ast) {
		super("Redeclared field: " + name, ast);
	}

}
