package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.ValueFactoryFactory;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class RealResult extends ValueResult<IDouble> {
	public RealResult(IDouble real) {
		this(real.getType(), real);
	}
	
	public RealResult(Type type, IDouble real) {
		super(type, real);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> add(AbstractResult<V> result) {
		return result.addReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> multiply(AbstractResult<V> result) {
		return result.multiplyReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> divide(AbstractResult<V> result) {
		return result.divideReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> subtract(AbstractResult<V> result) {
		return result.subtractReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> modulo(AbstractResult<V> result) {
		return result.moduloReal(this);
	}
	
	
	/// real impls start here
	
	@Override
	public <U extends IValue> AbstractResult<U> negative() {
		return makeResult(type, ValueFactoryFactory.getValueFactory().dubble(- getValue().getValue()));
	}
	
	
	@Override
	protected <U extends IValue> AbstractResult<U> addInteger(IntegerResult n) {
		return addReal(n.widenToReal());
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> subtractInteger(IntegerResult n) {
		return subtractReal(n.widenToReal());
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> multiplyInteger(IntegerResult n) {
		return multiplyReal(n.widenToReal());
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> divideInteger(IntegerResult n) {
		return divideReal(n.widenToReal());
	}
	
	@Override  
	protected <U extends IValue> AbstractResult<U> addReal(RealResult n) {
		return makeResult(type, getValue().add(n.getValue()));
	}
	
	@Override 
	protected <U extends IValue> AbstractResult<U> subtractReal(RealResult n) {
		// note the reverse subtraction.
		return makeResult(type, n.getValue().subtract(getValue()));
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> multiplyReal(RealResult n) {
		return makeResult(type, getValue().multiply(n.getValue()));
	}

	@Override
	protected <U extends IValue> AbstractResult<U> divideReal(RealResult n) {
		// note the reverse division
		return makeResult(type, n.getValue().divide(getValue()));
	}
	
	
}
