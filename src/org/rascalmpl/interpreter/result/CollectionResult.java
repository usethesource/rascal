package org.rascalmpl.interpreter.result;


import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public class CollectionResult<T extends IValue> extends ElementResult<T> {
	/*
	 * These methods are called for expressions like:
	 * 1 + [2,3]:   1.add([2,3]) --> [2,3].addInt(1) --> [2,3].insertElement(1) --> [1,2,3]
	 * etc.
	 */

	CollectionResult(Type type, T value, IEvaluatorContext ctx) {
		super(type, value, null, ctx);
	}

	
	@Override
	protected <U extends IValue> Result<U> addReal(RealResult n) {
		return insertElement(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> addInteger(IntegerResult n) {
		return insertElement(n);
	}

	@Override
	protected <U extends IValue> Result<U> addString(StringResult n) {
		return insertElement(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> addBool(BoolResult n) {
		return insertElement(n);
	}
	
	@Override 
	protected <U extends IValue> Result<U> addTuple(TupleResult t) {
		return insertElement(t);
	}
	
	<U extends IValue, V extends IValue> Result<U> insertElement(ElementResult<V> result) {
		throw new ImplementationError("this method should be specialized in subclasses");
	}


//	protected <U extends IValue> Type resultTypeWhenAddingElement(ElementResult<U> that) {
//		Type t1 = getType().getElementType();
//		Type t2 = that.getType();
//		getType().
//		return getTypeFactory().(t1.lub(t2));
//	}

	
}
