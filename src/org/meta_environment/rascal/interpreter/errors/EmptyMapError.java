package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class EmptyMapError extends Error {
	private static final long serialVersionUID = 555779952495556681L;
	
	public EmptyMapError(String message, AbstractAST node) {
		super("EmptyMapError", message, node);
	}
}
