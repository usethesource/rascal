package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class RealResult extends ElementResult<IReal> {
	private static final int PRECISION = 80*80; // ONE PAGE OF DIGITS

	public RealResult(IReal real, IEvaluatorContext ctx) {
		this(real.getType(), real, ctx);
	}
	
	public RealResult(Type type, IReal real, IEvaluatorContext ctx) {
		super(type, real, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, IEvaluatorContext ctx) {
		return result.addReal(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result, IEvaluatorContext ctx) {
		return result.multiplyReal(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> result, IEvaluatorContext ctx) {
		return result.divideReal(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, IEvaluatorContext ctx) {
		return result.subtractReal(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> modulo(Result<V> result, IEvaluatorContext ctx) {
		return result.moduloReal(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		return that.equalToReal(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, IEvaluatorContext ctx) {
		return that.nonEqualToReal(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanReal(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanOrEqualReal(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanReal(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanOrEqualReal(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, IEvaluatorContext ctx) {
		return result.compareReal(this, ctx);
	}
	
	/// real impls start here
	
	@Override
	public <U extends IValue> Result<U> negative(IEvaluatorContext ctx) {
		return makeResult(type, getValue().negate(), ctx);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> addInteger(IntegerResult n, IEvaluatorContext ctx) {
		return n.widenToReal().add(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractInteger(IntegerResult n, IEvaluatorContext ctx) {
		// Note reversed args: we need n - this
		return n.widenToReal().subtract(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult n, IEvaluatorContext ctx) {
		return n.widenToReal().multiply(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> divideInteger(IntegerResult n, IEvaluatorContext ctx) {
		// Note reversed args: we need n / this
		return n.widenToReal().divide(this, ctx);
	}
	
	@Override  
	protected <U extends IValue> Result<U> addReal(RealResult n, IEvaluatorContext ctx) {
		return makeResult(type, getValue().add(n.getValue()), ctx);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractReal(RealResult n, IEvaluatorContext ctx) {
		// note the reverse subtraction.
		return makeResult(type, n.getValue().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(RealResult n, IEvaluatorContext ctx) {
		return makeResult(type, getValue().multiply(n.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> divideReal(RealResult n, IEvaluatorContext ctx) {
		// note the reverse division
		return makeResult(type, n.getValue().divide(getValue(), PRECISION), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that, IEvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that, IEvaluatorContext ctx) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanReal(RealResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ctx) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ctx) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanReal(RealResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ctx) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ctx) >= 0);
	}

	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.widenToReal().equals(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.widenToReal().nonEquals(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that < this
		return that.widenToReal().lessThan(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that <= this
		return that.widenToReal().lessThanOrEqual(this, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that > this
		return that.widenToReal().greaterThan(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that >= this
		return that.widenToReal().greaterThanOrEqual(this, ctx);
	}

	
	@Override
	protected <U extends IValue> Result<U> compareReal(RealResult that, IEvaluatorContext ctx) {
		// note reverse arguments
		IReal left = that.getValue();
		IReal right = this.getValue();
		int result = left.compare(right);
		return makeIntegerResult(result, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that,
			IEvaluatorContext ctx) {
		return that.widenToReal().compare(this, ctx);
	}

	
}
