package org.meta_environment.rascal.interpreter;

public class RascalUndefinedValue extends RuntimeException {
	private static final long serialVersionUID = -7290225483329876543L;
	
	private String message;
	
    public RascalUndefinedValue(String message) {
    	this.message = message;
    };
	
	@Override
	public String getMessage() {
		return message;
	}
}
