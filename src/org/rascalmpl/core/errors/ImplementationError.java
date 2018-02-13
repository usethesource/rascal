package org.rascalmpl.core.errors;

import io.usethesource.vallang.ISourceLocation;

/**
 * Exception used for when the implementation detects that it has a bug.
 * This is a separate class of exceptions from static errors or run-time exceptions.
 */
public final class ImplementationError extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;
	private final ISourceLocation location;
	

	public ImplementationError(String message, Throwable cause) {
		super("Unexpected error in Rascal interpreter: " + message + " caused by " + cause.getMessage());
		this.initCause(cause);
		this.location = null;
	}
	
	// TODO replace these by asserts?
	public ImplementationError(String message) {
		super("Unexpected error in Rascal interpreter: " + message);
		this.location = null;
	}

	public ImplementationError(String message, ISourceLocation location) {
		super(message);
		this.location = location;
	}
	
	@Override
	public String getMessage() {
		if (location != null) return location + ":" + super.getMessage();
		
		return super.getMessage();
	}
}

