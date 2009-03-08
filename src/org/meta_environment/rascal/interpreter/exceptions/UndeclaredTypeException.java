package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class UndeclaredTypeException extends TypeErrorException {
	private static final long serialVersionUID = -2394719759439179575L;
	private String name;

	public UndeclaredTypeException(String name, AbstractAST ast) {
		super("Undeclared type " + name, ast);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
