package org.meta_environment.rascal.interpreter.asserts;


/**
 * Exception used for when the implementation detects that it has a bug.
 * This is a separate class of exceptions from static errors or run-time exceptions.
 */
public final class ImplementationError extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;

	public ImplementationError(String message, Throwable cause) {
		super("Unexpected error in Rascal interpreter: " + message + (cause != null ? (" caused by " + cause.getMessage()) : ""));
	}
	
	// TODO replace these by asserts?
	public ImplementationError(String message) {
		this(message, null);
	}
}
