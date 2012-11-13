/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.IntegerResult.makeStepRangeFromToWithSecond;
import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class RealResult extends ElementResult<IReal> {
	public static final int PRECISION = 64;

	public RealResult(IReal real, IEvaluatorContext ctx) {
		this(real.getType(), real, ctx);
	}
	
	public RealResult(Type type, IReal real, IEvaluatorContext ctx) {
		super(type, real, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result) {
		return result.multiplyReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> result) {
		return result.divideReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> that) {
		return that.makeRangeFromReal(this);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> step) {
		return to.makeStepRangeFromReal(this, step);
	}

	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToReal(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToReal(this);
	}

	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualReal(this);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that) {
	  return that.greaterThanReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that) {
	  return that.greaterThanOrEqualReal(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that) {
	  return that.lessThanReal(this);
	}
	
	/// real impls start here
	
	@Override
	public <U extends IValue> Result<U> negative() {
		return makeResult(type, getValue().negate(), ctx);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> addInteger(IntegerResult n) {
		return n.widenToReal().add(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractInteger(IntegerResult n) {
		// Note reversed args: we need n - this
		return n.widenToReal().subtract(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult n) {
		return n.widenToReal().multiply(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> divideInteger(IntegerResult n) {
		try {
			// Note reversed args: we need n / this
			return n.widenToReal().divide(this);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}
}

	
	@Override
	protected <U extends IValue> Result<U> addRational(RationalResult n) {
		return n.widenToReal().add(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractRational(RationalResult n) {
		// Note reversed args: we need n - this
		return n.widenToReal().subtract(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyRational(RationalResult n) {
		return n.widenToReal().multiply(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> divideRational(RationalResult n) {
		try {
			// Note reversed args: we need n / this
			return n.widenToReal().divide(this);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}
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
		try {
			// note the reverse division
			return makeResult(type, n.getValue().divide(getValue(), PRECISION), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that) {
    return bool(that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> equalToRational(RationalResult that) {
    return bool(that.getValue().equal(getValue()).getValue(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that) {
    return bool(!that.getValue().equal(getValue()).getValue(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that) {
		// note reversed args: we need that <= this
		return makeResult(getTypeFactory().boolType(), (that.getValue().lessEqual(getValue())), ctx);
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
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanReal(RealResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().lessEqual(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanReal(RealResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().lessEqual(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
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
		try {
			// note the reverse division
			return makeResult(type, n.getValue().divide(getValue(), PRECISION), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}
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
	protected <U extends IValue> Result<U> lessThanOrEqualNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().lessEqual(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult from) {
		return makeRangeWithDefaultStep(from);
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromReal(RealResult from) {
		return makeRangeWithDefaultStep(from);
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromReal(RealResult from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromNumber(NumberResult from) {
		return makeRangeWithDefaultStep(from);
	}
	
	private <U extends IValue, V extends INumber> Result<U> makeRangeWithDefaultStep(Result<V> from) {
		if (from.getValue().less(getValue()).getValue()) {
			return makeStepRangeFromToWithSecond(from, this, makeResult(getTypeFactory().realType().lub(from.getType()),
					from.getValue().add(getValueFactory().real(1.0)), ctx), getValueFactory(), getTypeFactory(), ctx);
		}
		return makeStepRangeFromToWithSecond(from, this, makeResult(getTypeFactory().realType().lub(from.getType()),
				from.getValue().subtract(getValueFactory().real(1.0)), ctx), getValueFactory(), getTypeFactory(), ctx);

	}
	
}
