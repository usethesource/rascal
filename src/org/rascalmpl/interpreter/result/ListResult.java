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

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArity;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class ListResult extends ListOrRelationResult<IList> {
	
	public ListResult(Type type, IList list, IEvaluatorContext ctx) {
		super(type, list, ctx);
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
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addList(this);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractList(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> join(Result<V> that) {
		return that.joinList(this);
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
	public <V extends IValue> Result<IBool> in(Result<V> result) {
		return result.inList(this);
	}	
	
	@Override
	public <V extends IValue> Result<IBool> notIn(Result<V> result) {
		return result.notInList(this);
	}	
	
	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToList(this);
	}

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToList(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> lessThan(Result<V> that) {
		return that.lessThanList(this);
	}
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> that) {
		return that.lessThanOrEqualList(this);
	}

	@Override
	public <V extends IValue> Result<IBool> greaterThan(Result<V> that) {
		return that.greaterThanList(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> that) {
		return that.greaterThanOrEqualList(this);
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
		if (getValue().length() == 0) {
			throw RuntimeExceptionFactory.emptyList(ctx.getCurrentAST(), ctx.getStackTrace());
		}
		IInteger index = ((IInteger)key.getValue());
		
		int idx = index.intValue();
		if(idx < 0){
			idx = idx + getValue().length();
		}
		if ( (idx >= getValue().length()) || (idx < 0) ) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		return makeResult(getStaticType().getElementType(), getValue().get(idx), ctx);
	}
	
	/////
	
	@Override
	protected <U extends IValue> Result<U> addList(ListResult l) {
		// Note the reverse concat
		return makeResult(getStaticType().lub(l.getStaticType()), l.getValue().concat(getValue()), ctx);
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
		return makeResult(l.getStaticType(), list, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> multiplyList(ListResult that) {
		Type t1 = that.type.getElementType();
		Type t2 = type.getElementType();
		// Note: reverse
		Type type = getTypeFactory().listType(getTypeFactory().tupleType(t1, t2));
		IListWriter w = this.getValueFactory().listWriter();
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
		IListWriter w = this.getValueFactory().listWriter();
		Type type = getStaticType().lub(that.getStaticType());
		for(IValue v : that.getValue())
			if(getValue().contains(v))
				w.append(v);
		return makeResult(type, w.done(), ctx);
	}
	
	<U extends IValue, V extends IValue> Result<U> appendElement(ElementResult<V> that) {
		// this is called by addLists in element types.
		Type newType = getTypeFactory().listType(that.getStaticType().lub(getStaticType().getElementType()));
		return makeResult(newType, value.append(that.getValue()), ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> removeElement(ElementResult<V> value) {
		IList list = getValue();
		return makeResult(getStaticType(), list.delete(value.getValue()), ctx);
	}

	protected <V extends IValue> Result<IBool> elementOf(ElementResult<V> elementResult) {
		return bool((getValue().contains(elementResult.getValue())), ctx);
	}

	protected <V extends IValue> Result<IBool> notElementOf(ElementResult<V> elementResult) {
		return bool((!getValue().contains(elementResult.getValue())), ctx);
	}
	
	@Override
	protected Result<IBool> equalToList(ListResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected Result<IBool> nonEqualToList(ListResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected Result<IBool> nonEqualToListRelation(ListRelationResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected Result<IBool> greaterThanList(ListResult that) {
	  return that.lessThanList(this);
	}
	
	@Override
	protected Result<IBool> greaterThanOrEqualList(ListResult that) {
	  return that.lessThanOrEqualList(this);
	}
	
	@Override
	protected Result<IBool> lessThanList(ListResult that) {
	  IList val = that.getValue();
    
    if (val.length() > value.length()) {
      return bool(false, ctx);
    }
    
    OUTER:for (int iThat = 0, iThis = 0; iThat < val.length(); iThat++) {
      for (iThis = Math.max(iThis, iThat) ; iThis < value.length(); iThis++) {
        if (val.get(iThat).equals(value.get(iThis))) {
          iThis++;
          continue OUTER;
        }
      }
      return bool(false, ctx);
    }

    return bool(val.length() != value.length(), ctx);
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualList(ListResult that) {
	  IList left = that.getValue();
	  IList right = getValue();
	  
	  if (left.length() == 0) {
	    return new LessThanOrEqualResult(right.length() > 0, right.length() == 0, ctx);
	  }
	  else if (left.length() > right.length()) {
	    return new LessThanOrEqualResult(false, false, ctx);
	  }
	  
		OUTER:for (int iThat = 0, iThis = 0; iThat < left.length(); iThat++) {
		  for (iThis = Math.max(iThis, iThat) ; iThis < right.length(); iThis++) {
		    if (left.get(iThat).equals(right.get(iThis))) {
		      continue OUTER;
		    }
		  }
		  return new LessThanOrEqualResult(false, false, ctx);
		}
	  
		return new LessThanOrEqualResult(left.length() < right.length(), left.length() == right.length(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> joinListRelation(ListRelationResult that) {
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
		IListWriter writer = getValueFactory().listWriter();
		IValue fieldValues[] = new IValue[arity1 + 1];
		for (IValue relValue: that.getValue()) {
			for (IValue setValue: this.getValue()) {
				for (int i = 0; i < arity1; i++) {
					fieldValues[i] = ((ITuple)relValue).get(i);
				}
				fieldValues[arity1] = setValue;
				writer.append(getValueFactory().tuple(fieldValues));
			}
		}
		Type resultType = getTypeFactory().lrelTypeFromTuple(resultTupleType);
		return makeResult(resultType, writer.done(), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> joinList(ListResult that) {
		// Note the reverse of arguments, we need "that join this"
		// join between sets degenerates to product
		Type tupleType = getTypeFactory().tupleType(that.getStaticType().getElementType(), 
				getStaticType().getElementType());
		return makeResult(getTypeFactory().lrelTypeFromTuple(tupleType),
				that.getValue().product(getValue()), ctx);
	}
	
}
