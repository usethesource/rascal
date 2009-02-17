package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class EmptySetError extends Error {
	private static final long serialVersionUID = 5777799911110496681L;
	
	public EmptySetError(String message, AbstractAST node) {
		super("EmptySetError", message, node);
	}
}
