package org.meta_environment.rascal.interpreter.control_exceptions;

import org.meta_environment.rascal.interpreter.result.Result;

public class Return extends RuntimeException {
	private static final long serialVersionUID = -6601026099925601817L;
    private Result value;
	
    public Return(){
    	super();
    	
    	this.value = null;
    }
    
    public Return(Result value){
    	super();
    	
    	this.value = value;
    }
	
	public Result getValue() {
		return value;
	}
}
