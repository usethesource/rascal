package org.meta_environment.rascal.interpreter.result;


import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;


public class ValueResult extends AbstractResult {

	private IValue value;

	
	public ValueResult(Type type, IValue value) {
		super(type, value);
		this.value = value;
	}
	
	ValueResult(Type type, IValue value, Iterator<AbstractResult> iter) {
		super(type, value, iter);
	}
	
	// TODO: do type checking on element types here.
	
	@Override
	public IValue getValue() {
		return value;
	}


	@Override
	protected BoolResult inSet(SetResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected BoolResult notInSet(SetResult s) {
		return s.notElementOf(this);
	}
	
	
	@Override
	protected BoolResult inList(ListResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected BoolResult notInList(ListResult s) {
		return s.notElementOf(this);
	}
	
	
	@Override
	protected SetResult addSet(SetResult s) {
		return s.addElement(this);
	}
	
	@Override
	protected SetResult subtractSet(SetResult s) {
		return s.removeElement(this);
	}

	@Override
	protected ListResult addList(ListResult s) {
		return s.appendElement(this);
	}

	@Override
	protected ListResult subtractList(ListResult s) {
		return s.removeElement(this);
	}



}
