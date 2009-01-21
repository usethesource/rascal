package org.meta_environment.rascal.interpreter.exceptions;

/*
 * This class is for representing runtime errors during the execution of
 * Rascal programs. Examples are subscript out of bounds and the like.
 */

public class RascalRunTimeError extends RuntimeException {
	
	private static final long serialVersionUID = 3715676299644311671L;
	
	public RascalRunTimeError(String message) {
		super(message);
	}
	
	public RascalRunTimeError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
