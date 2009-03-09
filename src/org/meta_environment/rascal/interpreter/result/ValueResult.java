package org.meta_environment.rascal.interpreter.result;


import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.exceptions.TypeErrorException;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;


public class ValueResult<T extends IValue> extends AbstractResult<T> {

	public ValueResult(Type type, T value) {
		super(type, value);
	}
	
	public ValueResult(Type type, T value, Iterator<AbstractResult<IValue>> iter) {
		super(type, value, iter);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> equals(AbstractResult<V> that) {
		if (!getType().comparable(that.getType())) {
			throw new TypeErrorException("Arguments of equals have incomparable types: " + getType() + " and " + that.getType(), null);
		}
		IBool bool = getValueFactory().bool(((IInteger)this.compare(that).getValue()).intValue() == 0);
		return makeResult(getTypeFactory().boolType(), bool);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> that) {
		// the default fall back implementation for IValue-based results
		int result = getType().toString().compareTo(that.getType().toString());
		return makeIntegerResult(result);
	}
	
	//////
	
	@Override
	protected <U extends IValue> AbstractResult<U> inSet(SetResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> notInSet(SetResult s) {
		return s.notElementOf(this);
	}
	
	
	@Override
	protected <U extends IValue> AbstractResult<U> inList(ListResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> notInList(ListResult s) {
		return s.notElementOf(this);
	}
	
	
	@Override
	protected <U extends IValue> AbstractResult<U> addSet(SetResult s) {
		return s.addElement(this);
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> subtractSet(SetResult s) {
		return s.removeElement(this);
	}

	@Override
	protected <U extends IValue> AbstractResult<U> addList(ListResult s) {
		return s.appendElement(this);
	}

	@Override
	protected <U extends IValue> AbstractResult<U> subtractList(ListResult s) {
		return s.removeElement(this);
	}

	protected int compareValues(IValue left, IValue right) {
		AbstractResult<IValue> leftResult = makeResult(left.getType(), left);
		AbstractResult<IValue> rightResult = makeResult(right.getType(), right);
		AbstractResult<IValue> resultResult = leftResult.compare(rightResult);
		// compare always returns IntegerResult so we can cast its value.
		return ((IInteger)resultResult.getValue()).intValue();
	}



}
