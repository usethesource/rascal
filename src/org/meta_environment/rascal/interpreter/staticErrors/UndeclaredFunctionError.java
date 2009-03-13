package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UndeclaredFunctionError extends StaticError {
	private static final long serialVersionUID = -3215674987633177L;
	
	public UndeclaredFunctionError(String name, AbstractAST node) {
		super("Undeclared function: " + name, node);
	}
}
