package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

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
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.staticErrors.ArityError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class RelationResult extends SetOrRelationResult<IRelation> {

		public RelationResult(Type type, IRelation rel, IEvaluatorContext ctx) {
			super(type, rel, ctx);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, IEvaluatorContext ctx) {
			return result.addRelation(this, ctx);
		}
		
		@Override
		public  <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, IEvaluatorContext ctx) {
			return result.subtractRelation(this, ctx);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result, IEvaluatorContext ctx) {
			return result.intersectRelation(this, ctx);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
			return that.equalToRelation(this, ctx);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, IEvaluatorContext ctx) {
			return that.nonEqualToRelation(this, ctx);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, IEvaluatorContext ctx) {
			return that.lessThanRelation(this, ctx);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, IEvaluatorContext ctx) {
			return that.lessThanOrEqualRelation(this, ctx);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, IEvaluatorContext ctx) {
			return that.greaterThanRelation(this, ctx);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, IEvaluatorContext ctx) {
			return that.greaterThanOrEqualRelation(this, ctx);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, IEvaluatorContext ctx) {
			return result.compareRelation(this, ctx);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right, IEvaluatorContext ctx) {
			return right.composeRelation(this, ctx);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> that, IEvaluatorContext ctx) {
			return that.multiplyRelation(this, ctx);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> join(Result<V> that, IEvaluatorContext ctx) {
			return that.joinRelation(this, ctx);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> in(Result<V> that, IEvaluatorContext ctx) {
			return that.inRelation(this, ctx);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> that, IEvaluatorContext ctx) {
			return that.notInRelation(this, ctx);
		}
		
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, IEvaluatorContext ctx) {
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
			
			boolean yieldSet = (relArity - nSubs) == 1;
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
			ISetWriter wset = null;
			IRelationWriter wrel = null;
			
			if (yieldSet){
				resultType = getTypeFactory().setType(resFieldType[0]);
				wset = resultType.writer(getValueFactory());
			} else {
				resultType = getTypeFactory().relType(resFieldType);
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
					if(yieldSet){
						wset.insert(args[0]);
					} else {
						wrel.insert(getValueFactory().tuple(args));
					}
				}
			}
			return makeResult(resultType, yieldSet ? wset.done() : wrel.done(), ctx);
		}

		////
		
		
		@Override
		protected <U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult, IEvaluatorContext ctx) {
			return bool(getValue().contains(elementResult.getValue()));
		}

		@Override
		protected <U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult, IEvaluatorContext ctx) {
			return bool(!getValue().contains(elementResult.getValue()));
		}
		
		@Override
		public  <U extends IValue> Result<U> transitiveClosure(IEvaluatorContext ctx) {
			if (getValue().arity() == 0 || getValue().arity() == 2) {
				return makeResult(type, getValue().closure(), ctx);
			}
			throw new ArityError(2, getValue().arity(), ctx.getCurrentAST());
		}
		

		@Override
		public  <U extends IValue> Result<U> transitiveReflexiveClosure(IEvaluatorContext ctx) {
			if (getValue().arity() == 0 || getValue().arity() == 2) {
				return makeResult(type, getValue().closureStar(), ctx);
			}
			throw new ArityError(2, getValue().arity(), ctx.getCurrentAST());
		}
		
		
		@Override
		public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, IEvaluatorContext ctx) {
			Type tupleType = getType().getFieldTypes();	
			
			if (!getType().hasFieldNames()) {
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
			
			if (getType().hasField(name, store)) {
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
			
			try {
				ISetWriter w = getValueFactory().setWriter(tupleType.getFieldType(name));
				for (IValue e : getValue()) {
					w.insert(((ITuple) e).get(tupleType.getFieldIndex(name)));
				}
				return makeResult(getTypeFactory().setType(tupleType.getFieldType(name)), w.done(), ctx);
			}
			// TODO: why catch this exception here?
			catch (UndeclaredFieldException e) {
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
		}
		
		
		///

		@Override
		protected <U extends IValue> Result<U> composeRelation(RelationResult that, IEvaluatorContext ctx) {
			RelationResult left = that;
			RelationResult right = this;
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

		<U extends IValue, V extends IValue> Result<U> insertTuple(TupleResult tuple, IEvaluatorContext ctx) {
			// TODO: check arity 
			Type newType = getTypeFactory().setType(tuple.getType().lub(getType().getElementType()));
			return makeResult(newType, (IRelation) getValue().insert(tuple.getValue()), ctx);
		}

		@Override
		protected <U extends IValue> Result<U> joinRelation(RelationResult that, IEvaluatorContext ctx) {
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
			ISetWriter writer = getValueFactory().setWriter(tupleType);
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
		protected <U extends IValue> Result<U> joinSet(SetResult that, IEvaluatorContext ctx) {
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
		
		
		
}


