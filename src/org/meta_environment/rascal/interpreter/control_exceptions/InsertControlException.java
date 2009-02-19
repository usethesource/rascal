package org.meta_environment.rascal.interpreter.control_exceptions;

import org.meta_environment.rascal.interpreter.result.Result;

public class InsertControlException extends RuntimeException {
	private static final long serialVersionUID = -6601026099925601817L;
    
	private final Result value;
	
	public InsertControlException(){
    	super();
    	
    	this.value = null;
    }
    
    public InsertControlException(Result value){
    	super();
    	
    	this.value = value;
    }
	
	public Result getValue() {
		return value;
	}
}
