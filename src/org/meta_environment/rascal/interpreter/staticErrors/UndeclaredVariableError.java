package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UndeclaredVariableError extends StaticError {
	private static final long serialVersionUID = -5617996489458337612L;

	public UndeclaredVariableError(String name, AbstractAST ast) {
		super("Undeclared variable: " + name, ast);
		printStackTrace();
	}

}
