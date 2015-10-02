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

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArity;

public class ListOrRelationResult<T extends IList> extends CollectionResult<T> {

	ListOrRelationResult(Type type, T value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}

	protected <V extends IValue> Result<IBool> elementOf(ElementResult<V> elementResult) {
				return bool(getValue().contains(elementResult.getValue()), ctx);
			}

	protected <V extends IValue> Result<IBool> notElementOf(ElementResult<V> elementResult) {
		return bool(!getValue().contains(elementResult.getValue()), ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> slice(Result<?> first, Result<?> second, Result<?> end) {
		return super.slice(first, second, end, getValue().length());
	}
	
	public Result<IValue> makeSlice(int first, int second, int end){
		IListWriter w = getValueFactory().listWriter();
		int increment = second - first;
		if (first == end || increment == 0) {
			// nothing to be done
		} else if (first <= end) {
			if (increment == 1) {
				return makeResult(TypeFactory.getInstance().listType(getType().getElementType()), getValue().sublist(first, end - first), ctx);
			}
			else {
				for (int i = first; i >= 0 && i < end; i += increment) {
					w.append(getValue().get(i));
				}
			}
		} else {
			for (int j = first; j >= 0 && j > end && j < getValue().length(); j += increment) {
				w.append(getValue().get(j));
			}
		}
		return makeResult(TypeFactory.getInstance().listType(getType().getElementType()), w.done(), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> addList(ListResult s) {
		return makeResult(type.lub(s.type), s.getValue().concat(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> addListRelation(ListRelationResult s) {
		return makeResult(type.lub(s.type), s.getValue().concat(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractList(ListResult s) {
		// note the reverse subtract
		return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractListRelation(ListRelationResult s) {
		// note the reverse subtract
		return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> multiplyListRelation(ListRelationResult that) {
		Type tupleType = getTypeFactory().tupleType(that.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(getTypeFactory().lrelTypeFromTuple(tupleType), that.getValue().product(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> multiplyList(ListResult s) {
		Type tupleType = getTypeFactory().tupleType(s.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(getTypeFactory().lrelTypeFromTuple(tupleType), s.getValue().product(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> intersectList(ListResult s) {
		return makeResult(type.lub(s.type), s.getValue().intersect(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> intersectListRelation(ListRelationResult s) {
		return makeResult(type.lub(s.type), s.getValue().intersect(getValue()), ctx);
	}

	@Override
	protected <U extends IValue, V extends IValue> Result<U> insertElement(Result<V> that) {
		Type newType = getTypeFactory().listType(that.getType().lub(getType().getElementType()));
		return makeResult(newType, value.insert(that.getValue()), ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> addElement(
			ElementResult<V> that) {
		Type newType = getTypeFactory().listType(that.getType().lub(getType().getElementType()));
		return makeResult(newType, getValue().append(that.getValue()), ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> removeElement(
			ElementResult<V> valueResult) {
		return makeResult(type, getValue().delete(valueResult.getValue()), ctx);
	}

	@Override
	protected Result<IBool> equalToListRelation(ListRelationResult that) {
		return that.equalityBoolean(this);
	}

	@Override
	protected Result<IBool> nonEqualToListRelation(ListRelationResult that) {
		return that.nonEqualityBoolean(this);
	}

	@Override
	protected Result<IBool> equalToList(ListResult that) {
		return bool(getValue().isEqual(that.getValue()), ctx);
	}

	@Override
	protected Result<IBool> nonEqualToList(ListResult that) {
		return bool(!getValue().isEqual(that.getValue()), ctx);
	}

	@Override
	protected Result<IBool> lessThanList(ListResult that) {
	  LessThanOrEqualResult loe = lessThanOrEqualList(that);
		return bool(loe.getLess() && !loe.getEqual(), ctx);
	}

	@Override
	protected LessThanOrEqualResult lessThanOrEqualList(ListResult that) {
		IList left = that.getValue();
    IList right = getValue();
    boolean eq = left.isEqual(right);
    return new LessThanOrEqualResult(left.isSubListOf(right) && !eq, eq, ctx);
	}

	@Override
	protected Result<IBool> greaterThanList(ListResult that) {
		// note reversed args: we need that > this
		return bool(getValue().isSubListOf(that.getValue()) && !getValue().isEqual(that.getValue()), ctx);
	}

	@Override
	protected Result<IBool> greaterThanOrEqualList(ListResult that) {
		// note reversed args: we need that >= this
		return bool(getValue().isSubListOf(that.getValue()), ctx);
	}

	@Override
	protected Result<IBool> lessThanListRelation(ListRelationResult that) {
		// note reversed args: we need that < this
	  LessThanOrEqualResult loe = lessThanOrEqualListRelation(that);
		return bool(loe.getLess() && !loe.getEqual(), ctx);
	}

	@Override
	protected LessThanOrEqualResult lessThanOrEqualListRelation(ListRelationResult that) {
	  IList left = that.getValue();
    IList right = getValue();
    boolean eq = left.isEqual(right);
    return new LessThanOrEqualResult(left.isSubListOf(right) && !eq, eq, ctx);
	}

	@Override
	protected Result<IBool> greaterThanListRelation(ListRelationResult that) {
		// note reversed args: we need that > this
		return bool(getValue().isSubListOf(that.getValue()) && !getValue().isEqual(that.getValue()), ctx);
	}

	@Override
	protected Result<IBool> greaterThanOrEqualListRelation(
			ListRelationResult that) {
		// note reversed args: we need that >= this
		return bool(getValue().isSubListOf(that.getValue()), ctx);
	}
}
