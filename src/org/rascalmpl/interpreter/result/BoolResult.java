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

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class BoolResult extends ElementResult<IBool> {
	
	public BoolResult(Type type, IBool bool, IEvaluatorContext ctx) {
		this(type, bool, null, ctx);
	}
		
	public BoolResult(Type type, IBool bool, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		super(type, bool, iter, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result) {
		return result.lessThanBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result) {
		return result.greaterThanBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualBool(this);
	}
	
	@Override
	public <U extends IValue> Result<U> negate() {
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
	protected <U extends IValue> Result<U> equalToBool(BoolResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualBool(BoolResult that) {
	  // false < true or true <= true
		return bool(!that.isTrue() || isTrue(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanBool(BoolResult that) {
	  return bool(!that.isTrue() && isTrue(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanBool(BoolResult that) {
	  return that.lessThanBool(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualBool(BoolResult that) {
	  return that.lessThanOrEqualBool(this);
	}

	@Override
	public boolean isTrue() {
		return getValue().getValue();
	}
}
