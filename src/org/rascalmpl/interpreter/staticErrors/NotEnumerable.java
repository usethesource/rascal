package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class NotEnumerable extends StaticError {
	public NotEnumerable(String type, AbstractAST ast) {
		super(type + " is not enumerable", ast);
	}

	private static final long serialVersionUID = 7512965714991339935L;

}
