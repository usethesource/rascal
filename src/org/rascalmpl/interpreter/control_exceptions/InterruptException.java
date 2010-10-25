package org.rascalmpl.interpreter.control_exceptions;

public class InterruptException extends ControlException {
	private static final long serialVersionUID = -6244185056015873062L;
	private final String stackTrace;
	
	public InterruptException(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getRascalStackTrace() {
		return stackTrace;
	}
	
	@Override
	public String toString() {
		return "interrupted" + ((stackTrace != null && stackTrace.length() != 0) ? (": " + stackTrace) : "");
	}
}
