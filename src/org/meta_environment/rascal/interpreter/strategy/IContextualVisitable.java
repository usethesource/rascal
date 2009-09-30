package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;

public interface IContextualVisitable extends IVisitable {
	
	public void updateContext(IValue oldvalue, IValue newvalue);
		
	public IValue getContext();
	
	public void setContext(IValue value);


}
