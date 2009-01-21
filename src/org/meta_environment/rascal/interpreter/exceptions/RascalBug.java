package org.meta_environment.rascal.interpreter.exceptions;

/**
 * Exception used for when the implementation detects that it has a bug.
 */
public class RascalBug extends RuntimeException {
	private static final long serialVersionUID = -8740312542969306482L;

	public RascalBug(String message) {
		super(message);
	}
	
	public RascalBug(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
