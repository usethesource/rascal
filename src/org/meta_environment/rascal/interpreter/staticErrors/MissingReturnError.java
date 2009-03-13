package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class MissingReturnError extends StaticError {
	private static final long serialVersionUID = 6595223256368783269L;

	public MissingReturnError(AbstractAST ast) {
		super("Missing return statement", ast);
	}

}
