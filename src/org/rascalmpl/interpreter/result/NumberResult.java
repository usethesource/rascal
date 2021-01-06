/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.IntegerResult.makeStepRangeFromToWithSecond;
import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import org.rascalmpl.values.ValueFactoryFactory;

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
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> that) {
		return that.makeRangeFromNumber(this);
	}

	@Override
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> step) {
		return to.makeStepRangeFromNumber(this, step);
	}

	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractNumber(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToNumber(this);
	}

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToNumber(this);
	}

	@Override
	public <V extends IValue> Result<IBool> lessThan(Result<V> result) {
		return result.lessThanNumber(this);
	}
	
	@Override
	protected Result<IBool> lessThanReal(RealResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualNumber(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThan(Result<V> result) {
		return result.greaterThanNumber(this);
	}
	
	@Override
	protected Result<IBool> greaterThanReal(ElementResult<IReal> that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx); 
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualReal(ElementResult<IReal> that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualNumber(this);
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
		try {
			// Note reversed args: we need n / this
			int prec = ValueFactoryFactory.getValueFactory().getPrecision();
			return makeResult(type, n.getValue().divide(getValue(), prec), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}			
	}
	
	@Override  
	protected <U extends IValue> Result<U> addReal(ElementResult<IReal> n) {
		return makeResult(type, getValue().add(n.getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> addRational(RationalResult that) {
		return makeResult(type, getValue().add(that.getValue()), ctx);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractReal(ElementResult<IReal> n) {
		// note the reverse subtraction.
		return makeResult(type, n.getValue().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(ElementResult<IReal> n) {
		return makeResult(type, getValue().multiply(n.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> divideReal(ElementResult<IReal> n) {
		try {
			// note the reverse division
			int prec = ValueFactoryFactory.getValueFactory().getPrecision();
			return makeResult(type, n.getValue().divide(getValue(), prec), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}			
	}
	
	@Override
	protected Result<IBool> equalToReal(RealResult that) {
	  return bool(that.getValue().equal(getValue()).getValue(), ctx);
	}
	
	@Override
	protected Result<IBool> nonEqualToReal(RealResult that) {
	  return bool(!that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected Result<IBool> equalToRational(RationalResult that) {
	  return bool(that.getValue().equal(getValue()).getValue(), ctx);
	}
	
	@Override
	protected Result<IBool> nonEqualToRational(
			RationalResult that) {
	  return bool(!that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected LessThanOrEqualResult lessThanOrEqualReal(ElementResult<IReal> that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected LessThanOrEqualResult lessThanOrEqualRational(RationalResult that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
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
			int prec = ValueFactoryFactory.getValueFactory().getPrecision();
			return makeResult(type, n.getValue().divide(getValue(), prec), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}			
	}
	
	@Override
	protected Result<IBool> equalToNumber(NumberResult that) {
	  return bool(that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected Result<IBool> nonEqualToNumber(NumberResult that) {
	  return bool(!that.getValue().equal(getValue()).getValue(), ctx);
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualNumber(NumberResult that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected Result<IBool> greaterThanOrEqualNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> equalToInteger(IntegerResult that) {
		return makeResult(getTypeFactory().boolType(), that.getValue().equal(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> nonEqualToInteger(IntegerResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected Result<IBool> lessThanInteger(IntegerResult that) {
		// note reversed args: we need that < this
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> lessThanNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> lessThanRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualInteger(IntegerResult that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected Result<IBool> greaterThanInteger(IntegerResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualInteger(IntegerResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult from) {
		return makeRangeWithDefaultStep(from, getValueFactory().integer(1));
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromReal(ElementResult<IReal> from) {
		return makeRangeWithDefaultStep(from, getValueFactory().real(1.0));
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromReal(ElementResult<IReal> from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
	}

	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromNumber(NumberResult from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> makeRangeFromNumber(NumberResult from) {
		if (getStaticType().lub(from.getStaticType()).isInteger()) {
			return makeRangeWithDefaultStep(from, getValueFactory().integer(1));
		}
		if (getStaticType().lub(from.getStaticType()).isReal()) {
			return makeRangeWithDefaultStep(from, getValueFactory().real(1.0));
		}
		if (getStaticType().lub(from.getStaticType()).isNumber()) {
			return makeRangeWithDefaultStep(from, getValueFactory().integer(1));
		}
		throw new ImplementationError("Unknown number type in makeRangeFromNumber");
	}
	
	private <U extends IValue, V extends INumber> Result<U> makeRangeWithDefaultStep(Result<V> from, INumber step) {
		if (from.getValue().less(getValue()).getValue()) {
			return makeStepRangeFromToWithSecond(from, this, makeResult(from.getStaticType(),
					from.getValue().add(step), ctx), getValueFactory(), getTypeFactory(), ctx);
		}
		return makeStepRangeFromToWithSecond(from, this, makeResult(from.getStaticType(),
				from.getValue().subtract(step), ctx), getValueFactory(), getTypeFactory(), ctx);
	}
	

}
