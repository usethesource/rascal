package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

/**
 * Exception used for when the implementation detects that it has a bug.
 */
public class ImplementationError extends Error {
	private static final long serialVersionUID = -8740312542969306482L;

	//TODO: remove
	public ImplementationError(String message) {
		super(null, message);
	}
	
	public ImplementationError(String message, AbstractAST ast) {
		super("ImplementationError", message, ast);
	}
	
	public ImplementationError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
	
}
