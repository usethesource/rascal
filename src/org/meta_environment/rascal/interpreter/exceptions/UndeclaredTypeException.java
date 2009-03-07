package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class UndeclaredTypeException extends TypeErrorException {
	private static final long serialVersionUID = -2394719759439179575L;

	public UndeclaredTypeException(String name, AbstractAST ast) {
		super("Undeclared type " + name, ast);
	}
}
