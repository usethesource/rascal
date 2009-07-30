package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.*;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

public class IntegerResult extends ElementResult<IInteger> {

	public IntegerResult(Type type, IInteger n, IEvaluatorContext ctx) {
		super(type, n, ctx);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, IEvaluatorContext ctx) {
		return result.addInteger(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result, IEvaluatorContext ctx) {
		return result.multiplyInteger(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, IEvaluatorContext ctx) {
		return result.subtractInteger(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> result, IEvaluatorContext ctx) {
		return result.divideInteger(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> modulo(Result<V> result, IEvaluatorContext ctx) {
		return result.moduloInteger(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> that, IEvaluatorContext ctx) {
		return that.makeRangeFromInteger(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> step, IEvaluatorContext ctx) {
		return to.makeStepRangeFromInteger(this, step, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, IEvaluatorContext ctx) {
		return result.compareInteger(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		return that.equalToInteger(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, IEvaluatorContext ctx) {
		return that.nonEqualToInteger(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanInteger(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanOrEqualInteger(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanInteger(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanOrEqualInteger(this, ctx);
	}
	
	
	/// real impls start here
	
	@Override
	public <U extends IValue> Result<U> negative(IEvaluatorContext ctx) {
		return makeResult(type, getValue().negate(), ctx);
	}
	
	@Override  
	protected <U extends IValue> Result<U> addInteger(IntegerResult n, IEvaluatorContext ctx) {
		return makeResult(type, getValue().add(n.getValue()), ctx);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractInteger(IntegerResult n, IEvaluatorContext ctx) {
		// Note the reverse subtraction
		return makeResult(type, n.getValue().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult n, IEvaluatorContext ctx) {
		return makeResult(type, getValue().multiply(n.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> divideInteger(IntegerResult n, IEvaluatorContext ctx) {
		// note the reverse division.
		return makeResult(type, n.getValue().divide(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> moduloInteger(IntegerResult n, IEvaluatorContext ctx) {
		// note reverse
		return makeResult(type, n.getValue().remainder(getValue()), ctx);
	}
	
	@Override  
	protected <U extends IValue> Result<U> addReal(RealResult n, IEvaluatorContext ctx) {
		return n.addInteger(this, ctx);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(RealResult n, IEvaluatorContext ctx) {
		return n.multiplyInteger(this, ctx);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractReal(RealResult n, IEvaluatorContext ctx) {
		return widenToReal().subtractReal(n, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> divideReal(RealResult n, IEvaluatorContext ctx) {
		return widenToReal().divideReal(n, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult from, IEvaluatorContext ctx) {
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
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult from, Result<V> second, IEvaluatorContext ctx) {
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
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that, IEvaluatorContext ctx) {
		// note: reversed arguments
		IInteger left = that.getValue();
		IInteger right = this.getValue();
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(left.compare(right)), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareReal(RealResult that, IEvaluatorContext ctx) {
		// note: reversed arguments
		return widenToReal().compare(that, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that, IEvaluatorContext ctx) {
		return that.nonEquals(widenToReal(), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ctx) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ctx) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ctx) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ctx) >= 0);
	}

	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that, IEvaluatorContext ctx) {
		return that.equals(widenToReal(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanReal(RealResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that < this
		return that.lessThan(widenToReal(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that <= this
		return that.lessThanOrEqual(widenToReal(), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanReal(RealResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that > this
		return that.greaterThan(widenToReal(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that >= this
		return that.greaterThanOrEqual(widenToReal(), ctx);
	}

	
	<U extends IValue> Result<U> widenToReal() {
		return makeResult(getTypeFactory().realType(), getValue().toReal(), null);
	}
	

	
}
