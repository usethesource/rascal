package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchFunctionError extends Error {
	private static final long serialVersionUID = -3215674987633177L;
	
	public NoSuchFunctionError(String message, AbstractAST node) {
		super("NoSuchFunctionError", message, node);
	}

}
