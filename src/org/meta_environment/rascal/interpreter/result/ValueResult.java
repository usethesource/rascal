package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;


public class ValueResult extends AbstractResult {

	private IValue value;

	
	public ValueResult(IValue value) {
		this.value = value;
	}
	
	public ValueResult(Iterator<AbstractResult> iter) {
		// TODO: what to do with this constructor.
		super(iter);
	}
	
	// TODO: do type checking on element types here.
	
	@Override
	public IValue getValue() {
		return value;
	}

	ValueResult() {	
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
