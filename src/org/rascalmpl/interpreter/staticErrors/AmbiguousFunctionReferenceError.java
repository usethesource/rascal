package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class AmbiguousFunctionReferenceError extends StaticError {
	private static final long serialVersionUID = -1800429914313750634L;

	public AmbiguousFunctionReferenceError(String name, AbstractAST ast) {
		super("Ambiguous unqualified function name: " + name, ast);
	}

}
