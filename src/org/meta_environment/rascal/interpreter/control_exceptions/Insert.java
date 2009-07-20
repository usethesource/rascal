package org.meta_environment.rascal.interpreter.control_exceptions;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.matching.MatchPattern;
import org.meta_environment.rascal.interpreter.result.Result;

public class Insert extends ControlException {
	private static final long serialVersionUID = -6601026099925601817L;
    
	private final Result<IValue> value;
	private MatchPattern mp;
	
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
    
    public Insert(Result<IValue> value, MatchPattern mp){
    	super();
    	
    	this.value = value;
    	this.mp = mp;
    }
	
	public Result<IValue> getValue() {
		return value;
	}
	
	public MatchPattern getMatchPattern(){
		return mp;
	}
	
	public void setMatchPattern(MatchPattern mp){
		this.mp = mp;
	}
}
