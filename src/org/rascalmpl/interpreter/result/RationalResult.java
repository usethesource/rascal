/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;

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
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result) {
		return result.compareRational(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToRational(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToRational(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result) {
		return result.lessThanRational(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualRational(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result) {
		return result.greaterThanRational(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualRational(this);
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
		// note the reverse division.
		return makeResult(type, n.getValue().divide(getValue()), ctx);
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
		// note the reverse division.
		return makeResult(type, n.getValue().toRational().divide(getValue()), ctx);
	}

	
	@Override  
	protected <U extends IValue> Result<U> addReal(RealResult n) {
		return n.addRational(this);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(RealResult n) {
		return n.multiplyRational(this);
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
	protected <U extends IValue> Result<U> makeRangeFromRational(RationalResult from) {
		// this = to
		return makeRangeWithDefaultStep(from);
	}


	private <U extends IValue, V extends INumber> Result<U> makeRangeWithDefaultStep(Result<V> from) {
		if (from.getValue().less(getValue()).getValue()) {
			return makeStepRangeFromToWithSecond(from, this, makeResult(from.getType(),
					from.getValue().add(getValueFactory().rational(1,1)), ctx), getValueFactory(), getTypeFactory(), ctx);
		}
		return makeStepRangeFromToWithSecond(from, this, makeResult(from.getType(),
					from.getValue().subtract(getValueFactory().integer(1)), ctx), getValueFactory(), getTypeFactory(), ctx);
	}
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromRational(RationalResult from, Result<V> second) {
		return makeStepRangeFromToWithSecond(from, this, second, getValueFactory(), getTypeFactory(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromReal(RealResult from) {
		return makeRangeWithDefaultStep(from);
	}
	
	@Override
	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult from) {
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
		if (!second.getType().isSubtypeOf(tf.numberType())) {
			throw new UnexpectedTypeError(tf.numberType(), second.getType(), ctx.getCurrentAST());
		}
		
		INumber iSecond = (INumber) second.getValue();
		INumber diff = iSecond.subtract(iFrom);
		
		INumber zero = diff.subtract(diff); // zero in the type that we're dealing with.

		// Use declared types here
		Type resultType = second.getType().lub(from.getType().lub(to.getType()));
		
		IListWriter w = vf.listWriter(resultType);
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
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that) {
		// note: reversed arguments
		IRational left = that.getValue().toRational();
		IRational right = this.getValue();
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(left.compare(right)), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareRational(RationalResult that) {
		// note: reversed arguments
		IRational left = that.getValue();
		IRational right = this.getValue();
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(left.compare(right)), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareReal(RealResult that) {
		return that.compare(widenToReal());
	}
	
	
	@Override
	protected <U extends IValue> Result<U> compareNumber(NumberResult that) {
		// note: reversed arguments
		// note: reversed arguments
		INumber left = that.getValue();
		IRational right = this.getValue();
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(left.compare(right)), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> equalToRational(RationalResult that) {
		return that.equalityBoolean(this);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToRational(RationalResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that) {
		return that.nonEquals(widenToReal());
	}

	@Override
	protected <U extends IValue> Result<U> lessThanRational(RationalResult that) {
		// note reversed args: we need that < this
		return bool((that.comparisonInts(this) < 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualRational(RationalResult that) {
		// note reversed args: we need that <= this
		return bool((that.comparisonInts(this) <= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanRational(RationalResult that) {
		// note reversed args: we need that > this
		return bool((that.comparisonInts(this) > 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualRational(RationalResult that) {
		// note reversed args: we need that >= this
		return bool((that.comparisonInts(this) >= 0), ctx);
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
		return widenToReal().greaterThan(this);
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
