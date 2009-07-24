package org.meta_environment.rascal.interpreter.control_exceptions;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.matching.IBooleanResult;
import org.meta_environment.rascal.interpreter.result.Result;

public class Insert extends ControlException {
	private static final long serialVersionUID = -6601026099925601817L;
    
	private final Result<IValue> value;
	private IBooleanResult mp;
	
	public Insert(){
    	super();
    	
    	this.value = null;
    	this.mp = null;
    }
    
    public Insert(Result<IValue> value){
    	super();
    	
    	this.value = value;
    	this.mp = null;
    }
    
    public Insert(Result<IValue> value, IBooleanResult mp){
    	super();
    	
    	this.value = value;
    	this.mp = mp;
    }
	
	public Result<IValue> getValue() {
		return value;
	}
	
	public IBooleanResult getMatchPattern(){
		return mp;
	}
	
	public void setMatchPattern(IBooleanResult mp){
		this.mp = mp;
	}
}
