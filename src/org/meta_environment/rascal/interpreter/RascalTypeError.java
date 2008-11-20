package org.meta_environment.rascal.interpreter;

public class RascalTypeError extends RuntimeException {
	private static final long serialVersionUID = 3715516757858886671L;

	public RascalTypeError(String message) {
		super(message);
	}
	
	public RascalTypeError(String message, Throwable cause) {
		super(message, cause);
	}
}
