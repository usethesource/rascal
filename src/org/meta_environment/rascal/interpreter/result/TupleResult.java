package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;

public class TupleResult extends AbstractResult {
	
	private ITuple tuple;
	
	public TupleResult(ITuple tuple) {
		this.tuple = tuple;
	}

	@Override
	public ITuple getValue() {
		return tuple;
	}

	
	
}
