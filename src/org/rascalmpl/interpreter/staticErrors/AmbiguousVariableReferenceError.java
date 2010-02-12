package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class AmbiguousVariableReferenceError extends StaticError {
	private static final long serialVersionUID = -6398515695309309263L;

	public AmbiguousVariableReferenceError(String name, AbstractAST ast) {
		super("Ambiguous unqualified variable name " + name, ast);
	}
}
