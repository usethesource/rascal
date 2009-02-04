package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.interpreter.env.EvalResult;

public class InsertException extends RuntimeException {
	private static final long serialVersionUID = -6601026099925601817L;
    
	private final EvalResult value;
	
	public InsertException(){
    	super();
    	
    	this.value = null;
    }
    
    public InsertException(EvalResult value){
    	super();
    	
    	this.value = value;
    }
	
	public EvalResult getValue() {
		return value;
	}
}
