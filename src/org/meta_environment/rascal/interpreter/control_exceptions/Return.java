package org.meta_environment.rascal.interpreter.control_exceptions;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.result.Result;

public class Return extends ControlException {
	private static final long serialVersionUID = -6601026099925601817L;
    private Result<IValue> value;
	
    public Return(){
    	super();
    	
    	this.value = null;
    }
    
    public Return(Result<IValue> value){
    	super();
    	
    	this.value = value;
    }
	
	public Result<IValue> getValue() {
		return value;
	}
}
