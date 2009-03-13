package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UnguardedInsertError extends StaticError {
	private static final long serialVersionUID = -3024435867811407010L;

	public UnguardedInsertError(AbstractAST ast) {
		super("Return statement outside of function scope", ast);
	}

}
