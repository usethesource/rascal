package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class NumberResult extends ElementResult<INumber> {
	public NumberResult(Type type, INumber value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addNumber(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result) {
		return result.multiplyNumber(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> result) {
		return result.divideNumber(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractNumber(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToNumber(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToNumber(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result) {
		return result.lessThanNumber(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualNumber(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result) {
		return result.greaterThanNumber(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualNumber(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result) {
		return result.compareNumber(this);
	}
	
	/// real impls start here
	
	@Override
	public <U extends IValue> Result<U> negative() {
		return makeResult(type, getValue().negate(), ctx);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> addInteger(IntegerResult n) {
		return makeResult(type, getValue().add(n.getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractInteger(IntegerResult n) {
		// Note reversed args: we need n - this
		return makeResult(type, n.getValue().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult n) {
		return makeResult(type, getValue().multiply(n.getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> divideInteger(IntegerResult n) {
		// Note reversed args: we need n / this
		return makeResult(type, n.getValue().divide(getValue(), RealResult.PRECISION), ctx);
	}
	
	@Override  
	protected <U extends IValue> Result<U> addReal(RealResult n) {
		return makeResult(type, getValue().add(n.getValue()), ctx);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractReal(RealResult n) {
		// note the reverse subtraction.
		return makeResult(type, n.getValue().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(RealResult n) {
		return makeResult(type, getValue().multiply(n.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> divideReal(RealResult n) {
		// note the reverse division
		return makeResult(type, n.getValue().divide(getValue(), RealResult.PRECISION), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that) {
		return that.equalityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanReal(RealResult that) {
		// note reversed args: we need that < this
		return bool((that.comparisonInts(this) < 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that) {
		// note reversed args: we need that <= this
		return bool((that.comparisonInts(this) <= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanReal(RealResult that) {
		// note reversed args: we need that > this
		return bool((that.comparisonInts(this) > 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that) {
		// note reversed args: we need that >= this
		return bool((that.comparisonInts(this) >= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> compareReal(RealResult that) {
		// note reverse arguments
		IReal left = that.getValue();
		INumber right = this.getValue();
		int result = left.compare(right);
		return makeIntegerResult(result);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that) {
		return that.widenToReal().compare(this);
	}
	
	@Override  
	protected <U extends IValue> Result<U> addNumber(NumberResult n) {
		return makeResult(type, getValue().add(n.getValue()), ctx);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractNumber(NumberResult n) {
		// note the reverse subtraction.
		return makeResult(type, n.getValue().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyNumber(NumberResult n) {
		return makeResult(type, getValue().multiply(n.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> divideNumber(NumberResult n) {
		// note the reverse division
		return makeResult(type, n.getValue().divide(getValue(), RealResult.PRECISION), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToNumber(NumberResult that) {
		return that.equalityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToNumber(NumberResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanNumber(NumberResult that) {
		// note reversed args: we need that < this
		return bool((that.comparisonInts(this) < 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualNumber(NumberResult that) {
		// note reversed args: we need that <= this
		return bool((that.comparisonInts(this) <= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanNumber(NumberResult that) {
		// note reversed args: we need that > this
		return bool((that.comparisonInts(this) > 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualNumber(NumberResult that) {
		// note reversed args: we need that >= this
		return bool((that.comparisonInts(this) >= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that) {
		return that.widenToReal().equals(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that) {
		return that.widenToReal().nonEquals(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that) {
		// note reversed args: we need that < this
		return that.widenToReal().lessThan(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that) {
		// note reversed args: we need that <= this
		return that.widenToReal().lessThanOrEqual(this);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that) {
		// note reversed args: we need that > this
		return that.widenToReal().greaterThan(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that) {
		// note reversed args: we need that >= this
		return that.widenToReal().greaterThanOrEqual(this);
	}

	
	@Override
	protected <U extends IValue> Result<U> compareNumber(NumberResult that) {
		// note reverse arguments
		INumber left = that.getValue();
		INumber right = this.getValue();
		int result = left.compare(right);
		return makeIntegerResult(result);
	}

}
