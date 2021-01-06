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
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.Iterator;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class ElementResult<T extends IValue> extends Result<T> {
	public ElementResult(Type type, T value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}
	
	public ElementResult(Type type, T value, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		super(type, value, iter, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that) {
		return that.insertElement(this);
	}
	
	@Override
	protected Result<IBool> inSet(SetResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected Result<IBool> notInSet(SetResult s) {
		return s.notElementOf(this);
	}
	
	@Override
	protected Result<IBool> inRelation(RelationResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected Result<IBool> inListRelation(ListRelationResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected Result<IBool> notInRelation(RelationResult s) {
		return s.notElementOf(this);
	}
	
	@Override
	protected Result<IBool> notInListRelation(ListRelationResult s) {
		return s.notElementOf(this);
	}
	
	@Override
	protected Result<IBool> inList(ListResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected Result<IBool> notInList(ListResult s) {
		return s.notElementOf(this);
	}
	
	@Override
	protected Result<IBool> inMap(MapResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected Result<IBool> notInMap(MapResult s) {
		return s.notElementOf(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> addSet(SetResult s) {
		return s.addElement(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractSet(SetResult s) {
		return s.removeElement(this);
	}

	@Override
	protected <U extends IValue> Result<U> addList(ListResult s) {
		return s.appendElement(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractList(ListResult s) {
		return s.removeElement(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult that) {
		if (that.getValue().getElementType().isBottom()) {
			return makeResult(getTypeFactory().setType(this.getStaticType()), that.getValue().insert(this.getValue()), ctx);
		}
		return super.addRelation(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractRelation(RelationResult that) {
		if(that.getStaticType().getElementType().isBottom())
			return makeResult(that.getStaticType(), that.getValue(), ctx);
		return super.subtractRelation(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> addListRelation(ListRelationResult that) {
		if (that.getValue().getElementType().isBottom()) {
			return makeResult(getTypeFactory().listType(this.getStaticType()), that.getValue().append(this.getValue()), ctx);
		}
		return super.addListRelation(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractListRelation(ListRelationResult that) {
		if(that.getStaticType().getElementType().isBottom())
			return makeResult(that.getStaticType(), that.getValue(), ctx);
		return super.subtractListRelation(that);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> setAnnotation(String annoName, Result<V> anno, Environment env) {
	    // TODO: simulating annotations still here
	    Type annoType;
	    
	    if (RascalValueFactory.isLegacySourceLocationAnnotation(getStaticType(), annoName)) {
            annoName = RascalValueFactory.Location;
            annoType = getTypeFactory().sourceLocationType();
        }
	    else {
	        annoType = env.getKeywordParameterTypes(getStaticType()).get(annoName);
	    }

		if (getStaticType() != getTypeFactory().nodeType()) {
			if (!anno.getStaticType().isSubtypeOf(annoType)) {
				throw new UnexpectedType(annoType, anno.getStaticType(), ctx.getCurrentAST());
			}
		}

		IValue annotatedBase = ((INode)getValue()).asWithKeywordParameters().setParameter(annoName, anno.getValue());

		return makeResult(getStaticType(), annotatedBase, ctx);
	}

	
	@Override
  public <V extends IValue> Result<IBool> lessThan(Result<V> that) {
    return lessThanOrEqual(that).isLess();
  }
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> that) {
	  return new LessThanOrEqualResult(false, false, ctx);
	}
  
  @Override
  public <V extends IValue> Result<IBool> greaterThan(Result<V> that) {
    return that.lessThan(this);
  }
  
  @Override
  public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> that) {
    return that.lessThanOrEqual(this);
  }
  
	@Override
	protected Result<IBool> equalToValue(ValueResult that) {
		return that.equalityBoolean(this);
	}

	protected <V extends IValue> Result<IBool> equalityBoolean(ElementResult<V> that) {
		V a = that.getValue();
		T b = this.getValue();

		return bool(a.equals(b), ctx);
	}

	protected <V extends IValue> Result<IBool> nonEqualityBoolean(ElementResult<V> that) {
		V a = that.getValue();
		T b = this.getValue();
		return bool((!a.equals(b)), ctx);
	}
	
	@SuppressWarnings("unchecked")
	private int getInt(Result<?> x){
		Result<IValue> key = (Result<IValue>) x;
		if (!key.getStaticType().isInteger()) {
			throw new UnexpectedType(TypeFactory.getInstance().integerType(), key.getStaticType(), ctx.getCurrentAST());
		}
		return ((IInteger)key.getValue()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> slice(Result<?> first, Result<?> second, Result<?> end, int len) {
		
		int firstIndex = 0;
		int secondIndex = 1;
		int endIndex = len;
		
		if(first != null){
			firstIndex = getInt(first);
			if(firstIndex < 0)
				firstIndex += len;
		}
		if(end != null){
			endIndex = getInt(end);
			if(endIndex < 0){
				endIndex += len;
			}
		}
		
		if(second == null){
			secondIndex = firstIndex + ((firstIndex <= endIndex) ? 1 : -1);
		} else {
			secondIndex = getInt(second);
			if(secondIndex < 0) {
				secondIndex += len;
			}
			if(!(first == null && end == null)){
				if(first == null && secondIndex > endIndex)
					firstIndex = len - 1;
				if(end == null && secondIndex < firstIndex)
					endIndex = -1;
			}
		}
		
		if (len == 0 || firstIndex >= len) {
			return (Result<U>) makeSlice(0, 1, 0);
		}
		if (endIndex > len ) {
			return makeSlice(firstIndex, secondIndex, len);
		}
		return (Result<U>) makeSlice(firstIndex, secondIndex, endIndex);
	}

  protected Result<? extends INumber> toReal(IntegerResult from) {
    return makeResult(getTypeFactory().realType(), from.getValue().toReal(getValueFactory().getPrecision()), ctx);
  }

	
}
