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

import java.util.Iterator;
import java.util.Map.Entry;

import org.rascalmpl.ast.Field;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UndeclaredField;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArity;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.UndeclaredFieldException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public class MapResult extends ElementResult<IMap> {
	
	public MapResult(Type type, IMap map, IEvaluatorContext ctx) {
		super(type, map, ctx);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addMap(this);
		
	}

	@Override 
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractMap(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result) {
		return result.intersectMap(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArity(getStaticType(), subscripts.length, ctx.getCurrentAST());
		}
		
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!getStaticType().getKeyType().comparable(key.getStaticType())) {
			throw new UnexpectedType(getStaticType().getKeyType(), key.getStaticType(), ctx.getCurrentAST());
		}
		IValue v = getValue().get(key.getValue());
		if (v == null){
			throw RuntimeExceptionFactory.noSuchKey(key.getValue(), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		return makeResult(getStaticType().getValueType(), v, ctx);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Result<IBool> isKeyDefined(Result<?>[] subscripts) {
		if (subscripts.length != 1) { 
			throw new UnsupportedSubscriptArity(getStaticType(), subscripts.length, ctx.getCurrentAST());
		} 
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!getStaticType().getKeyType().comparable(key.getStaticType())) {
			throw new UnexpectedType(getStaticType().getKeyType(), key.getStaticType(), ctx.getCurrentAST());
		}
		IValue v = getValue().get(key.getValue());
		if (v == null){
			return makeResult(getTypeFactory().boolType(), getValueFactory().bool(false), ctx);
		}
		
		return makeResult(getTypeFactory().boolType(), getValueFactory().bool(true), ctx);
	}
	
	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToMap(this);
	}

	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
		if (name.equals(type.getKeyLabel())) {
			ISetWriter w = getValueFactory().setWriter();
			w.insertAll(value);
			return makeResult(getTypeFactory().setType(type.getKeyType()), w.done(), ctx);
		}
		else if (name.equals(type.getValueLabel())) {
			ISetWriter w = getValueFactory().setWriter();
			Iterator<IValue> it = value.valueIterator();
			while (it.hasNext()) {
				w.insert(it.next());
			}
			return makeResult(getTypeFactory().setType(type.getValueType()), w.done(), ctx);
		}

		throw new UndeclaredField(name, type, ctx.getCurrentAST());
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(
			String name, Result<V> repl, TypeStore store) {
		if(type.getKeyLabel() != null){
			if (type.getKeyLabel().equals(name)) {
				throw new UnsupportedOperation("You can not update the keys of a map using the field update operator", ctx.getCurrentAST());
			}
			else if (type.getValueLabel().equals(name)) {
				// interesting operation, sets the image of all keys to one default
				if (!repl.getStaticType().isSubtypeOf(type.getValueType())) {
					throw new UnexpectedType(type.getValueType(), repl.getStaticType(), ctx.getCurrentAST());
				}

				IMapWriter w = getValueFactory().mapWriter();

				for (IValue key : value) {
					w.put(key, repl.getValue());
				}

				return makeResult(type, w.done(), ctx);
			}
		}
		
		throw new UndeclaredFieldException(type, name);
	}
	
	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToMap(this);
	}

	@Override
	public <V extends IValue> Result<IBool> lessThan(Result<V> that) {
		return that.lessThanMap(this);
	}
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> that) {
		return that.lessThanOrEqualMap(this);
	}

	@Override
	public <V extends IValue> Result<IBool> greaterThan(Result<V> that) {
		return that.greaterThanMap(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> that) {
		return that.greaterThanOrEqualMap(this);
	}

	
	@Override
	public <V extends IValue> Result<IBool> in(Result<V> result) {
		return result.inMap(this);
	}	
	
	@Override
	public <V extends IValue> Result<IBool> notIn(Result<V> result) {
		return result.notInMap(this);
	}	
	
	////
	
	protected <V extends IValue> Result<IBool> elementOf(ElementResult<V> elementResult) {
		return bool(getValue().containsKey(elementResult.getValue()), ctx);
	}

	protected <V extends IValue> Result<IBool> notElementOf(ElementResult<V> elementResult) {
		return bool(!getValue().containsKey(elementResult.getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> addMap(MapResult m) {
		// Note the reverse
		return makeResult(getStaticType().lub(m.getStaticType()), m.value.join(value), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> addListRelation(ListRelationResult that) {
		return that.addMap(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractMap(MapResult m) {
		// Note the reverse
		return makeResult(m.getStaticType(), m.getValue().remove(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> intersectMap(MapResult m) {
		// Note the reverse
		return makeResult(m.getStaticType(), m.getValue().common(getValue()), ctx);
	}

	
	@Override
	protected Result<IBool> equalToMap(MapResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected Result<IBool> nonEqualToMap(MapResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected Result<IBool> lessThanMap(MapResult that) {
		// note reversed args: we need that < this
		return bool(that.getValue().isSubMap(getValue()) && !that.getValue().equals(getValue()), ctx);
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualMap(MapResult that) {
	  boolean subMap = that.getValue().isSubMap(getValue());
	  boolean equals = that.getValue().equals(getValue());
	  return new LessThanOrEqualResult(subMap && !equals, equals, ctx);
	}

	@Override
	protected Result<IBool> greaterThanMap(MapResult that) {
		LessThanOrEqualResult loe = that.lessThanOrEqualMap(this);
		if (!loe.getEqual()) {
		  return loe.isLess();
		}
		return bool(false, ctx);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualMap(MapResult that) {
	  return that.lessThanOrEqualMap(this);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(
			Result<V> right) {
		return right.composeMap(this);
	}
	
	@Override
	public <U extends IValue> Result<U> composeMap(MapResult left) {
		if (left.getStaticType().getValueType().comparable(getStaticType().getKeyType())) {		
			Type mapType = getTypeFactory().mapType(left.getStaticType().getKeyType(), getStaticType().getValueType());
			return ResultFactory.makeResult(mapType, left.getValue().compose(getValue()), ctx);
		}
		
		return undefinedError("composition", left);
	}
	
	@Override
	public Result<IValue> fieldSelect(int[] selectedFields) {
		ISetWriter w = getValueFactory().setWriter();
		
		// TODO: poor mans implementation can be made much faster without intermediate relation building
		Iterator<Entry<IValue,IValue>> it = value.entryIterator();
		while (it.hasNext()) {
			Entry<IValue,IValue> entry = it.next();
			w.insert(getValueFactory().tuple(entry.getKey(), entry.getValue()));
		}
		
		return makeResult(getTypeFactory().setType(type.getFieldTypes()), w.done(), ctx).fieldSelect(selectedFields);
	}
	
	@Override
	public Result<IValue> fieldSelect(Field[] selectedFields) {
		int nFields = selectedFields.length;
		int fieldIndices[] = new int[nFields];
		Type baseType = this.getStaticType();
		
		for (int i = 0; i < nFields; i++) {
			Field f = selectedFields[i];
			if (f.isIndex()) {
				fieldIndices[i] = ((IInteger) f.getFieldIndex()
						.interpret(this.ctx.getEvaluator()).getValue()).intValue();
			} else {
				String fieldName = org.rascalmpl.interpreter.utils.Names
						.name(f.getFieldName());
				try {
					fieldIndices[i] = baseType.getFieldIndex(fieldName);
				} catch (UndeclaredFieldException e) {
					throw new UndeclaredField(fieldName, baseType,
							ctx.getCurrentAST());
				}
			}

			if (fieldIndices[i] < 0 || fieldIndices[i] > 1) {
				throw org.rascalmpl.exceptions.RuntimeExceptionFactory
						.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(fieldIndices[i]),
								ctx.getCurrentAST(), ctx.getStackTrace());
			}
		}
		
		return this.fieldSelect(fieldIndices);
	}
	
}
