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

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class ListResult extends CollectionResult<IList> {
	
	public ListResult(Type type, IList list, IEvaluatorContext ctx) {
		super(type, list, ctx);
	}
		
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addList(this);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractList(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result) {
		return result.intersectList(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result) {
		return result.multiplyList(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> in(Result<V> result) {
		return result.inList(this);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> result) {
		return result.notInList(this);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result) {
		return result.compareList(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToList(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToList(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that) {
		return that.lessThanList(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that) {
		return that.lessThanOrEqualList(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that) {
		return that.greaterThanList(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that) {
		return that.greaterThanOrEqualList(this);
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
		if (getValue().length() == 0) {
			throw RuntimeExceptionFactory.emptyList(ctx.getCurrentAST(), ctx.getStackTrace());
		}
		IInteger index = ((IInteger)key.getValue());
		if ( (index.intValue() >= getValue().length()) || (index.intValue() < 0) ) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		return makeResult(getType().getElementType(), getValue().get(index.intValue()), ctx);
	}

	/////
	
	@Override
	protected <U extends IValue> Result<U> addList(ListResult l) {
		// Note the reverse concat
		return makeResult(getType().lub(l.getType()), l.getValue().concat(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractList(ListResult l) {
		// Note the reversal of args
		IList list = l.getValue();
		for (IValue v: getValue()) {
			if (list.contains(v)) {
				list = list.delete(v);
			}
		}
		return makeResult(l.getType(), list, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> multiplyList(ListResult that) {
		Type t1 = that.type.getElementType();
		Type t2 = type.getElementType();
		// Note: reverse
		Type type = getTypeFactory().listType(getTypeFactory().tupleType(t1, t2));
		IListWriter w = type.writer(getValueFactory());
		for (IValue v1 : that.getValue()) {
			for (IValue v2 : getValue()) {
				w.append(getValueFactory().tuple(v1, v2));	
			}
		}
		return makeResult(type, w.done(), ctx);	
	}
	
	@Override
	protected <U extends IValue> Result<U> intersectList(ListResult that) {
		// Note the reversal of args
		IListWriter w = type.writer(getValueFactory());
		Type type = getType().lub(that.getType());
		for(IValue v : that.getValue())
			if(getValue().contains(v))
				w.append(v);
		return makeResult(type, w.done(), ctx);
	}
	
	
	@Override
	protected <U extends IValue, V extends IValue> Result<U> insertElement(Result<V> that) {
		Type newType = getTypeFactory().listType(that.getType().lub(getType().getElementType()));
		return makeResult(newType, value.insert(that.getValue()), ctx);
	}
	
	<U extends IValue, V extends IValue> Result<U> appendElement(ElementResult<V> that) {
		// this is called by addLists in element types.
		Type newType = getTypeFactory().listType(that.getType().lub(getType().getElementType()));
		return makeResult(newType, value.append(that.getValue()), ctx);
	}

	<U extends IValue, V extends IValue> Result<U> removeElement(ElementResult<V> value) {
		IList list = getValue();
		return makeResult(getType(), list.delete(value.getValue()), ctx);
	}

	<U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult) {
		return bool((getValue().contains(elementResult.getValue())), ctx);
	}

	<U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult) {
		return bool((!getValue().contains(elementResult.getValue())), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToList(ListResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanList(ListResult that) {
		// note reverse of arguments: we need that < this
		// TODO: move to PDB:
		if (that.getValue().isEqual(getValue())) {
			return bool(false, ctx);
		}
		return lessThanOrEqualList(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualList(ListResult that) {
		for (IValue value: that.getValue()) {
			if (!getValue().contains(value)) {
				return bool(false, ctx);
			}
		}
		return bool(true, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanList(ListResult that) {
		// note double reversal of arguments: that >  this
		return that.lessThanList(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualList(ListResult that) {
		// note double reversal of arguments: that >=  this
		return that.lessThanOrEqualList(this);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> compareList(ListResult that) {
		// Note reversed args
		IList left = that.getValue();
		IList right = this.getValue();
		int compare = Integer.valueOf(left.length()).compareTo(Integer.valueOf(right.length()));
	
		// TODO: think about what <= on lists should mean
		if (compare != 0) {
			return makeIntegerResult(compare);
		}
		for (int i = 0; i < left.length(); i++) {
			compare = compareIValues(left.get(i), right.get(i), ctx);
			if (compare != 0) {
				return makeIntegerResult(compare);
			}
		}
		return makeIntegerResult(0);
	}

	
}
