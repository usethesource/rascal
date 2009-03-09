package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationException;

public class CollectionResult<T extends IValue> extends ValueResult<T> {
	/*
	 * These methods are called for expressions like:
	 * 1 + [2,3]:   1.add([2,3]) --> [2,3].addInt(1) --> [2,3].insertElement(1) --> [1,2,3]
	 * etc.
	 */

	CollectionResult(Type type, T value) {
		super(type, value);
	}

	
	@Override
	protected <U extends IValue> AbstractResult<U> addReal(RealResult n) {
		return insertElement(n);
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> addInteger(IntegerResult n) {
		return insertElement(n);
	}

	@Override
	protected <U extends IValue> AbstractResult<U> addString(StringResult n) {
		return insertElement(n);
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> addBool(BoolResult n) {
		return insertElement(n);
	}
	
	@Override 
	protected <U extends IValue> AbstractResult<U> addTuple(TupleResult t) {
		return insertElement(t);
	}
	
	<U extends IValue, V extends IValue> AbstractResult<U> insertElement(ValueResult<V> result) {
		throw new ImplementationException("this method should be specialized in subclasses");
	}


	protected <U extends IValue> Type resultTypeWhenAddingElement(ValueResult<U> that) {
		Type t1 = type.getElementType();
		Type t2 = that.type;
		return TypeFactory.getInstance().listType(t1.lub(t2));
	}
	
	
}
