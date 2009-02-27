package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.type.Type;

public class ConstructorResult extends NodeResult {

	public ConstructorResult(Type type, IConstructor cons) {
		super(type, cons);
	}
	
	public IConstructor getValue() {
		return (IConstructor)super.getValue();
	}

}
