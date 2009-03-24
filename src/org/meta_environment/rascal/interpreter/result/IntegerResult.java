package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.*;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

import org.meta_environment.rascal.ast.AbstractAST;

public class IntegerResult extends ElementResult<IInteger> {

	public IntegerResult(Type type, IInteger n) {
		super(type, n);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, AbstractAST ast) {
		return result.addInteger(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result, AbstractAST ast) {
		return result.multiplyInteger(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, AbstractAST ast) {
		return result.subtractInteger(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> result, AbstractAST ast) {
		return result.divideInteger(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> modulo(Result<V> result, AbstractAST ast) {
		return result.moduloInteger(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> that, AbstractAST ast) {
		return that.makeRangeFromInteger(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> step, AbstractAST ast) {
		return to.makeStepRangeFromInteger(this, step, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, AbstractAST ast) {
		return result.compareInteger(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToInteger(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
		return that.nonEqualToInteger(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, AbstractAST ast) {
		return result.lessThanInteger(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.lessThanOrEqualInteger(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, AbstractAST ast) {
		return result.greaterThanInteger(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.greaterThanOrEqualInteger(this, ast);
	}
	
	
	/// real impls start here
	
	@Override
	public <U extends IValue> Result<U> negative(AbstractAST ast) {
		return makeResult(type, getValue().negate());
	}
	
	@Override  
	protected <U extends IValue> Result<U> addInteger(IntegerResult n, AbstractAST ast) {
		return makeResult(type, getValue().add(n.getValue()));
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractInteger(IntegerResult n, AbstractAST ast) {
		// Note the reverse subtraction
		return makeResult(type, n.getValue().subtract(getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult n, AbstractAST ast) {
		return makeResult(type, getValue().multiply(n.getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> divideInteger(IntegerResult n, AbstractAST ast) {
		// note the reverse division.
		return makeResult(type, n.getValue().divide(getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> moduloInteger(IntegerResult n, AbstractAST ast) {
		// note reverse
		return makeResult(type, n.getValue().remainder(getValue()));
	}
	
	@Override  
	protected <U extends IValue> Result<U> addReal(RealResult n, AbstractAST ast) {
		return n.addInteger(this, ast);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(RealResult n, AbstractAST ast) {
		return n.multiplyInteger(this, ast);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractReal(RealResult n, AbstractAST ast) {
		return widenToReal().subtractReal(n, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> divideReal(RealResult n, AbstractAST ast) {
		return widenToReal().divideReal(n, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult from, AbstractAST ast) {
		// NOTE: this == to
		IInteger iFrom = ((IInteger) from.getValue());
		IInteger iTo = ((IInteger) this.getValue());
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
		return makeResult(getTypeFactory().listType(getTypeFactory().integerType()), w.done());
	}

	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult from, Result<V> second, AbstractAST ast) {
		// NOTE: this == to
		IInteger iFrom = ((IInteger) from.getValue());
		IInteger iTo = ((IInteger) this.getValue());
		if (!second.getType().isIntegerType()) {
			throw new UnexpectedTypeError(getTypeFactory().integerType(), second.getType(), ast);
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
		return makeResult(getTypeFactory().listType(getTypeFactory().integerType()), w.done());		
	}
	
	@Override
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that, AbstractAST ast) {
		// note: reversed arguments
		IInteger left = that.getValue();
		IInteger right = this.getValue();
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(left.compare(right)));
	}
	
	@Override
	protected <U extends IValue> Result<U> compareReal(RealResult that, AbstractAST ast) {
		// note: reversed arguments
		return widenToReal().compare(that, ast);
	}

	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that, AbstractAST ast) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that, AbstractAST ast) {
		return that.nonEquals(widenToReal(), ast);
	}

	@Override
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ast) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ast) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ast) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ast) >= 0);
	}

	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that, AbstractAST ast) {
		return that.equals(widenToReal(), ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanReal(RealResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return that.lessThan(widenToReal(), ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return that.lessThanOrEqual(widenToReal(), ast);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanReal(RealResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return that.greaterThan(widenToReal(), ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return that.greaterThanOrEqual(widenToReal(), ast);
	}

	
	<U extends IValue> Result<U> widenToReal() {
		return makeResult(getTypeFactory().realType(), getValue().toReal());
	}
	

	
}
