package org.meta_environment.rascal.interpreter.result;

public abstract class CollectionResult extends ElementResult {

	/*
	 * These methods are called for expressions like:
	 * 1 + [2,3]:   1.add([2,3]) --> [2,3].addInt(1) --> [2,3].insertElement(1) --> [1,2,3]
	 * etc.
	 */
	
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
	
	// TODO: make abstract? 
	abstract CollectionResult insertElement(ElementResult result);
	
	
}
