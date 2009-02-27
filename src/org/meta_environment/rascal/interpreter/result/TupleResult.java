package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.type.Type;

public class TupleResult extends ValueResult {
	
	private ITuple tuple;
	
	public TupleResult(Type type, ITuple tuple) {
		super(type, tuple);
		this.tuple = tuple;
	}

	@Override
	public ITuple getValue() {
		return tuple;
	}

	// TODO: field selection and projection
	
}
