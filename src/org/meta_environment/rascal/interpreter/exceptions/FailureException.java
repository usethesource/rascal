package org.meta_environment.rascal.interpreter.exceptions;

public class FailureException extends RuntimeException {
	private static final long serialVersionUID = 2774285953244945424L;
	
	private final String label;
	
	public FailureException() {
		super();
		
		label = null;
	}
	
	public FailureException(String label) {
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
