package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class BoolResult extends AbstractResult {

	private IBool bool;
	
	public BoolResult(IBool bool) {
		this.bool = bool;
	}
	
	public BoolResult(Iterator<AbstractResult> iter, IBool bool) {
		super(iter);
		this.bool = bool;
	}

	public BoolResult(Iterator<AbstractResult> iter, boolean b){
		this(iter, ValueFactoryFactory.getValueFactory().bool(b));
	}

	@Override
	public IBool getValue() {
		return bool;
	}

	@Override
	protected AbstractResult addSet(SetResult s) {
		return s.addBool(this);
	}
	
	@Override
	protected AbstractResult subtractSet(SetResult s) {
		throw new ImplementationError("NYI");
		//return new SetResult(s.getSet().delete(this));
	}

	@Override
	protected ListResult addList(ListResult s) {
		return s.appendResult(this);
	}

	
	public IBool getBool() {
		return bool;
	}
	
}
