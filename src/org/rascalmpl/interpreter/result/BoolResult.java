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

import java.util.Iterator;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public class BoolResult extends ElementResult<IBool> {
	
	public BoolResult(Type type, IBool bool, IEvaluatorContext ctx) {
		this(type, bool, null, ctx);
	}
		
	public BoolResult(Type type, IBool bool, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		super(type, bool, iter, ctx);
	}
	
	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToBool(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToBool(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> lessThan(Result<V> result) {
		return result.lessThanBool(this);
	}
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualBool(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThan(Result<V> result) {
		return result.greaterThanBool(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualBool(this);
	}
	
	@Override
	public Result<IBool> negate() {
		return bool(getValue().not().getValue(), ctx);
	}
	
	/////
	
	@Override
	public Result<IValue> ifThenElse(Result<IValue> then, Result<IValue> _else) {
		if (isTrue()) {
			return then;
		}
		return _else;
	}
	
	///
	
	
	@Override
	protected Result<IBool> equalToBool(BoolResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected Result<IBool> nonEqualToBool(BoolResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualBool(BoolResult that) {
	  // false < true or true <= true
	  if (that.isTrue()) {
	    return new LessThanOrEqualResult(false, isTrue(), ctx);
	  }
	  else {
	    return new LessThanOrEqualResult(isTrue(), !isTrue(), ctx);
	  }
	}
	
	@Override
	protected Result<IBool> lessThanBool(BoolResult that) {
	  return bool(!that.isTrue() && isTrue(), ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanBool(BoolResult that) {
	  return that.lessThanBool(this);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualBool(BoolResult that) {
	   LessThanOrEqualResult r = that.lessThanOrEqualBool(this);
	   return r;
	}

	@Override
	public boolean isTrue() {
		return getValue().getValue();
	}
	
	@Override
	protected <U extends IValue> Result<U> addListRelation(ListRelationResult that) {
		return that.addBool(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult that) {
		return that.addBool(this);
	}
}
