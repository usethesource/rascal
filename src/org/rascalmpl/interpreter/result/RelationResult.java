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

import org.rascalmpl.ast.Field;
import org.rascalmpl.ast.Name;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.Arity;
import org.rascalmpl.interpreter.staticErrors.UndeclaredField;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArity;
import org.rascalmpl.interpreter.utils.Names;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IRelation;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.UndeclaredFieldException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public class RelationResult extends SetOrRelationResult<ISet> {

		public RelationResult(Type type, ISet rel, IEvaluatorContext ctx) {
			super(type, rel, ctx);
//			if (!rel.isRelation()) 
//				throw new RuntimeException();
		}
		
		@Override
		public Result<IBool> isKeyDefined(Result<?>[] subscripts) {
			int len = getStaticType().getElementType().getArity();
			
			if (subscripts.length >= len){
			    throw new UnsupportedSubscriptArity(getStaticType(), len, ctx.getCurrentAST());
			}
			
			return makeResult(getTypeFactory().boolType(), getValueFactory().bool(true), ctx);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
			return result.addRelation(this);
		}
		
		@Override
		public Result<IBool> has(Name name) {
			return ResultFactory.bool(getStaticType().hasField(Names.name(name)), ctx);
		}
		
		@Override
		public  <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
			return result.subtractRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result) {
			return result.intersectRelation(this);
		}
		
		@Override
		public <V extends IValue> Result<IBool> equals(Result<V> that) {
			return that.equalToRelation(this);
		}

		@Override
		public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
			return that.nonEqualToRelation(this);
		}
		
		@Override
		public <V extends IValue> Result<IBool> lessThan(Result<V> that) {
			return that.lessThanRelation(this);
		}
		
		@Override
		public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> that) {
			return that.lessThanOrEqualRelation(this);
		}

		@Override
		public <V extends IValue> Result<IBool> greaterThan(Result<V> that) {
			return that.greaterThanRelation(this);
		}
		
		@Override
		public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> that) {
			return that.greaterThanOrEqualRelation(this);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
			return right.composeRelation(this);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> that) {
			return that.multiplyRelation(this);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> join(Result<V> that) {
			return that.joinRelation(this);
		}
		
		@Override
		public <V extends IValue> Result<IBool> in(Result<V> that) {
			return that.inRelation(this);
		}
		
		@Override
		public <V extends IValue> Result<IBool> notIn(Result<V> that) {
			return that.notInRelation(this);
		}
		
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
			if(getStaticType().getElementType().isBottom()) {
//			    throw RuntimeExceptionFactory.noSuchElement(subscripts[0].getValue(), ctx.getCurrentAST(), ctx.getStackTrace());
			    return ResultFactory.makeResult(getStaticType(), ctx.getValueFactory().set(), ctx);
			}
			
			// TODO: must go to vallang!
			int nSubs = subscripts.length;
			if (nSubs >= getStaticType().getArity()) {
				throw new UnsupportedSubscriptArity(getStaticType(), nSubs, ctx.getCurrentAST());
			}
			int relArity = getStaticType().getArity();
			
			Type subscriptType[] = new Type[nSubs];
			boolean subscriptIsSet[] = new boolean[nSubs];
			Type valueType = TypeFactory.getInstance().valueType();
			/*
			 * Subscripts will value null are interpreted as wildcards (_) that match any value.
			 */
			for (int i = 0; i < nSubs; i++){
				subscriptType[i] = subscripts[i] == null ? valueType : subscripts[i].getStaticType();
			}
			
			boolean yieldSet = (relArity - nSubs) == 1;
			Type resFieldType[] = new Type[relArity - nSubs];
			for (int i = 0; i < relArity; i++) {
				Type relFieldType = getStaticType().getFieldType(i);
				if (i < nSubs) {
					if (subscriptType[i].isSet() && 
							relFieldType.comparable(subscriptType[i].getElementType())){
						subscriptIsSet[i] = true;
					} 
					else if (subscripts[i] == null || relFieldType.comparable(subscriptType[i])){
						subscriptIsSet[i] = false;
					} 
					else {
						throw new UnexpectedType(relFieldType, subscriptType[i], ctx.getCurrentAST());
					}
				} else {
					resFieldType[i - nSubs] = relFieldType;
				}
			}
			Type resultType;
			if (yieldSet){
				resultType = getTypeFactory().setType(resFieldType[0]);
			}
			else {
				resultType = getTypeFactory().relType(resFieldType);
			}
			
			
			if (nSubs == 1) {
			    // special case the simple subscription to directly target vallang
			    IRelation<ISet> relation = getValue().asRelation();
			    
			    ISet result;
			    if (subscripts[0] == null) {
			        // edge case, we want everything, so an project
			        int[] restSubScripts = new int[relArity - 1];
			        for (int i = 1; i < relArity; i++) {
			            restSubScripts[i - 1] = i;
			        }
			        result = relation.project(restSubScripts);
			    }
			    else if (subscriptIsSet[0]) {
			        ISet subScriptSet = ((ISet) subscripts[0].getValue());
			        if (subScriptSet.size() * 2 < relation.asContainer().size() && relArity == 2) {
			            // if it is a binary relation and the subscript set is not that big, it's quicker to just do a few seperate indexes
                        result = this.getValueFactory().set();
                        for (IValue v : subScriptSet) {
                            result = result.union(relation.index(v));
                        }
			        }
			        else {
			            // we have a big subscript, so it's quicker to do a single loop
			            ISetWriter resultWriter = this.getValueFactory().setWriter();
                        int[] restSubScripts = new int[relArity - 1];
                        for (int i = 1; i < relArity; i++) {
                            restSubScripts[i - 1] = i;
                        }
			            for (IValue v : relation) {
			                ITuple tup = (ITuple)v;
			                if (subScriptSet.contains(tup.get(0))) {
			                   resultWriter.append(tup.select(restSubScripts));
			                }
			            }
			            result = resultWriter.done();
			        }
			    }
			    else {
			        result = relation.index(subscripts[0].getValue());
			        
			    }
			    return makeResult(resultType, result, ctx);
			}
			
			ISetWriter result = this.getValueFactory().setWriter();
			for (IValue v : getValue()) {
				ITuple tup = (ITuple)v;
				boolean allEqual = true;
				for(int k = 0; k < nSubs && allEqual; k++){
					if(subscriptIsSet[k] && ((subscripts[k] == null) ||
							                 ((ISet) subscripts[k].getValue()).contains(tup.get(k)))){
						/* ok */
					} else if (subscripts[k] == null || tup.get(k).equals(subscripts[k].getValue())){
						/* ok */
					} else {
						allEqual = false;
					}
				}
				
				if (allEqual) {
				    if (yieldSet) {
						result.insert(tup.get(nSubs));
				    }
				    else {
				        IValue args[] = new IValue[relArity - nSubs];
				        for (int i = nSubs; i < relArity; i++) {
				            args[i - nSubs] = tup.get(i);
				        }
				        result.insert(getValueFactory().tuple(args));
				    }
				}
			}
			return makeResult(resultType, result.done(), ctx);
		}

		////
		
		
		@Override
		protected <V extends IValue> Result<IBool> elementOf(ElementResult<V> elementResult) {
			return bool((getValue().contains(elementResult.getValue())), ctx);
		}

		@Override
		protected <V extends IValue> Result<IBool> notElementOf(ElementResult<V> elementResult) {
			return bool((!getValue().contains(elementResult.getValue())), ctx);
		}
		
		@Override
		public  <U extends IValue> Result<U> transitiveClosure() {
			if (getValue().asRelation().arity() == 0 || getValue().asRelation().arity() == 2) {
				return makeResult(type, getValue().asRelation().closure(), ctx);
			}
			throw new Arity(2, getValue().asRelation().arity(), ctx.getCurrentAST());
		}
		

		@Override
		public  <U extends IValue> Result<U> transitiveReflexiveClosure() {
			if (getValue().asRelation().arity() == 0 || getValue().asRelation().arity() == 2) {
				return makeResult(type, getValue().asRelation().closureStar(), ctx);
			}
			throw new Arity(2, getValue().asRelation().arity(), ctx.getCurrentAST());
		}
		
		
		@Override
		public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
			Type tupleType = getStaticType().getFieldTypes();	
			
			if (!getStaticType().hasFieldNames()) {
				throw new UndeclaredField(name, getStaticType(), ctx.getCurrentAST());
			}
			
			if (!getStaticType().hasField(name, store)) {
				throw new UndeclaredField(name, getStaticType(), ctx.getCurrentAST());
			}
			
			try {
				ISetWriter w = getValueFactory().setWriter();
				for (IValue e : getValue()) {
					w.insert(((ITuple) e).get(tupleType.getFieldIndex(name)));
				}
				return makeResult(getTypeFactory().setType(tupleType.getFieldType(name)), w.done(), ctx);
			}
			// TODO: why catch this exception here?
			catch (UndeclaredFieldException e) {
				throw new UndeclaredField(name, getStaticType(), ctx.getCurrentAST());
			}
		}
		
		
		///

		@Override
		protected <U extends IValue> Result<U> composeRelation(RelationResult that) {
			RelationResult left = that;
			RelationResult right = this;
			Type leftrelType = left.getStaticType(); 
			Type rightrelType = right.getStaticType();
			int leftArity = leftrelType.getArity();
			int rightArity = rightrelType.getArity();
				
			if (leftArity != 0 && leftArity != 2) {
				throw new Arity(2, leftArity, ctx.getCurrentAST());
			}
				
			if (rightArity != 0 && rightArity != 2) {
				throw new Arity(2, rightArity, ctx.getCurrentAST());
			}
			Type resultType = leftrelType.compose(rightrelType);
			return makeResult(resultType, left.getValue().asRelation().compose(right.getValue().asRelation()), ctx);
		}

		<U extends IValue, V extends IValue> Result<U> insertTuple(TupleResult tuple) {
			// TODO: check arity 
			Type newType = getTypeFactory().setType(tuple.getStaticType().lub(getStaticType().getElementType()));
			return makeResult(newType, /*(ISet)*/ getValue().insert(tuple.getValue()), ctx); // do not see a reason for the unsafe cast
		}

		@Override
		protected <U extends IValue> Result<U> joinRelation(RelationResult that) {
			// Note the reverse of arguments, we need "that join this"
			int arity1 = that.getValue().asRelation().arity();
			int arity2 = this.getValue().asRelation().arity();
			Type tupleType1 = that.getStaticType().getElementType();
			Type tupleType2 = this.getStaticType().getElementType();
			Type fieldTypes[] = new Type[arity1 + arity2];
			for (int i = 0; i < arity1; i++) {
				fieldTypes[i] = tupleType1.getFieldType(i);
			}
			for (int i = arity1;  i < arity1 + arity2; i++) {
				fieldTypes[i] = tupleType2.getFieldType(i - arity1);
			}
			Type tupleType = getTypeFactory().tupleType(fieldTypes);
			ISetWriter writer = getValueFactory().setWriter();
			IValue fieldValues[] = new IValue[arity1 + arity2];
			for (IValue tuple1: that.getValue()) {
				for (IValue tuple2: this.getValue()) {
					for (int i = 0; i < arity1; i++) {
						fieldValues[i] = ((ITuple)tuple1).get(i);
					}
					for (int i = arity1; i < arity1 + arity2; i++) {
						fieldValues[i] = ((ITuple)tuple2).get(i - arity1);
					}
					writer.insert(getValueFactory().tuple(fieldValues));
				}
			}
			Type resultType = getTypeFactory().relTypeFromTuple(tupleType);
			return makeResult(resultType, writer.done(), ctx);
		}

		@Override
		protected <U extends IValue> Result<U> joinSet(SetResult that) {
			// Note the reverse of arguments, we need "that join this"
			int arity2 = this.getValue().asRelation().arity();
			Type eltType = that.getStaticType().getElementType();
			Type tupleType = this.getStaticType().getElementType();
			Type fieldTypes[] = new Type[1 + arity2];
			fieldTypes[0] = eltType;
			for (int i = 1;  i < 1 + arity2; i++) {
				fieldTypes[i] = tupleType.getFieldType(i);
			}
			Type resultTupleType = getTypeFactory().tupleType(fieldTypes);
			ISetWriter writer = getValueFactory().setWriter();
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
			if (!getStaticType().getElementType().isBottom()) {
				for (int i : selectedFields) {
					if (i < 0 || i >= getStaticType().getArity()) {
						throw RuntimeExceptionFactory.indexOutOfBounds(ctx.getValueFactory().integer(i), ctx.getCurrentAST(), ctx.getStackTrace());
					}
				}
			}
		   return makeResult(type.select(selectedFields), value.asRelation().project(selectedFields), ctx);
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

				if (fieldIndices[i] < 0 || (fieldIndices[i] > baseType.getArity() && !getStaticType().getElementType().isBottom())) {
					throw org.rascalmpl.exceptions.RuntimeExceptionFactory
							.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(fieldIndices[i]),
									ctx.getCurrentAST(), ctx.getStackTrace());
				}
			}
			
			return this.fieldSelect(fieldIndices);
		}
		
}


