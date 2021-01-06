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


import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.rascalmpl.interpreter.IEvaluatorContext;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class SetResult extends SetOrRelationResult<ISet> {

	public SetResult(Type type, ISet set, IEvaluatorContext ctx) {
		super(type, set, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result) {
		return result.multiplySet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> join(Result<V> that) {
		return that.joinSet(this);
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result) {
		return result.intersectSet(this);
	}
	
	
	@Override
	public <V extends IValue> Result<IBool> in(Result<V> result) {
		return result.inSet(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> notIn(Result<V> result) {
		return result.notInSet(this);
	}

	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToSet(this);
	}

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToSet(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> lessThan(Result<V> that) {
		return that.lessThanSet(this);
	}
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> that) {
		return that.lessThanOrEqualSet(this);
	}

	@Override
	public <V extends IValue> Result<IBool> greaterThan(Result<V> that) {
		return that.greaterThanSet(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> that) {
		return that.greaterThanOrEqualSet(this);
	}

	@Override
	protected LessThanOrEqualResult lessThanOrEqualSet(SetResult that) {
	  boolean isSubset = that.getValue().isSubsetOf(getValue());
    boolean equals = that.getValue().equals(getValue());
    return new LessThanOrEqualResult(isSubset && !equals, equals, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> joinRelation(RelationResult that) {
		// Note the reverse of arguments, we need "that join this"
		int arity1 = that.getValue().asRelation().arity();
		Type eltType = getStaticType().getElementType();
		Type tupleType = that.getStaticType().getElementType();
		Type fieldTypes[] = new Type[arity1 + 1];
		for (int i = 0;  i < arity1; i++) {
			fieldTypes[i] = tupleType.getFieldType(i);
		}
		fieldTypes[arity1] = eltType;
		Type resultTupleType = getTypeFactory().tupleType(fieldTypes);
		ISetWriter writer = getValueFactory().setWriter();
		IValue fieldValues[] = new IValue[arity1 + 1];
		for (IValue relValue: that.getValue()) {
			for (IValue setValue: this.getValue()) {
				for (int i = 0; i < arity1; i++) {
					fieldValues[i] = ((ITuple)relValue).get(i);
				}
				fieldValues[arity1] = setValue;
				writer.insert(getValueFactory().tuple(fieldValues));
			}
		}
		Type resultType = getTypeFactory().relTypeFromTuple(resultTupleType);
		return makeResult(resultType, writer.done(), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> joinSet(SetResult that) {
		// Note the reverse of arguments, we need "that join this"
		// join between sets degenerates to product
		Type tupleType = getTypeFactory().tupleType(that.getStaticType().getElementType(), 
				getStaticType().getElementType());
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType),
				that.getValue().product(getValue()), ctx);
	}
}
