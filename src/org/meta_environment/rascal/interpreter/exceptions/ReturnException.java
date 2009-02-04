package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.interpreter.env.EvalResult;

public class ReturnException extends RuntimeException {
	private static final long serialVersionUID = -6601026099925601817L;
    private EvalResult value;
	
    public ReturnException(){
    	super();
    	
    	this.value = null;
    }
    
    public ReturnException(EvalResult value){
    	super();
    	
    	this.value = value;
    }
	
	public EvalResult getValue() {
		return value;
	}
}
