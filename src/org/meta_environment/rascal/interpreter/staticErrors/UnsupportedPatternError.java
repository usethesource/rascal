package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UnsupportedPatternError extends StaticError {
	private static final long serialVersionUID = 3463736182521201443L;

	public UnsupportedPatternError(String op, AbstractAST ast) {
		super(op + " is not allowed in patterns", ast);
	}
}
