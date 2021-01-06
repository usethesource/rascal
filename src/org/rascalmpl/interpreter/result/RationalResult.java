/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Anya Helene Bagge - anya@ii.uib.no
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
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class RationalResult extends ElementResult<IRational> {

	public RationalResult(Type type, IRational n, IEvaluatorContext ctx) {
		super(type, n, ctx);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addRational(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result) {
		return result.multiplyRational(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractRational(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> result) {
		return result.divideRational(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> that) {
		return that.makeRangeFromRational(this);
	}

	@Override
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> step) {
		return to.makeStepRangeFromRational(this, step);
	}

	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToRational(this);
	}

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToRational(this);
	}

	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualRational(this);
	}
	
	
	/// real impls start here
	
	@Override
	public <U extends IValue> Result<U> negative() {
		return makeResult(type, getValue().negate(), ctx);
	}
	
	@Override  
	protected <U extends IValue> Result<U> addRational(RationalResult n) {
		return makeResult(type, getValue().add(n.getValue()), ctx);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractRational(RationalResult n) {
		// Note the reverse subtraction
		return makeResult(type, n.getValue().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyRational(RationalResult n) {
		return makeResult(type, getValue().multiply(n.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> divideRational(RationalResult n) {
		try {
			// note the reverse division.
			return makeResult(type, n.getValue().divide(getValue()), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}
	}

	
	@Override  
	protected <U extends IValue> Result<U> addInteger(IntegerResult n) {
		return makeResult(type, getValue().toRational().add(n.getValue()), ctx);
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractInteger(IntegerResult n) {
		// Note the reverse subtraction
		return makeResult(type, n.getValue().toRational().subtract(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult n) {
		return makeResult(type, getValue().toRational().multiply(n.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> divideInteger(IntegerResult n) {
		try {
			// note the reverse division.
			return makeResult(type, n.getValue().toRational().divide(getValue()), ctx);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), this.ctx.getCurrentAST(), null);
		}		
	}

	
	@Override  
	protected <U extends IValue> Result<U> addReal(ElementResult<IReal> n) {
		return n.addRational(this);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(ElementResult<IReal> n) {
		return n.multiplyRational(this);
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
	protected <U extends IValue> Result<U> makeRangeFromRational(RationalResult from) {
		// this = to
		return makeRangeWithDefaultStep(from);
	}


	private <U extends IValue, V extends INumber> Result<U> makeRangeWithDefaultStep(Result<V> from) {
		if (from.getValue().less(getValue()).getValue()) {
			return makeStepRangeFromToWithSecond(from, this, makeResult(from.getStaticType(),
					from.getValue().add(getValueFactory().rational(1,1)), ctx), getValueFactory(), getTypeFactory(), ctx);
		}
		return makeStepRangeFromToWithSecond(from, this, makeResult(from.getStaticType(),
					from.getValue().subtract(getValueFactory().integer(1)), ctx), getValueFactory(), getTypeFactory(), ctx);
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromRational(RationalResult from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromReal(ElementResult<IReal> from) {
		return makeRangeWithDefaultStep(from);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult from) {
		return makeRangeWithDefaultStep(from);
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromReal(ElementResult<IReal> from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
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
			do {
				w.append(iFrom);
				iFrom = iFrom.add(diff);
				if (ctx.isInterrupted()) throw new InterruptException(ctx.getStackTrace(), ctx.getCurrentAST().getLocation());
			} while (iFrom.lessEqual(iTo).getValue());
		} 
		else if (iFrom.greaterEqual(iTo).getValue() && diff.less(zero).getValue()) {
			do {
				w.append(iFrom);
				iFrom = iFrom.add(diff);
			} while (iFrom.greaterEqual(iTo).getValue());
		}
		return makeResult(tf.listType(resultType), w.done(), ctx);	
	}
	
	@Override
	protected <U extends IValue> Result<U> addListRelation(ListRelationResult that) {
		return that.addRational(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult that) {
		return that.addRational(this);
	}
	
	@Override
	protected Result<IBool> equalToRational(RationalResult that) {
		return bool(that.getValue().equal(getValue()).getValue(), ctx);
	}
	
	
	@Override
	protected Result<IBool> equalToInteger(IntegerResult that) {
    return bool(that.getValue().equal(getValue()).getValue(), ctx);
	}
	
	@Override
	protected Result<IBool> nonEqualToRational(RationalResult that) {
    return bool(!that.getValue().equal(getValue()).getValue(), ctx);
	}
	
	@Override
	protected Result<IBool> nonEqualToReal(RealResult that) {
    return bool(!that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected LessThanOrEqualResult lessThanOrEqualRational(RationalResult that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
	}

	@Override
	protected LessThanOrEqualResult lessThanOrEqualInteger(IntegerResult that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
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
	protected Result<IBool> lessThanReal(RealResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> lessThanRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().less(getValue()), ctx);
	}
	

	@Override
	protected Result<IBool> greaterThanNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
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
	protected Result<IBool> greaterThanOrEqualNumber(NumberResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualReal(ElementResult<IReal> that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greaterEqual(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanRational(RationalResult that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanReal(ElementResult<IReal> that) {
	  return makeResult(getTypeFactory().boolType(), that.getValue().greater(getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> equalToReal(RealResult that) {
		return bool(that.getValue().compare(getValue()) == 0, ctx);
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualReal(ElementResult<IReal> that) {
	  return new LessThanOrEqualResult(that.getValue().less(getValue()).getValue(), that.getValue().equal(getValue()).getValue(), ctx);
	}

	<U extends IValue> Result<U> widenToReal() {
		return makeResult(getTypeFactory().realType(), getValue().toReal(getValueFactory().getPrecision()), ctx);
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
