package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class VoidResult extends AbstractResult {

	public VoidResult() {
		super(null, null);
		this.type = getTypeFactory().voidType();
	}
	
	@Override
	public IValue getValue() {
		throw new ImplementationError("void results have no value");
	}

}
