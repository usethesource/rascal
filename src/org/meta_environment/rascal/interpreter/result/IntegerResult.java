package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

public class IntegerResult extends ValueResult<IInteger> {

	public IntegerResult(Type type, IInteger n) {
		super(type, n);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> add(AbstractResult<V> result) {
		return result.addInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> multiply(AbstractResult<V> result) {
		return result.multiplyInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> subtract(AbstractResult<V> result) {
		return result.subtractInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> divide(AbstractResult<V> result) {
		return result.divideInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> modulo(AbstractResult<V> result) {
		return result.moduloInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> result) {
		return result.compare(this);
	}
	
	/// real impls start here
	
	@Override
	public <U extends IValue> AbstractResult<U> negative() {
		return makeResult(type, getValue().negate());
	}
	
	@Override  
	protected <U extends IValue> AbstractResult<U> addInteger(IntegerResult n) {
		return makeResult(type, getValue().add(n.getValue()));
	}
	
	@Override 
	protected <U extends IValue> AbstractResult<U> subtractInteger(IntegerResult n) {
		// Note the reverse subtraction
		return makeResult(type, n.getValue().subtract(getValue()));
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> multiplyInteger(IntegerResult n) {
		return makeResult(type, getValue().multiply(n.getValue()));
	}

	@Override
	protected <U extends IValue> AbstractResult<U> divideInteger(IntegerResult n) {
		// note the reverse division.
		return makeResult(type, n.getValue().divide(getValue()));
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> moduloInteger(IntegerResult n) {
		// note reverse
		return makeResult(type, n.getValue().remainder(getValue()));
	}
	
	@Override  
	protected <U extends IValue> AbstractResult<U> addReal(RealResult n) {
		return n.addInteger(this);
	}
	
	
	@Override
	protected <U extends IValue> AbstractResult<U> multiplyReal(RealResult n) {
		return n.multiplyInteger(this);
	}
	
	@Override 
	protected <U extends IValue> AbstractResult<U> subtractReal(RealResult n) {
		return widenToReal().subtractReal(n);
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> divideReal(RealResult n) {
		return widenToReal().divideReal(n);
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> compareInteger(IntegerResult that) {
		// note: reversed arguments
		IInteger left = that.getValue();
		IInteger right = this.getValue();
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(left.compare(right)));
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> compareReal(RealResult that) {
		// note: reversed arguments
		return widenToReal().compare(that);
	}
	
		
	RealResult widenToReal() {
		return new RealResult(type, getValue().toReal());
	}
	

	
}
