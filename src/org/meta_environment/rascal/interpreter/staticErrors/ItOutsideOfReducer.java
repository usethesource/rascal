package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class ItOutsideOfReducer extends StaticError {
	private static final long serialVersionUID = -6837835628108765920L;
	
	public ItOutsideOfReducer(AbstractAST ast) {
		super("Use of 'it' special variable is only allowed within reducers", ast);
	}


	
}
