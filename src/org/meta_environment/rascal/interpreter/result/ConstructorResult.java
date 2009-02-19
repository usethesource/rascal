package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IConstructor;

public class ConstructorResult extends NodeResult {

	public ConstructorResult(IConstructor cons) {
		super(cons);
	}
	
	public IConstructor getValue() {
		return (IConstructor)super.getValue();
	}

}
