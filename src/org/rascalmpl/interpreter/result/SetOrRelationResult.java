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

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class SetOrRelationResult<T extends ISet> extends CollectionResult<T> {

	SetOrRelationResult(Type type, T value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> elementOf(
			ElementResult<V> elementResult) {
				return bool(getValue().contains(elementResult.getValue()), ctx);
			}

	protected <U extends IValue, V extends IValue> Result<U> notElementOf(
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
		return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractRelation(RelationResult s) {
				// note the reverse subtract
				return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()), ctx);
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
				Type newType = getTypeFactory().setType(that.getType().lub(getType().getElementType()));
				return makeResult(newType, getValue().insert(that.getValue()), ctx);
			}

	protected <U extends IValue, V extends IValue> Result<U> removeElement(
			ElementResult<V> valueResult) {
				return makeResult(type, getValue().delete(valueResult.getValue()), ctx);
			}

	@Override
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that) {
				return that.equalityBoolean(this);
			}

	@Override
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that) {
				return that.nonEqualityBoolean(this);
			}

	@Override
	protected <U extends IValue> Result<U> equalToSet(SetResult that) {
		return bool(getValue().isEqual(that.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that) {
		return bool(!getValue().isEqual(that.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> lessThanSet(SetResult that) {
		// note reversed args: we need that < this
		return bool(that.getValue().isSubsetOf(getValue()) && !that.getValue().isEqual(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that) {
				// note reversed args: we need that <= this
				return bool(that.getValue().isSubsetOf(getValue()), ctx);
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanSet(SetResult that) {
		// note reversed args: we need that > this
		return bool(getValue().isSubsetOf(that.getValue()) && !getValue().isEqual(that.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that) {
				// note reversed args: we need that >= this
				return bool(getValue().isSubsetOf(that.getValue()), ctx);
			}

	@Override
	protected <U extends IValue> Result<U> lessThanRelation(RelationResult that) {
				// note reversed args: we need that < this
				return bool(that.getValue().isSubsetOf(getValue()) && !that.getValue().isEqual(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that) {
				// note reversed args: we need that <= this
				return bool(that.getValue().isSubsetOf(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that) {
	  return bool(getValue().isSubsetOf(that.getValue()) && !getValue().isEqual(that.getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualRelation(RelationResult that) {
	  return bool(getValue().isSubsetOf(that.getValue()), ctx);
	}
}
