package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.interpreter.env.Result;

public class InsertException extends RuntimeException {
	private static final long serialVersionUID = -6601026099925601817L;
    
	private final Result value;
	
	public InsertException(){
    	super();
    	
    	this.value = null;
    }
    
    public InsertException(Result value){
    	super();
    	
    	this.value = value;
    }
	
	public Result getValue() {
		return value;
	}
}
