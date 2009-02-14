package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

/**
 * Exception used for when the implementation detects that it has a bug.
 */
public class RascalImplementationException extends RascalException {
	private static final long serialVersionUID = -8740312542969306482L;

	public RascalImplementationException(String message) {
		super(message);
	}
	
	public RascalImplementationException(String message, AbstractAST ast) {
		super(message, ast);
	}
	
	public RascalImplementationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
	
}
