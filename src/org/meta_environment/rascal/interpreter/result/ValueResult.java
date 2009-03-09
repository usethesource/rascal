package org.meta_environment.rascal.interpreter.result;


import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;


public class ValueResult<T extends IValue> extends AbstractResult<T> {

	public ValueResult(Type type, T value) {
		super(type, value);
	}
	
	public ValueResult(Type type, T value, Iterator<AbstractResult<IValue>> iter) {
		super(type, value, iter);
	}
	
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



}
