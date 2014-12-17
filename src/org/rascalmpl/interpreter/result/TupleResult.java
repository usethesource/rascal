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
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.Field;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UndeclaredField;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscript;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArity;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class TupleResult extends ElementResult<ITuple> {
	
	public TupleResult(Type type, ITuple tuple, IEvaluatorContext ctx) {
		super(type, tuple, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that) {
		return that.addTuple(this);
	}
	
	@Override
	public Result<IValue> fieldSelect(int[] selectedFields) {
		return makeResult(type.select(selectedFields), value.select(selectedFields), ctx);
	}
	
	@Override
	public Result<IValue> fieldSelect(Field[] selectedFields) {
		int nFields = selectedFields.length;
		int fieldIndices[] = new int[nFields];
		Type baseType = this.getType();
		
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

			if (fieldIndices[i] < 0 || fieldIndices[i] > baseType.getArity()) {
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
						.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(fieldIndices[i]),
								ctx.getCurrentAST(), ctx.getStackTrace());
			}
		}
		
		return this.fieldSelect(fieldIndices);
	}
	
	@Override
	public Result<IBool> has(Name name) {
		return ResultFactory.bool(getType().hasField(Names.name(name)), ctx);
	}
	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
			if (!getType().hasFieldNames()) {
				throw new UndeclaredField(name, getType(), ctx.getCurrentAST());
			}
			
			if (!getType().hasField(name, store)) {
				throw new UndeclaredField(name, getType(), ctx.getCurrentAST());
			}
			
			try {
				int index = getType().getFieldIndex(name);
				Type type = getType().getFieldType(index);
				return makeResult(type, getValue().get(index), ctx);
			} 
			catch (UndeclaredFieldException e){
				throw new UndeclaredField(name, getType(), ctx.getCurrentAST());
			}
	}
		
	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store) {
		if (!getType().hasFieldNames()) {
			throw new UndeclaredField(name, getType(), ctx.getCurrentAST());
		}

		try {
			int index = getType().getFieldIndex(name);
			Type type = getType().getFieldType(index);
			if(!type.isSubtypeOf(repl.getType())){
				throw new UnexpectedType(type, repl.getType(), ctx.getCurrentAST());
			}
			return makeResult(getType(), getValue().set(index, repl.getValue()), ctx);
		} catch (UndeclaredFieldException e) {
			throw new UndeclaredField(name, getType(), ctx.getCurrentAST());
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
		if (subscripts.length > 1) {
			throw new UnsupportedSubscriptArity(getType(), subscripts.length, ctx.getCurrentAST());
		}
		Result<IValue> subsBase = (Result<IValue>)subscripts[0];
		if(subsBase == null)
			/*
			 * Wild card not allowed as tuple subscript
			 */
			throw new UnsupportedSubscript(type, null, ctx.getCurrentAST());
		if (!subsBase.getType().isInteger()){
			throw new UnsupportedSubscript(getTypeFactory().integerType(), subsBase.getType(), ctx.getCurrentAST());
		}
		IInteger index = (IInteger)subsBase.getValue();
		int idx = index.intValue();
		if(idx < 0){
			idx = idx + getValue().arity();
		}
		if ( (idx >= getValue().arity()) || (idx < 0)) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		Type elementType = getType().getFieldType(idx);
		IValue element = getValue().get(idx);
		return makeResult(elementType, element, ctx);
	}
	
	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToTuple(this);
	}

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToTuple(this);
	}

	@Override
	public <V extends IValue> Result<IBool> lessThan(Result<V> result) {
		return result.lessThanTuple(this);
	}
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualTuple(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThan(Result<V> result) {
		return result.greaterThanTuple(this);
	}
	
	@Override
	public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualTuple(this);
	}
	
	///

	@Override
	protected <U extends IValue> Result<U> addTuple(TupleResult that) {
		// Note reversed args
		TupleResult left = that;
		TupleResult right = this;
		Type leftType = left.getType();
		Type rightType = right.getType();
		
		int leftArity = leftType.getArity();
		int rightArity = rightType.getArity();
		int newArity = leftArity + rightArity;
		
		Type fieldTypes[] = new Type[newArity];
		String fieldNames[] = new String[newArity];
		IValue fieldValues[] = new IValue[newArity];
		
		boolean consistentLabels = true;
		for(int i = 0; i < leftArity; i++){
			fieldTypes[i] = leftType.getFieldType(i);
			fieldNames[i] = leftType.getFieldName(i);
			fieldValues[i] = left.getValue().get(i);
			consistentLabels &= fieldNames[i] != null;
		}
		
		for(int i = 0; i < rightArity; i++){
			fieldTypes[leftArity + i] = rightType.getFieldType(i);
			fieldNames[leftArity + i] = rightType.getFieldName(i);
			fieldValues[leftArity + i] = right.getValue().get(i);
			consistentLabels &= fieldNames[i] != null;
			if (consistentLabels) {
				for (int j = 0; j < leftArity; j++) {
					if (fieldNames[j].equals(fieldNames[i])) {
						// duplicate field name, so degenerate to unlabeled tuple
						consistentLabels = false;
					}
				}
			}
		}
		
		Type newTupleType;
		if (consistentLabels) {
			newTupleType = getTypeFactory().tupleType(fieldTypes, fieldNames);
		}
		else {
			newTupleType = getTypeFactory().tupleType(fieldTypes);
		}
		return makeResult(newTupleType, getValueFactory().tuple(fieldValues), ctx);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult that) {
		return that.insertTuple(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractRelation(RelationResult that) {
		if(that.getType().getElementType().getArity() == this.getType().getArity())
			return that.removeElement(this);
		return super.subtractRelation(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> addListRelation(ListRelationResult that) {
		return that.appendTuple(this);
	}
		
	@Override
	protected <U extends IValue> Result<U> subtractListRelation(ListRelationResult that) {
		if(that.getType().getElementType().getArity() == this.getType().getArity())
			return that.removeElement(this);
		return super.subtractListRelation(that);
	}

	
	@Override
	protected Result<IBool> equalToTuple(TupleResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected Result<IBool> nonEqualToTuple(TupleResult that) {
		return that.nonEqualityBoolean(this);
	}

	@Override
	protected Result<IBool> greaterThanOrEqualTuple(TupleResult that) {
	  return that.lessThanOrEqualTuple(this);
	}
	
	@Override
	protected Result<IBool> greaterThanTuple(TupleResult that) {
	  LessThanOrEqualResult loe = that.lessThanOrEqualTuple(this);
    return loe.isLess();
	}
	
	@Override
	protected Result<IBool> lessThanTuple(TupleResult that) {
	  LessThanOrEqualResult loe = lessThanOrEqualTuple(that);
    return loe.isLess();
	}
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualTuple(TupleResult that) {
    ITuple left = that.getValue();
    int leftArity = left.arity();
    ITuple right = getValue();
    int rightArity = right.arity();
    
    for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
       IValue leftArg = left.get(i);
       IValue rightArg = right.get(i);
       LessThanOrEqualResult loe = makeResult(leftArg.getType(), leftArg, ctx).lessThanOrEqual(makeResult(rightArg.getType(), rightArg,ctx));
       
       if (loe.getLess()) {
         return loe;
       }
       
       if (!loe.getEqual()) { 
         return new LessThanOrEqualResult(false, false, ctx);
       }
    }
    
    return new LessThanOrEqualResult(leftArity < rightArity, leftArity == rightArity, ctx);
	}
}
