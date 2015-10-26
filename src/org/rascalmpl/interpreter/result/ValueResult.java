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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.Iterator;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public class ValueResult extends ElementResult<IValue> {

	public ValueResult(Type type, IValue value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}
	
	public ValueResult(Type type, IValue value, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		super(type, value, iter, ctx);
	}
	
	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToValue(this);
	}
	

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return equals(that).negate();
	}

	
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> that) {
	  Type thisRuntimeType = getValue().getType();
    Type thatRuntimeType = that.getValue().getType();
    
    if (thisRuntimeType.comparable(thatRuntimeType)) {
	    return makeResult(thisRuntimeType, getValue(), ctx).lessThanOrEqual(makeResult(thatRuntimeType, that.getValue(), ctx));
	  }
    else {
      return new LessThanOrEqualResult(false, false, ctx);
    }
	}
	
	@Override
	protected Result<IBool> equalToInteger(IntegerResult that) {
	  return equalityBoolean(that);
	}

	@Override
	protected Result<IBool> equalToReal(RealResult that) {
	  return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToRational(RationalResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToNumber(NumberResult that) {
		return equalityBoolean(that);
	}

	@Override
	protected Result<IBool> equalToString(StringResult that) {
	  return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToList(ListResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToSet(SetResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToMap(MapResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToNode(NodeResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToSourceLocation(SourceLocationResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToRelation(RelationResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToTuple(TupleResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToBool(BoolResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected Result<IBool> equalToValue(ValueResult that) {
	  IValue leftValue = that.getValue();
    IValue rightValue = getValue();
    // value equality should simulate the dynamic equality on the specific types
    return makeResult(rightValue.getType(), rightValue, ctx).equals(makeResult(leftValue.getType(), leftValue, ctx));
	}

	@Override
	protected Result<IBool> equalToDateTime(DateTimeResult that) {
		return equalityBoolean(that);
	}

	
	@Override
	protected Result<IBool> nonEqualToInteger(IntegerResult that) {
		return nonEqualityBoolean(that);
	}

	@Override
	protected Result<IBool> nonEqualToReal(RealResult that) {
		return nonEqualityBoolean(that);
	}

	@Override
	protected Result<IBool> nonEqualToString(StringResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected Result<IBool> nonEqualToList(ListResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected Result<IBool> nonEqualToSet(SetResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected Result<IBool> nonEqualToMap(MapResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected Result<IBool> nonEqualToNode(NodeResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected Result<IBool> nonEqualToSourceLocation(SourceLocationResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected Result<IBool> nonEqualToRelation(RelationResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected Result<IBool> nonEqualToTuple(TupleResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected Result<IBool> nonEqualToBool(BoolResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected Result<IBool> nonEqualToValue(ValueResult that) {
		return nonEqualityBoolean(this);
	}	
	
	@Override
	protected Result<IBool> nonEqualToDateTime(DateTimeResult that) {
		return nonEqualityBoolean(that);
	}
}
