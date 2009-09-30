package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;

public interface IContextualVisitable extends IVisitable {
	
	public void updateContext(IValue oldvalue, IValue newvalue);
	
	public void initContext(IValue v);
	
	public IValue getContext();


}
