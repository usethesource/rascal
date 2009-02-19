package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class IntegerResult extends AbstractResult {

	private IInteger integer;
	
	public IntegerResult(IInteger n) {
		this.setInteger(n);
	}
	
	@Override
	public IValue getValue() {
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
	
	
	/// real impls start here
	
	@Override  
	protected IntegerResult addInteger(IntegerResult n) {
		return new IntegerResult(getInteger().add(n.getInteger()));
	}
	
	@Override 
	protected IntegerResult subtractInteger(IntegerResult n) {
		// Note the reverse subtraction
		return new IntegerResult(n.getInteger().subtract(getInteger()));
	}
	
	@Override
	protected IntegerResult multiplyInteger(IntegerResult n) {
		return new IntegerResult(getInteger().multiply(n.getInteger()));
	}

	@Override
	protected IntegerResult divideInteger(IntegerResult n) {
		// note the reverse division.
		return new IntegerResult(n.getInteger().divide(getInteger()));
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

	@Override
	protected SetResult addSet(SetResult s) {
		return s.addInteger(this);
	}
	
	@Override
	protected ListResult addList(ListResult s) {
		return s.appendResult(this);
	}

	@Override
	protected AbstractResult subtractSet(SetResult s) {
		throw new ImplementationError("NYI");
		//return new SetResult(s.getSet().delete(this));
	}

	private void setInteger(IInteger integer) {
		this.integer = integer;
	}

	public IInteger getInteger() {
		return integer;
	}
	
	public RealResult widenToReal() {
		return new RealResult(getInteger().toDouble());
	}
	

	
}
