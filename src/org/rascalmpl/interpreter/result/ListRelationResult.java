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
import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListRelationWriter;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.Field;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.ArityError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class ListRelationResult extends ListOrRelationResult<IListRelation> {

		public ListRelationResult(Type type, IListRelation rel, IEvaluatorContext ctx) {
			super(type, rel, ctx);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
			return result.addListRelation(this);
		}
		
		@Override
		public <U extends IValue> Result<U> has(Name name) {
			return ResultFactory.bool(getType().hasField(Names.name(name)), ctx);
		}
		
		@Override
		public  <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
			return result.subtractListRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result) {
			return result.intersectListRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
			return that.equalToListRelation(this);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
			return that.nonEqualToListRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that) {
			return that.lessThanListRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that) {
			return that.lessThanOrEqualListRelation(this);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that) {
			return that.greaterThanListRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that) {
			return that.greaterThanOrEqualListRelation(this);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result) {
			return result.compareListRelation(this);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
			return right.composeListRelation(this);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> that) {
			return that.multiplyListRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> join(Result<V> that) {
			return that.joinListRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> in(Result<V> that) {
			return that.inListRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> that) {
			return that.notInListRelation(this);
		}
		
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
			if(getType().getElementType().isVoidType()) throw RuntimeExceptionFactory.noSuchElement(subscripts[0].getValue(), ctx.getCurrentAST(), ctx.getStackTrace());
			
			// TODO: must go to PDB
			int nSubs = subscripts.length;
			if (nSubs >= getType().getArity()) {
				throw new UnsupportedSubscriptArityError(getType(), nSubs, ctx.getCurrentAST());
			}
			int relArity = getType().getArity();
			
			Type subscriptType[] = new Type[nSubs];
			boolean subscriptIsSet[] = new boolean[nSubs];
			Type valueType = TypeFactory.getInstance().valueType();
			/*
			 * Subscripts will value null are interpreted as wildcards (_) that match any value.
			 */
			for (int i = 0; i < nSubs; i++){
				subscriptType[i] = subscripts[i] == null ? valueType : subscripts[i].getType();
			}
			
			boolean yieldList = (relArity - nSubs) == 1;
			Type resFieldType[] = new Type[relArity - nSubs];
			for (int i = 0; i < relArity; i++) {
				Type relFieldType = getType().getFieldType(i);
				if (i < nSubs) {
					if (subscriptType[i].isSetType() && 
							relFieldType.comparable(subscriptType[i].getElementType())){
						subscriptIsSet[i] = true;
					} 
					else if (subscripts[i] == null || relFieldType.comparable(subscriptType[i])){
						subscriptIsSet[i] = false;
					} 
					else {
						throw new UnexpectedTypeError(relFieldType, subscriptType[i], ctx.getCurrentAST());
					}
				} else {
					resFieldType[i - nSubs] = relFieldType;
				}
			}
			Type resultType;
			IListWriter wset = null;
			IListRelationWriter wrel = null;
			
			if (yieldList){
				resultType = getTypeFactory().listType(resFieldType[0]);
				wset = resultType.writer(getValueFactory());
			} else {
				resultType = getTypeFactory().lrelType(resFieldType);
				wrel = resultType.writer(getValueFactory());
			}

			
			for (IValue v : getValue()) {
				ITuple tup = (ITuple)v;
				boolean allEqual = true;
				for(int k = 0; k < nSubs; k++){
					if(subscriptIsSet[k] && ((subscripts[k] == null) ||
							                 ((ISet) subscripts[k].getValue()).contains(tup.get(k)))){
						/* ok */
					} else if (subscripts[k] == null || tup.get(k).isEqual(subscripts[k].getValue())){
						/* ok */
					} else {
						allEqual = false;
					}
				}
				
				if (allEqual) {
					IValue args[] = new IValue[relArity - nSubs];
					for (int i = nSubs; i < relArity; i++) {
						args[i - nSubs] = tup.get(i);
					}
					if(yieldList){
						wset.append(args[0]);
					} else {
						wrel.append(getValueFactory().tuple(args));
					}
				}
			}
			return makeResult(resultType, yieldList ? wset.done() : wrel.done(), ctx);
		}

		////
		
		
		@Override
		protected <U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult) {
			return bool((getValue().contains(elementResult.getValue())), ctx);
		}

		@Override
		protected <U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult) {
			return bool((!getValue().contains(elementResult.getValue())), ctx);
		}
		
		@Override
		public  <U extends IValue> Result<U> transitiveClosure() {
			if (getValue().arity() == 0 || getValue().arity() == 2) {
				return makeResult(type, getValue().closure(), ctx);
			}
			throw new ArityError(2, getValue().arity(), ctx.getCurrentAST());
		}
		

		@Override
		public  <U extends IValue> Result<U> transitiveReflexiveClosure() {
			if (getValue().arity() == 0 || getValue().arity() == 2) {
				return makeResult(type, getValue().closureStar(), ctx);
			}
			throw new ArityError(2, getValue().arity(), ctx.getCurrentAST());
		}
		
		
		@Override
		public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
			Type tupleType = getType().getFieldTypes();	
			
			if (!getType().hasFieldNames()) {
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
			
			if (!getType().hasField(name, store)) {
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
			
			try {
				IListWriter w = getValueFactory().listWriter(tupleType.getFieldType(name));
				for (IValue e : getValue()) {
					w.append(((ITuple) e).get(tupleType.getFieldIndex(name)));
				}
				return makeResult(getTypeFactory().listType(tupleType.getFieldType(name)), w.done(), ctx);
			}
			// TODO: why catch this exception here?
			catch (UndeclaredFieldException e) {
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
		}
		
		
		///

		@Override
		protected <U extends IValue> Result<U> composeListRelation(ListRelationResult that) {
			ListRelationResult left = that;
			ListRelationResult right = this;
			Type leftrelType = left.getType(); 
			Type rightrelType = right.getType();
			int leftArity = leftrelType.getArity();
			int rightArity = rightrelType.getArity();
				
			if (leftArity != 0 && leftArity != 2) {
				throw new ArityError(2, leftArity, ctx.getCurrentAST());
			}
				
			if (rightArity != 0 && rightArity != 2) {
				throw new ArityError(2, rightArity, ctx.getCurrentAST());
			}
			Type resultType = leftrelType.compose(rightrelType);
			return makeResult(resultType, left.getValue().compose(right.getValue()), ctx);
		}

		<U extends IValue, V extends IValue> Result<U> appendTuple(TupleResult tuple) {
			// TODO: check arity 
			Type newType = getTypeFactory().setType(tuple.getType().lub(getType().getElementType()));
			return makeResult(newType, (IRelation) getValue().append(tuple.getValue()), ctx);
		}

		@Override
		protected <U extends IValue> Result<U> joinListRelation(ListRelationResult that) {
			// Note the reverse of arguments, we need "that join this"
			int arity1 = that.getValue().arity();
			int arity2 = this.getValue().arity();
			Type tupleType1 = that.getType().getElementType();
			Type tupleType2 = this.getType().getElementType();
			Type fieldTypes[] = new Type[arity1 + arity2];
			for (int i = 0; i < arity1; i++) {
				fieldTypes[i] = tupleType1.getFieldType(i);
			}
			for (int i = arity1;  i < arity1 + arity2; i++) {
				fieldTypes[i] = tupleType2.getFieldType(i - arity1);
			}
			Type tupleType = getTypeFactory().tupleType(fieldTypes);
			IListWriter writer = getValueFactory().listWriter(tupleType);
			IValue fieldValues[] = new IValue[arity1 + arity2];
			for (IValue tuple1: that.getValue()) {
				for (IValue tuple2: this.getValue()) {
					for (int i = 0; i < arity1; i++) {
						fieldValues[i] = ((ITuple)tuple1).get(i);
					}
					for (int i = arity1; i < arity1 + arity2; i++) {
						fieldValues[i] = ((ITuple)tuple2).get(i - arity1);
					}
					writer.append(getValueFactory().tuple(fieldValues));
				}
			}
			Type resultType = getTypeFactory().lrelTypeFromTuple(tupleType);
			return makeResult(resultType, writer.done(), ctx);
		}

		@Override
		protected <U extends IValue> Result<U> joinSet(SetResult that) {
			// Note the reverse of arguments, we need "that join this"
			int arity2 = this.getValue().arity();
			Type eltType = that.getType().getElementType();
			Type tupleType = this.getType().getElementType();
			Type fieldTypes[] = new Type[1 + arity2];
			fieldTypes[0] = eltType;
			for (int i = 1;  i < 1 + arity2; i++) {
				fieldTypes[i] = tupleType.getFieldType(i);
			}
			Type resultTupleType = getTypeFactory().tupleType(fieldTypes);
			ISetWriter writer = getValueFactory().setWriter(resultTupleType);
			IValue fieldValues[] = new IValue[1 + arity2];
			for (IValue setValue: that.getValue()) {
				for (IValue relValue: this.getValue()) {
					fieldValues[0] = setValue;
					for (int i = 1; i < 1 + arity2; i++) {
						fieldValues[i] = ((ITuple)relValue).get(i);
					}
					writer.insert(getValueFactory().tuple(fieldValues));
				}
			}
			Type resultType = getTypeFactory().relTypeFromTuple(resultTupleType);
			return makeResult(resultType, writer.done(), ctx);
		}
		
		@Override
		public Result<IValue> fieldSelect(int[] selectedFields) {
			if (!getType().getElementType().isVoidType()) {
				for (int i : selectedFields) {
					if (i < 0 || i >= getType().getArity()) {
						throw RuntimeExceptionFactory.indexOutOfBounds(ctx.getValueFactory().integer(i), ctx.getCurrentAST(), ctx.getStackTrace());
					}
				}
			}
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

				if (fieldIndices[i] < 0 || (fieldIndices[i] > baseType.getArity() && !getType().getElementType().isVoidType())) {
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
							.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(fieldIndices[i]),
									ctx.getCurrentAST(), ctx.getStackTrace());
				}
			}
			
			return this.fieldSelect(fieldIndices);
		}
		
}


