package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class AppendWithoutLoop extends StaticError {
	private static final long serialVersionUID = 4840304460180936759L;

	public AppendWithoutLoop(AbstractAST ast) {
		super("append statement without enclosing loop", ast);
	}
}
