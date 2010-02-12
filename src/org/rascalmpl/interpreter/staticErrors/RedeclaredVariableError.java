package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class RedeclaredVariableError extends StaticError {
	private static final long serialVersionUID = -5617996489458337612L;

	public RedeclaredVariableError(String name, AbstractAST ast) {
		super("Redeclared variable: " + name, ast);
	}

}
