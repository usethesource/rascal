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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class ValueResult extends ElementResult<IValue> {

	public ValueResult(Type type, IValue value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}
	
	public ValueResult(Type type, IValue value, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		super(type, value, iter, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToValue(this);
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return equals(that).negate();
	}

	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(
			Result<V> that) {
	  Type thisRuntimeType = getValue().getType();
    Type thatRuntimeType = that.getValue().getType();
    
    if (thisRuntimeType.comparable(thatRuntimeType)) {
	    return makeResult(thisRuntimeType, getValue(), ctx).lessThanOrEqual(makeResult(thatRuntimeType, that.getValue(), ctx));
	  }
    else {
      // incomparable means false
      return bool(false, ctx);
    }
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that) {
		return equalityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that) {
		return equalityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> equalToString(StringResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToList(ListResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToSet(SetResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToMap(MapResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToNode(NodeResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToBool(BoolResult that) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToValue(ValueResult that) {
		return that.equalityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> equalToDateTime(DateTimeResult that) {
		return equalityBoolean(that);
	}

	
	@Override
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that) {
		return nonEqualityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that) {
		return nonEqualityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToString(StringResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToSourceLocation(SourceLocationResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToValue(ValueResult that) {
		return nonEqualityBoolean(this);
	}	
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToDateTime(DateTimeResult that) {
		return nonEqualityBoolean(that);
	}
}
