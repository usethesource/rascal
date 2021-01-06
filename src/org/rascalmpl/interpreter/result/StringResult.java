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

import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArity;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class StringResult extends ElementResult<IString> {

	private IString string;
	
	public StringResult(Type type, IString string, IEvaluatorContext ctx) {
		super(type, string, ctx);
		this.string = string;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Result<IBool> isKeyDefined(Result<?>[] subscripts) {
		if (subscripts.length != 1) { 
			throw new UnsupportedSubscriptArity(getStaticType(), subscripts.length, ctx.getCurrentAST());
		} 
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!key.getStaticType().isSubtypeOf(getTypeFactory().integerType())) {
			throw new UnexpectedType(getTypeFactory().integerType(), key.getStaticType(), ctx.getCurrentAST());
		}
		int idx = ((IInteger) key.getValue()).intValue();
		int len = getValue().length();
		
		if ((idx >= 0 && idx >= len) || (idx < 0 && idx < -len)){
			return makeResult(getTypeFactory().boolType(), getValueFactory().bool(false), ctx);
		}
		
		return makeResult(getTypeFactory().boolType(), getValueFactory().bool(true), ctx);
	}
	
	@Override
	public IString getValue() {
		return string;
	}
	
	protected int length() {
		return string.getValue().length();
	}
	
	protected void yield(StringBuilder b) {
		b.append(string.getValue());
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addString(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToString(this);
	}

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToString(this);
	}

	@Override
	public <V extends IValue> Result<IBool> lessThan(Result<V> result) {
		return result.lessThanString(this);
	}
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualString(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThan(Result<V> result) {
		return result.greaterThanString(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualString(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> addString(StringResult s) {
	    // note the reversal
	    return makeResult(getStaticType(), s.getValue().concat(getValue()), ctx);
	}	
	
	@Override
	protected Result<IBool> equalToString(StringResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected Result<IBool> greaterThanString(StringResult that) {
	  return bool(that.getValue().compare(getValue()) > 0, ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualString(StringResult that) {
	  return bool(that.getValue().compare(getValue()) >= 0, ctx);
	}
	
	@Override
	protected Result<IBool> lessThanString(StringResult that) {
	  return bool(that.getValue().compare(getValue()) < 0, ctx);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
		String name = getValue().getValue();
		IValue node = this.getValueFactory().node(name, argValues, keyArgValues);
		return makeResult(getTypeFactory().nodeType(), node, ctx);
	}
	
	@Override
	protected Result<IBool> nonEqualToString(StringResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualString(StringResult that) {
		int cmp = that.getValue().compare(getValue());
    return new LessThanOrEqualResult(cmp < 0, cmp == 0, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> addSourceLocation(
			SourceLocationResult that) {
		Result<IValue> path = that.fieldAccess("path", new TypeStore());
		String parent = ((IString) path.getValue()).getValue();
		String child = getValue().getValue();
		if (parent.endsWith("/")) {
			parent = parent.substring(0, parent.length() - 1);
		}
		if (!child.startsWith("/")) {
			child = "/" + child;
		}
		
		return that.fieldUpdate("path", makeResult(getTypeFactory().stringType(), getValueFactory().string(parent + child), ctx), new TypeStore());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArity(getStaticType(), subscripts.length, ctx.getCurrentAST());
		}
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!key.getStaticType().isInteger()) {
			throw new UnexpectedType(TypeFactory.getInstance().integerType(), key.getStaticType(), ctx.getCurrentAST());
		}
		if (getValue().getValue().length() == 0) {
			throw RuntimeExceptionFactory.illegalArgument(ctx.getCurrentAST(), ctx.getStackTrace());
		}
		IInteger index = ((IInteger)key.getValue());
		int idx = index.intValue();
		if(idx < 0){
			idx = idx + getValue().length();
		}
		if ( (idx >= getValue().length()) || (idx < 0) ) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		return makeResult(getStaticType(), getValue().substring(idx, idx + 1), ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> slice(Result<?> first, Result<?> second, Result<?> end) {
		return super.slice(first, second, end, getValue().length());
	}
	
	public Result<IValue> makeSlice(int first, int second, int end){
		StringBuilder buffer = new StringBuilder();
		IString s = getValue();
		int increment = second - first;
		if(first == end || increment == 0){
			// nothing to be done
		} else
		if(first <= end){
			for(int i = first; i >= 0 && i < end; i += increment){
				buffer.appendCodePoint(s.charAt(i));
			}
		} else {
			for(int j = first; j >= 0 && j > end && j < getValue().length(); j += increment){
				buffer.appendCodePoint(s.charAt(j));
			}
		}
		return makeResult(TypeFactory.getInstance().stringType(), getValueFactory().string(buffer.toString()), ctx);
	}
	
}
