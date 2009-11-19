package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class InvalidComparisonError extends StaticError {
	private static final long serialVersionUID = -2125338648705520138L;

	public InvalidComparisonError(String from, String to, AbstractAST ast) {
		super("Invalid comparison between " + from + " and " + to, ast);
	}
}
