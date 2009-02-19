package org.meta_environment.rascal.interpreter.control_exceptions;

import org.meta_environment.rascal.interpreter.result.Result;

public class ReturnControlException extends RuntimeException {
	private static final long serialVersionUID = -6601026099925601817L;
    private Result value;
	
    public ReturnControlException(){
    	super();
    	
    	this.value = null;
    }
    
    public ReturnControlException(Result value){
    	super();
    	
    	this.value = value;
    }
	
	public Result getValue() {
		return value;
	}
}
