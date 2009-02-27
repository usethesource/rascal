package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.ITuple;

public class TupleResult extends ValueResult {
	
	private ITuple tuple;
	
	public TupleResult(ITuple tuple) {
		this.tuple = tuple;
	}

	@Override
	public ITuple getValue() {
		return tuple;
	}

	
	
}
