package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UninitializedVariableError extends Error {
	
	private static final long serialVersionUID = -44225561154564288L;
	
	public UninitializedVariableError(String message, AbstractAST node) {
		super("UninitializedVariableError", message, node);
	}
}
