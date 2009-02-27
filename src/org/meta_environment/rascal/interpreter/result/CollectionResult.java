package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class CollectionResult extends ValueResult {
	/*
	 * These methods are called for expressions like:
	 * 1 + [2,3]:   1.add([2,3]) --> [2,3].addInt(1) --> [2,3].insertElement(1) --> [1,2,3]
	 * etc.
	 */

	CollectionResult(Type type, IValue value) {
		super(type, value);
	}

	
	@Override
	protected CollectionResult addReal(RealResult n) {
		return insertElement(n);
	}
	
	@Override
	protected CollectionResult addInteger(IntegerResult n) {
		return insertElement(n);
	}

	@Override
	protected CollectionResult addString(StringResult n) {
		return insertElement(n);
	}
	
	@Override
	protected CollectionResult addBool(BoolResult n) {
		return insertElement(n);
	}
	
	@Override 
	protected CollectionResult addTuple(TupleResult t) {
		return insertElement(t);
	}
	
	CollectionResult insertElement(ValueResult result) {
		throw new ImplementationError("this method should be specialized in subclasses");
	}
	
	
}
