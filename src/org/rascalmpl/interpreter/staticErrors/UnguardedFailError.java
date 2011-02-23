package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class UnguardedFailError extends StaticError {
	private static final long serialVersionUID = -3024435867811407010L;

	public UnguardedFailError(AbstractAST ast) {
		super("Failed without a default function, switch case or visit case", ast);
	}

}
