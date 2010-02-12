package org.rascalmpl.interpreter.control_exceptions;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.result.Result;

public class Return extends ControlException {
	private static final long serialVersionUID = -6601026099925601817L;
    private final Result<IValue> value;
	private ISourceLocation loc;
	
    public Return(ISourceLocation loc){
    	super();
    	
    	this.loc = loc;
    	this.value = null;
    }
    
    public Return(Result<IValue> value, ISourceLocation loc){
    	super();
    	
    	this.loc = loc;
    	this.value = value;
    }
	
	public Result<IValue> getValue() {
		return value;
	}
	
	/**
	 * Points to the location of the expression of the return, for use in error messages
	 * @return
	 */
	public ISourceLocation getLocation() {
		return loc;
	}
}
