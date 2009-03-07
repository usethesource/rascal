package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

/**
 * Exception used for when the implementation detects that it has a bug.
 */
public class ImplementationException extends RascalException {
	private static final long serialVersionUID = -8740312542969306482L;

	//TODO: remove
	public ImplementationException(String message) {
		super(null, message);
	}
	
	public ImplementationException(String message, AbstractAST ast) {
		super("ImplementationException", message, ast);
	}
	
	public ImplementationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public boolean hasCause() {
		return getCause() != null;
	}
	
}
