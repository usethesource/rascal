package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class NotEnumerableError extends StaticError {
	public NotEnumerableError(String type, AbstractAST ast) {
		super(type + " is not enumerable", ast);
	}

	private static final long serialVersionUID = 7512965714991339935L;

}
