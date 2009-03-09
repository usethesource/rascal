package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;

public class BoolResult extends ValueResult<IBool> {

	public BoolResult(Type type, IBool bool) {
		this(type, bool, null);
	}
		
	public BoolResult(Type type, IBool bool, Iterator<AbstractResult<IValue>> iter) {
		super(type, bool, iter);
	}
	
	public BoolResult(boolean b) {
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b));
	}
	
	public BoolResult(boolean b, Iterator<AbstractResult<IValue>> iter){
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b), iter);
	}
	
	/////
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> that) {
		return that.compareBool(this);
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> compareBool(BoolResult that) {
		// note:  that <=> this
		BoolResult left = that;
		BoolResult right = this;
		boolean lb = left.getValue().getValue();
		boolean rb = right.getValue().getValue();
		int result = (lb == rb) ? 0 : ((!lb && rb) ? -1 : 1);
		return makeIntegerResult(result);
	}
	

}
