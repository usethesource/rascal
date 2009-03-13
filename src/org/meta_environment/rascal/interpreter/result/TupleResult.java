package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class TupleResult extends ValueResult<ITuple> {
	
	public TupleResult(Type type, ITuple tuple) {
		super(type, tuple);
	}

	@Override
	public <U extends IValue> AbstractResult<U> fieldAccess(String name, TypeStore store) {
			if (!getType().hasFieldNames()) {
				// TODO ast?
				throw new UndeclaredFieldError(name, getType(), null);
			}
			try {
				int index = getType().getFieldIndex(name);
				Type type = getType().getFieldType(index);
				return makeResult(type, getValue().get(index));
			} 
			catch (UndeclaredFieldException e){
				// TODO: ast argument
				throw new UndeclaredFieldError(name, getType(), null);
			}
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> result) {
		return result.compareTuple(this);
	}
	
	///

	
	@Override
	public <U extends IValue> AbstractResult<U> compareTuple(TupleResult that) {
		// Note reversed args
		ITuple left = that.getValue();
		ITuple right = this.getValue();
		int compare = new Integer(left.arity()).compareTo(right.arity());
		if (compare != 0) {
			return makeIntegerResult(compare);
		}
		for (int i = 0; i < left.arity(); i++) {
			compare = compareValues(left.get(i), right.get(i));
			if (compare != 0) {
				return makeIntegerResult(compare);
			}
		}
		return makeIntegerResult(0);
	}
	
}
