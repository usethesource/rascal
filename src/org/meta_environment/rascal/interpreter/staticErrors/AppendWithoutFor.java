package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class AppendWithoutFor extends StaticError {
	private static final long serialVersionUID = 4840304460180936759L;

	public AppendWithoutFor(AbstractAST ast) {
		super("append statement without enclosing for loop", ast);
	}
}
