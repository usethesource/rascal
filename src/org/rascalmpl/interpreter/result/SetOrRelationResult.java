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

import org.rascalmpl.interpreter.IEvaluatorContext;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class SetOrRelationResult<T extends ISet> extends CollectionResult<T> {

	SetOrRelationResult(Type type, T value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}

	protected <V extends IValue> Result<IBool> elementOf(
			ElementResult<V> elementResult) {
				return bool(getValue().contains(elementResult.getValue()), ctx);
			}

	protected <V extends IValue> Result<IBool> notElementOf(
			ElementResult<V> elementResult) {
		return bool(!getValue().contains(elementResult.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> addSet(SetResult s) {
		return makeResult(type.lub(s.type), getValue().union(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult s) {
		return makeResult(type.lub(s.type), getValue().union(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractSet(SetResult s) {
		// note the reverse subtract
		return makeResult(s.getStaticType(), s.getValue().subtract(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractRelation(RelationResult s) {
		// note the reverse subtract
		return makeResult(s.getStaticType(), s.getValue().subtract(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that) {
		Type tupleType = getTypeFactory().tupleType(that.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType), that.getValue().product(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> multiplySet(SetResult s) {
		Type tupleType = getTypeFactory().tupleType(s.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType), s.getValue().product(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> intersectSet(SetResult s) {
		return makeResult(type.lub(s.type), getValue().intersect(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> intersectRelation(RelationResult s) {
		return makeResult(type.lub(s.type), getValue().intersect(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue, V extends IValue> Result<U> insertElement(Result<V> valueResult) {
		return addElement((ElementResult<V>) valueResult);
	}

	protected <U extends IValue, V extends IValue> Result<U> addElement(
			ElementResult<V> that) {
		Type newType = getTypeFactory().setType(that.getStaticType().lub(getStaticType().getElementType()));
		return makeResult(newType, getValue().insert(that.getValue()), ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> removeElement(
			ElementResult<V> valueResult) {
		return makeResult(type, getValue().delete(valueResult.getValue()), ctx);
	}

	@Override
	protected Result<IBool> equalToRelation(RelationResult that) {
				return that.equalityBoolean(this);
			}

	@Override
	protected Result<IBool> nonEqualToRelation(RelationResult that) {
				return that.nonEqualityBoolean(this);
			}

	@Override
	protected Result<IBool> equalToSet(SetResult that) {
		return bool(getValue().equals(that.getValue()), ctx);
	}

	@Override
	protected Result<IBool> nonEqualToSet(SetResult that) {
		return bool(!getValue().equals(that.getValue()), ctx);
	}

	@Override
	protected Result<IBool> lessThanSet(SetResult that) {
		// note reversed args: we need that < this
	  LessThanOrEqualResult loe = lessThanOrEqualSet(that);
		return loe.isLess();
	}

	@Override
	protected LessThanOrEqualResult lessThanOrEqualSet(SetResult that) {
	  boolean isSubset = that.getValue().isSubsetOf(getValue());
	  boolean equals = that.getValue().equals(getValue());
	  return new LessThanOrEqualResult(isSubset && !equals, equals, ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanSet(SetResult that) {
		// note reversed args: we need that > this
		return bool(getValue().isSubsetOf(that.getValue()) && !getValue().equals(that.getValue()), ctx);
	}

	@Override
	protected Result<IBool> greaterThanOrEqualSet(SetResult that) {
				// note reversed args: we need that >= this
				return bool(getValue().isSubsetOf(that.getValue()), ctx);
			}

	@Override
	protected Result<IBool> lessThanRelation(RelationResult that) {
				// note reversed args: we need that < this
				return bool(that.getValue().isSubsetOf(getValue()) && !that.getValue().equals(getValue()), ctx);
	}

	@Override
	protected LessThanOrEqualResult lessThanOrEqualRelation(RelationResult that) {
	  boolean isSubset = that.getValue().isSubsetOf(getValue());
    boolean equals = that.getValue().equals(getValue());
    return new LessThanOrEqualResult(isSubset && !equals, equals, ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanRelation(RelationResult that) {
	  return bool(getValue().isSubsetOf(that.getValue()) && !getValue().equals(that.getValue()), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualRelation(RelationResult that) {
	  return bool(getValue().isSubsetOf(that.getValue()), ctx);
	}
}
