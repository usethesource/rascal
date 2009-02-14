package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

/**
 * Exception used for when the implementation detects that it has a bug.
 */
public class RascalImplementationError extends RascalError {
	private static final long serialVersionUID = -8740312542969306482L;

	public RascalImplementationError(String message) {
		super(message);
	}
	
	public RascalImplementationError(String message, AbstractAST ast) {
		super(message, ast);
	}
	
	public RascalImplementationError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
