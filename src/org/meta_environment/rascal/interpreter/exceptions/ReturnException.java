package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.interpreter.env.Result;

public class ReturnException extends RuntimeException {
	private static final long serialVersionUID = -6601026099925601817L;
    private Result value;
	
    public ReturnException(){
    	super();
    	
    	this.value = null;
    }
    
    public ReturnException(Result value){
    	super();
    	
    	this.value = value;
    }
	
	public Result getValue() {
		return value;
	}
}
