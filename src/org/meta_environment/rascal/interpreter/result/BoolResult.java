package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.meta_environment.ValueFactoryFactory;

public class BoolResult extends ElementResult {

	private IBool bool;
	
	public BoolResult(IBool bool) {
		this(null, bool);
	}
		
	public BoolResult(Iterator<AbstractResult> iter, IBool bool) {
		super(iter);
		this.bool = bool;
	}
	
	public BoolResult(boolean b) {
		this(ValueFactoryFactory.getValueFactory().bool(b));
	}
	
	public BoolResult(Iterator<AbstractResult> iter, boolean b){
		this(iter, ValueFactoryFactory.getValueFactory().bool(b));
	}

	@Override
	public IBool getValue() {
		return bool;
	}
	
}
