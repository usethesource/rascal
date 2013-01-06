package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class NoKeywordParametersError extends StaticError {
	public NoKeywordParametersError(String name, AbstractAST ast) {
		super("constructor function " + name + " cannot be called with keyword parameters", ast);
	}

	private static final long serialVersionUID = 7512965714991339935L;

}
