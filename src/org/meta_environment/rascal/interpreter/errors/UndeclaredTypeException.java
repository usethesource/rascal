package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UndeclaredTypeException extends TypeError {
	private static final long serialVersionUID = -2394719759439179575L;

	public UndeclaredTypeException(String name, AbstractAST ast) {
		super("Undeclared type " + name, ast);
	}
}
