package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

/**
 * Exception used for when the implementation detects that it has a bug.
 */
public class ImplementationError extends RascalException {
	private static final long serialVersionUID = -8740312542969306482L;

	public ImplementationError(String message) {
		super(null, message);
	}
	
	public ImplementationError(String message, AbstractAST ast) {
		super(message, null, ast);
	}
	
	public ImplementationError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
	
}
