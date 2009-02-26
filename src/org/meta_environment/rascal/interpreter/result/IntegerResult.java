package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IInteger;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class IntegerResult extends ElementResult {

	private IInteger integer;
	
	public IntegerResult(IInteger n) {
		this.integer = n;
	}
	
	@Override
	public IInteger getValue() {
		return integer;
	}
	
	
	@Override
	public AbstractResult add(AbstractResult result) {
		return result.addInteger(this);
	}
	
	@Override
	public AbstractResult multiply(AbstractResult result) {
		return result.multiplyInteger(this);
	}
	
	@Override
	public AbstractResult subtract(AbstractResult result) {
		return result.subtractInteger(this);
	}
	
	@Override
	public AbstractResult divide(AbstractResult result) {
		return result.divideInteger(this);
	}
	
	@Override
	public AbstractResult modulo(AbstractResult result) {
		return result.moduloInteger(this);
	}
	
	
	/// real impls start here
	
	@Override
	public IntegerResult negative() {
		return new IntegerResult(ValueFactoryFactory.getValueFactory().integer(- getValue().getValue()));
	}
	
	@Override  
	protected IntegerResult addInteger(IntegerResult n) {
		return new IntegerResult(getValue().add(n.getValue()));
	}
	
	@Override 
	protected IntegerResult subtractInteger(IntegerResult n) {
		// Note the reverse subtraction
		return new IntegerResult(n.getValue().subtract(getValue()));
	}
	
	@Override
	protected IntegerResult multiplyInteger(IntegerResult n) {
		return new IntegerResult(getValue().multiply(n.getValue()));
	}

	@Override
	protected IntegerResult divideInteger(IntegerResult n) {
		// note the reverse division.
		return new IntegerResult(n.getValue().divide(getValue()));
	}
	
	@Override
	protected IntegerResult moduloInteger(IntegerResult n) {
		// note reverse
		return new IntegerResult(n.getValue().remainder(getValue()));
	}
	
	@Override  
	protected RealResult addReal(RealResult n) {
		return n.addInteger(this);
	}
	
	
	@Override
	protected RealResult multiplyReal(RealResult n) {
		return n.multiplyInteger(this);
	}
	
	@Override 
	protected RealResult subtractReal(RealResult n) {
		return widenToReal().subtractReal(n);
	}
	
	@Override
	protected RealResult divideReal(RealResult n) {
		return widenToReal().divideReal(n);
	}
		
	RealResult widenToReal() {
		return new RealResult(getValue().toDouble());
	}
	

	
}
