package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UndeclaredVariableError extends StaticError {
	private static final long serialVersionUID = -5617996489458337612L;
	private final String name;

	public UndeclaredVariableError(String name, AbstractAST ast) {
		super("Undeclared variable, function or constructor: " + name, ast);
		this.name = name;
		printStackTrace();
	}

	public String getName() {
		return name;
	}

}
