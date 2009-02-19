package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IDouble;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class RealResult extends AbstractResult {

	private IDouble real;
	
	public RealResult(IDouble real) {
		this.setReal(real);
	}
	
	@Override
	public IDouble getValue() {
		return real;
	}
	
	@Override
	public AbstractResult add(AbstractResult result) {
		return result.addReal(this);
	}
	
	@Override
	public AbstractResult multiply(AbstractResult result) {
		return result.multiplyReal(this);
	}
	
	@Override
	public AbstractResult divide(AbstractResult result) {
		return result.divideReal(this);
	}
	
	@Override
	public AbstractResult subtract(AbstractResult result) {
		return result.subtractReal(this);
	}
	
	/// real impls start here
	

	@Override
	protected RealResult addInteger(IntegerResult n) {
		return addReal(n.widenToReal());
	}
	
	@Override
	protected RealResult subtractInteger(IntegerResult n) {
		return subtractReal(n.widenToReal());
	}
	
	@Override
	protected RealResult multiplyInteger(IntegerResult n) {
		return multiplyReal(n.widenToReal());
	}
	
	@Override
	protected RealResult divideInteger(IntegerResult n) {
		return divideReal(n.widenToReal());
	}
	
	@Override  
	protected RealResult addReal(RealResult n) {
		return new RealResult(getReal().add(n.getReal()));
	}
	
	@Override 
	protected RealResult subtractReal(RealResult n) {
		// note the reverse subtraction.
		return new RealResult(n.getReal().subtract(getReal()));
	}
	
	@Override
	protected RealResult multiplyReal(RealResult n) {
		return new RealResult(getReal().multiply(n.getReal()));
	}

	@Override
	protected RealResult divideReal(RealResult n) {
		// note the reverse division
		return new RealResult(n.getReal().divide(getReal()));
	}

	@Override
	protected SetResult addSet(SetResult s) {
		return s.addReal(this);
	}
	
	@Override
	protected SetResult subtractSet(SetResult s) {
		throw new ImplementationError("NYI");
		//return new SetResult(s.getSet().delete(this));
	}

	
	public void setReal(IDouble real) {
		this.real = real;
	}

	public IDouble getReal() {
		return real;
	}
	
}
