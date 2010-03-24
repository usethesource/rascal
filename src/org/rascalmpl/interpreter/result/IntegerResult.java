package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;

public class IntegerResult extends ElementResult<IInteger> {

	public IntegerResult(Type type, IInteger n, IEvaluatorContext ctx) {
		super(type, n, ctx);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result) {
		return result.multiplyInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> result) {
		return result.divideInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> modulo(Result<V> result) {
		return result.moduloInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> that) {
		return that.makeRangeFromInteger(this);
	}

	@Override
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> step) {
		return to.makeStepRangeFromInteger(this, step);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result) {
		return result.compareInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToInteger(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToInteger(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result) {
		return result.lessThanInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result) {
		return result.greaterThanInteger(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualInteger(this);
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
		// Note the reverse subtraction
		return makeResult(type, n.getValue().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult n) {
		return makeResult(type, getValue().multiply(n.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> divideInteger(IntegerResult n) {
		// note the reverse division.
		return makeResult(type, n.getValue().divide(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> moduloInteger(IntegerResult n) {
		// note reverse
		return makeResult(type, n.getValue().remainder(getValue()), ctx);
	}
	
	@Override  
	protected <U extends IValue> Result<U> addReal(RealResult n) {
		return n.addInteger(this);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(RealResult n) {
		return n.multiplyInteger(this);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractReal(RealResult n) {
		return widenToReal().subtractReal(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> divideReal(RealResult n) {
		return widenToReal().divideReal(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult from) {
		// NOTE: this == to
		IInteger iFrom = from.getValue();
		IInteger iTo = this.getValue();
		IInteger one = getValueFactory().integer(1);
		IListWriter w = getValueFactory().listWriter(getTypeFactory().integerType());
		
		if (iTo.less(iFrom).getValue()) {
			while (iFrom.greaterEqual(iTo).getValue()) {
				w.append(iFrom);
				iFrom = iFrom.subtract(one);
			} 
		}
		else {
			while (iFrom.lessEqual(iTo).getValue()) {
				w.append(iFrom);
				iFrom = iFrom.add(one);
			}
		}
		return makeResult(getTypeFactory().listType(getTypeFactory().integerType()), w.done(), ctx);
	}

	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult from, Result<V> second) {
		// NOTE: this == to
		IInteger iFrom = from.getValue();
		IInteger iTo = this.getValue();
		if (!second.getType().isIntegerType()) {
			throw new UnexpectedTypeError(getTypeFactory().integerType(), second.getType(), ctx.getCurrentAST());
		}
		IInteger iSecond = ((IInteger) second.getValue());
		IInteger diff = iSecond.subtract(iFrom);
		IInteger zero = getValueFactory().integer(0);

		IListWriter w = getValueFactory().listWriter(getTypeFactory().integerType());
		if (iFrom.lessEqual(iTo).getValue() && diff.greater(zero).getValue()) {
			do {
				w.append(iFrom);
				iFrom = iFrom.add(diff);
			} while (iFrom.lessEqual(iTo).getValue());
		} 
		else if (iFrom.greaterEqual(iTo).getValue() && diff.less(zero).getValue()) {
			do {
				w.append(iFrom);
				iFrom = iFrom.add(diff);
			} while (iFrom.greaterEqual(iTo).getValue());
		}
		return makeResult(getTypeFactory().listType(getTypeFactory().integerType()), w.done(), ctx);		
	}
	
	@Override
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that) {
		// note: reversed arguments
		IInteger left = that.getValue();
		IInteger right = this.getValue();
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(left.compare(right)), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareReal(RealResult that) {
		// note: reversed arguments
		return widenToReal().compare(that);
	}

	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that) {
		return that.nonEquals(widenToReal());
	}

	@Override
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that) {
		// note reversed args: we need that < this
		return bool((that.comparisonInts(this) < 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that) {
		// note reversed args: we need that <= this
		return bool((that.comparisonInts(this) <= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that) {
		// note reversed args: we need that > this
		return bool((that.comparisonInts(this) > 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that) {
		// note reversed args: we need that >= this
		return bool((that.comparisonInts(this) >= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that) {
		return that.equals(widenToReal());
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanReal(RealResult that) {
		// note reversed args: we need that < this
		return that.lessThan(widenToReal());
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that) {
		// note reversed args: we need that <= this
		return that.lessThanOrEqual(widenToReal());
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanReal(RealResult that) {
		// note reversed args: we need that > this
		return that.greaterThan(widenToReal());
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that) {
		// note reversed args: we need that >= this
		return that.greaterThanOrEqual(widenToReal());
	}

	
	<U extends IValue> Result<U> widenToReal() {
		return makeResult(getTypeFactory().realType(), getValue().toReal(), ctx);
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
		return makeResult(n.getType(), n.getValue().divide(getValue(), RealResult.PRECISION), ctx);
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
	
	
}
