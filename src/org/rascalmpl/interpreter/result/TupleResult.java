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
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.Field;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptError;
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
					throw new UndeclaredFieldError(fieldName, baseType,
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
	public <U extends IValue> Result<U> has(Name name) {
		return ResultFactory.bool(getType().hasField(Names.name(name)), ctx);
	}
	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
			if (!getType().hasFieldNames()) {
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
			
			if (!getType().hasField(name, store)) {
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
			
			try {
				int index = getType().getFieldIndex(name);
				Type type = getType().getFieldType(index);
				return makeResult(type, getValue().get(index), ctx);
			} 
			catch (UndeclaredFieldException e){
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
	}
		
	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store) {
		if (!getType().hasFieldNames()) {
			throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
		}

		try {
			int index = getType().getFieldIndex(name);
			Type type = getType().getFieldType(index);
			if(!type.isSubtypeOf(repl.getType())){
				throw new UnexpectedTypeError(type, repl.getType(), ctx.getCurrentAST());
			}
			return makeResult(getType(), getValue().set(index, repl.getValue()), ctx);
		} catch (UndeclaredFieldException e) {
			throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
		if (subscripts.length > 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ctx.getCurrentAST());
		}
		Result<IValue> subsBase = (Result<IValue>)subscripts[0];
		if(subsBase == null)
			/*
			 * Wild card not allowed as tuple subscript
			 */
			throw new UnsupportedSubscriptError(type, null, ctx.getCurrentAST());
		if (!subsBase.getType().isIntegerType()){
			throw new UnsupportedSubscriptError(getTypeFactory().integerType(), subsBase.getType(), ctx.getCurrentAST());
		}
		IInteger index = (IInteger)subsBase.getValue();
		if (index.intValue() >= getValue().arity()) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		Type elementType = getType().getFieldType(index.intValue());
		IValue element = getValue().get(index.intValue());
		return makeResult(elementType, element, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result) {
		return result.compareTuple(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToTuple(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToTuple(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result) {
		return result.lessThanTuple(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualTuple(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result) {
		return result.greaterThanTuple(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result) {
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
			consistentLabels = fieldNames[i] != null;
		}
		
		for(int i = 0; i < rightArity; i++){
			fieldTypes[leftArity + i] = rightType.getFieldType(i);
			fieldNames[leftArity + i] = rightType.getFieldName(i);
			fieldValues[leftArity + i] = right.getValue().get(i);
			consistentLabels = fieldNames[i] != null;
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
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareTuple(TupleResult that) {
		// Note reversed args
		ITuple left = that.getValue();
		ITuple right = this.getValue();
		int compare = Integer.valueOf(left.arity()).compareTo(Integer.valueOf(right.arity()));
		if (compare != 0) {
			return makeIntegerResult(compare);
		}
		for (int i = 0; i < left.arity(); i++) {
			compare = compareIValues(left.get(i), right.get(i), ctx);
			if (compare != 0) {
				return makeIntegerResult(compare);
			}
		}
		return makeIntegerResult(0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanTuple(TupleResult that) {
		// note reversed args: we need that < this
		return bool((that.comparisonInts(this) < 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualTuple(TupleResult that) {
		// note reversed args: we need that <= this
		return bool((that.comparisonInts(this) <= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanTuple(TupleResult that) {
		// note reversed args: we need that > this
		return bool((that.comparisonInts(this) > 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualTuple(TupleResult that) {
		// note reversed args: we need that >= this
		return bool((that.comparisonInts(this) >= 0), ctx);
	}
}
