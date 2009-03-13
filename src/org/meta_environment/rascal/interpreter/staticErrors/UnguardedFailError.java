package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UnguardedFailError extends StaticError {
	private static final long serialVersionUID = -3024435867811407010L;

	public UnguardedFailError(AbstractAST ast) {
		super("Fail statement not guarded by conditional context", ast);
	}

}
