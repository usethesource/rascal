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

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class StringResult extends ElementResult<IString> {

	private IString string;
	
	public StringResult(Type type, IString string, IEvaluatorContext ctx) {
		super(type, string, ctx);
		this.string = string;
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
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToString(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToString(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result) {
		return result.lessThanString(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualString(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result) {
		return result.greaterThanString(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualString(this);
	}
	
	//////////////////////
//	
//	@Override
//	protected <U extends IValue> Result<U> addString(StringResult s) {
//		// Note the reverse concat.
//		return makeResult(type, s.getValue().concat(getValue()), ctx);
//	}	
	
	@SuppressWarnings("unchecked")
	@Override
	protected <U extends IValue> Result<U> addString(StringResult s) {
		// Note the reverse concat.
		return (Result<U>) new ConcatStringResult(getType(), s, this, ctx); 
	}	
	
	@Override
	protected <U extends IValue> Result<U> equalToString(StringResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanString(StringResult that) {
	  return bool(that.getValue().compare(getValue()) > 0, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualString(StringResult that) {
	  return bool(that.getValue().compare(getValue()) >= 0, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanString(StringResult that) {
	  return bool(that.getValue().compare(getValue()) < 0, ctx);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		String name = getValue().getValue();
		if (!getTypeFactory().isIdentifier(name)) {
			throw RuntimeExceptionFactory.illegalIdentifier(name, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		IValue node = getTypeFactory().nodeType().make(getValueFactory(), name, argValues);
		return makeResult(getTypeFactory().nodeType(), node, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToString(StringResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualString(StringResult that) {
		// note reversed args: we need that <= this
		return bool((that.getValue().compare(getValue()) <= 0), ctx);
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
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ctx.getCurrentAST());
		}
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!key.getType().isIntegerType()) {
			throw new UnexpectedTypeError(TypeFactory.getInstance().integerType(), key.getType(), ctx.getCurrentAST());
		}
		if (getValue().getValue().length() == 0) {
			throw RuntimeExceptionFactory.illegalArgument(ctx.getCurrentAST(), ctx.getStackTrace());
		}
		IInteger index = ((IInteger)key.getValue());
		if ( (index.intValue() >= getValue().getValue().length()) || (index.intValue() < 0) ) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		return makeResult(getType(), getValueFactory().string(getValue().getValue().substring(index.intValue(), index.intValue() + 1)), ctx);
	}
}
