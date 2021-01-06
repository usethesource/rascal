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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

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
	public <U extends IValue, V extends IValue> Result<U> remainder(Result<V> result) {
		return result.remainderInteger(this);
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
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToInteger(this);
	}

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToInteger(this);
	}

	@Override
	public <V extends IValue> Result<IBool> lessThan(Result<V> result) {
		return result.lessThanInteger(this);
	}
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualInteger(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThan(Result<V> result) {
		return result.greaterThanInteger(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> result) {
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
	protected <U extends IValue> Result<U> addRational(RationalResult that) {
		return that.addInteger(this); // use commutativity
	} 
	
	@Override
	protected <U extends IValue> Result<U> multiplyRational(RationalResult that) {
		return that.multiplyInteger(this); // use commutativity
	}
	
	@Override
	protected <U extends IValue> Result<U> divideRational(RationalResult that) {
		try {
			return makeResult(getTypeFactory().rationalType(), that.getValue().divide(getValue()), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractRational(RationalResult that) {
		return makeResult(getTypeFactory().rationalType(), that.getValue().subtract(getValue()), ctx);
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
		try {
			// note the reverse division.
			return makeResult(type, n.getValue().divide(getValue()), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}
	}
	
	@Override
	protected <U extends IValue> Result<U> remainderInteger(IntegerResult n) {
		try {
			// note reverse
			return makeResult(type, n.getValue().remainder(getValue()), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}
	}
	
	
	@Override
	protected <U extends IValue> Result<U> moduloInteger(IntegerResult n) {
		try {
			// note reverse
			return makeResult(type, n.getValue().mod(getValue()), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}
	}
	
	@Override  
	protected <U extends IValue> Result<U> addReal(ElementResult<IReal> n) {
		return n.addInteger(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> addListRelation(ListRelationResult that) {
		return that.addInteger(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult that) {
		return that.addInteger(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(ElementResult<IReal> n) {
		return n.multiplyInteger(this);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractReal(ElementResult<IReal> n) {
		return widenToReal().subtractReal(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> divideReal(ElementResult<IReal> n) {
		return widenToReal().divideReal(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult from) {
		// this = to
		return makeRangeWithDefaultStep(from);
	}


	private <U extends IValue, V extends INumber> Result<U> makeRangeWithDefaultStep(Result<V> from) {
		if (from.getValue().less(getValue()).getValue()) {
			return makeStepRangeFromToWithSecond(from, this, makeResult(from.getStaticType(),
					from.getValue().add(getValueFactory().integer(1)), ctx), getValueFactory(), getTypeFactory(), ctx);
		}
		return makeStepRangeFromToWithSecond(from, this, makeResult(from.getStaticType(),
					from.getValue().subtract(getValueFactory().integer(1)), ctx), getValueFactory(), getTypeFactory(), ctx);
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromReal(ElementResult<IReal> from) {
		return makeRangeWithDefaultStep(from);
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromReal(ElementResult<IReal> from, Result<V> second) {
	  return toReal(this).makeStepRangeFromReal(from, second);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromNumber(NumberResult from) {
		return makeRangeWithDefaultStep(from);
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromNumber(NumberResult from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
	}


	public static <U extends IValue, V extends INumber, W extends INumber, X extends IValue> Result<U> 
		makeStepRangeFromToWithSecond(
				Result<V> from,  
				Result<W> to, 
				Result<X> second, 
				IValueFactory vf, 
				TypeFactory tf, 
				IEvaluatorContext ctx) {
		
		INumber iFrom = from.getValue();
		INumber iTo = to.getValue();
		
		// I still think it is ugly to do it here...
		if (!second.getStaticType().isSubtypeOf(tf.numberType())) {
			throw new UnexpectedType(tf.numberType(), second.getStaticType(), ctx.getCurrentAST());
		}
		
		INumber iSecond = (INumber) second.getValue();
		INumber diff = iSecond.subtract(iFrom);
		
		INumber zero = diff.subtract(diff); // zero in the type that we're dealing with.

		// Use declared types here
		Type resultType = second.getStaticType().lub(from.getStaticType().lub(to.getStaticType()));
		
		IListWriter w = vf.listWriter();
		if (iFrom.lessEqual(iTo).getValue() && diff.greater(zero).getValue()) {
			 while (iFrom.less(iTo).getValue()) {
				w.append(iFrom);
				iFrom = iFrom.add(diff);
				if (ctx.isInterrupted()) throw new InterruptException(ctx.getStackTrace(), ctx.getCurrentAST().getLocation());
			};
		} 
		else if (iFrom.greaterEqual(iTo).getValue() && diff.less(zero).getValue()) {
			 while (iFrom.greater(iTo).getValue()) {
				w.append(iFrom);
				iFrom = iFrom.add(diff);
			};
		}
		return makeResult(tf.listType(resultType), w.done(), ctx);	
	}
	

	
	
	@Override
	protected Result<IBool> equalToInteger(IntegerResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected Result<IBool> nonEqualToInteger(IntegerResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected Result<IBool> nonEqualToReal(RealResult that) {
		return that.nonEquals(widenToReal());
	}


	@Override
	protected Result<IBool> greaterThanInteger(IntegerResult that) {
	   return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualInteger(IntegerResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> lessThanInteger(IntegerResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> lessThanNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualInteger(IntegerResult that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
	}
	
	@Override
	protected Result<IBool> equalToReal(RealResult that) {
		return that.equals(widenToReal());
	}
	
	@Override
	protected Result<IBool> equalToRational(RationalResult that) {
		return that.equalToInteger(this);
	}
	
	@Override
	protected Result<IBool> lessThanReal(RealResult that) {
		return that.lessThan(widenToReal());
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualReal(ElementResult<IReal> that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
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
	protected Result<IBool> lessThanRational(RationalResult that) {
		// note reversed args: we need that < this
		return that.lessThan(widenToRational());
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualRational(RationalResult that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected Result<IBool> greaterThanRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}

	<U extends IValue> Result<U> widenToReal() {
		return makeResult(getTypeFactory().realType(), getValue().toReal(getValueFactory().getPrecision()), ctx);
	}

	<U extends IValue> Result<U> widenToRational() {
		return makeResult(getTypeFactory().rationalType(), getValue().toRational(), ctx);
	}

	@Override  
	protected <U extends IValue> Result<U> addNumber(NumberResult n) {
		return makeResult(n.getStaticType(), getValue().add(n.getValue()), ctx);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractNumber(NumberResult n) {
		// note the reverse subtraction.
		return makeResult(n.getStaticType(), n.getValue().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyNumber(NumberResult n) {
		return makeResult(n.getStaticType(), getValue().multiply(n.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> divideNumber(NumberResult n) {
		try {
			// note the reverse division
			return makeResult(n.getStaticType(), n.getValue().divide(getValue(), getValueFactory().getPrecision()), ctx);
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
	
}
