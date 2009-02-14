package org.meta_environment.rascal.interpreter.control_exceptions;

public class FailureControlException extends RuntimeException {
	private static final long serialVersionUID = 2774285953244945424L;
	
	private final String label;
	
	public FailureControlException() {
		super();
		
		label = null;
	}
	
	public FailureControlException(String label) {
		super();
		
		this.label = label;
	}
	
	public boolean hasLabel() {
		return label != null;
	}
	
	public String getLabel() {
		return label;
	}
}
