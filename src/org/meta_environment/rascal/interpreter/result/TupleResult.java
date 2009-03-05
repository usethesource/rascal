package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.type.Type;

public class TupleResult extends ValueResult<ITuple> {
	
	public TupleResult(Type type, ITuple tuple) {
		super(type, tuple);
	}


	// TODO: field selection and projection
	
}
