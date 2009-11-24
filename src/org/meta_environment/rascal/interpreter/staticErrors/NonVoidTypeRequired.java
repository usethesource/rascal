package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class NonVoidTypeRequired extends StaticError {
	private static final long serialVersionUID = 6595223256368783269L;

	public NonVoidTypeRequired(AbstractAST ast) {
		super("Non-void type required", ast);
	}

}
