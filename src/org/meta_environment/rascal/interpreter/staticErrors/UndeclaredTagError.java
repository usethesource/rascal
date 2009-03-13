package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UndeclaredTagError extends StaticError {
	private static final long serialVersionUID = -1739569131029513596L;

	public UndeclaredTagError(String name, AbstractAST ast) {
		super("Undeclared tag: " + name, ast);
	}
}
