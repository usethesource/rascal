package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.staticErrors.ArityError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;

import org.meta_environment.rascal.ast.AbstractAST;

public class RelationResult extends CollectionResult<IRelation> {

		public RelationResult(Type type, IRelation rel) {
			super(type, rel);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, AbstractAST ast) {
			return result.addRelation(this, ast);
		}
		
		@Override
		public  <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, AbstractAST ast) {
			return result.subtractRelation(this, ast);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result, AbstractAST ast) {
			return result.intersectRelation(this, ast);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
			return that.equalToRelation(this, ast);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
			return that.nonEqualToRelation(this, ast);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, AbstractAST ast) {
			return that.lessThanRelation(this, ast);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, AbstractAST ast) {
			return that.lessThanOrEqualRelation(this, ast);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, AbstractAST ast) {
			return that.greaterThanRelation(this, ast);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, AbstractAST ast) {
			return that.greaterThanOrEqualRelation(this, ast);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, AbstractAST ast) {
			return result.compareRelation(this, ast);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right, AbstractAST ast) {
			return right.composeRelation(this, ast);
		}

		@Override
		public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> that, AbstractAST ast) {
			return that.multiplyRelation(this, ast);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> in(Result<V> that, AbstractAST ast) {
			return that.inRelation(this, ast);
		}
		
		@Override
		public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> that, AbstractAST ast) {
			return that.notInRelation(this, ast);
		}
		
		
		public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, AbstractAST ast) {
			// TODO: must go to PDB
			int nSubs = subscripts.length;
			if (nSubs >= getType().getArity()) {
				throw new UnsupportedSubscriptArityError(getType(), nSubs, ast);
			}
			int relArity = getType().getArity();
			
			Type subscriptType[] = new Type[nSubs];
			boolean subscriptIsSet[] = new boolean[nSubs];
			
			for (int i = 0; i < nSubs; i++){
				subscriptType[i] = subscripts[i].getType();
			}
			
			boolean yieldSet = (relArity - nSubs) == 1;
			Type resFieldType[] = new Type[relArity - nSubs];
			for (int i = 0; i < relArity; i++) {
				Type relFieldType = getType().getFieldType(i);
				if (i < nSubs) {
					if (subscriptType[i].isSetType() && 
					    subscriptType[i].getElementType().isSubtypeOf(relFieldType)){
						subscriptIsSet[i] = true;
					} 
					else if (subscriptType[i].isSubtypeOf(relFieldType)){
						subscriptIsSet[i] = false;
					} 
					else {
						throw new UnexpectedTypeError(relFieldType, subscriptType[i], ast);
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
					if(subscriptIsSet[k] && ((ISet) subscripts[k].getValue()).contains(tup.get(k))){
						/* ok */
					} else if (tup.get(k).isEqual(subscripts[k].getValue())){
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
			return makeResult(resultType, yieldSet ? wset.done() : wrel.done());
		}

		////
		
		
		protected <U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult, AbstractAST ast) {
			return bool(getValue().contains(elementResult.getValue()));
		}

		protected <U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult, AbstractAST ast) {
			return bool(!getValue().contains(elementResult.getValue()));
		}
		
		@Override
		public  <U extends IValue> Result<U> transitiveClosure(AbstractAST ast) {
			if (getValue().arity() == 0 || getValue().arity() == 2) {
				return makeResult(type, getValue().closure());
			}
			throw new ArityError(2, getValue().arity(), ast);
		}
		

		@Override
		public  <U extends IValue> Result<U> transitiveReflexiveClosure(AbstractAST ast) {
			if (getValue().arity() == 0 || getValue().arity() == 2) {
				return makeResult(type, getValue().closureStar());
			}
			throw new ArityError(2, getValue().arity(), ast);
		}
		
		
		@Override
		public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, AbstractAST ast) {
			Type tupleType = getType().getFieldTypes();			
			try {
				ISetWriter w = getValueFactory().setWriter(tupleType.getFieldType(name));
				for (IValue e : getValue()) {
					w.insert(((ITuple) e).get(tupleType.getFieldIndex(name)));
				}
				return makeResult(getTypeFactory().setType(tupleType.getFieldType(name)), w.done());
			}
			// TODO: why catch this exception here?
			catch (UndeclaredFieldException e) {
				throw new UndeclaredFieldError(name, getType(), ast);
			}
		}
		
		
		///
		
		@Override
		protected <U extends IValue> Result<U> addRelation(RelationResult r, AbstractAST ast) {
			//checkCompatibleArity(r);
			return makeResult(type.lub(r.type), (IRelation)getValue().union(r.getValue()));
		}
		
		@Override
		protected <U extends IValue> Result<U> addSet(SetResult s, AbstractAST ast) {
			return makeResult(type.lub(s.type), getValue().union(s.getValue()));
		}
		
		@Override 
		protected <U extends IValue> Result<U> subtractRelation(RelationResult r, AbstractAST ast) {			
			//checkCompatibleArity(r);
			return makeResult(type.lub(r.type), (IRelation) r.getValue().subtract(getValue()));
		}

		@Override 
		protected <U extends IValue> Result<U> subtractSet(SetResult r, AbstractAST ast) {			
			//checkCompatibleArity(r);
			return makeResult(getType().lub(r.getType()), r.getValue().subtract(getValue()));
		}

		
		@Override 
		protected <U extends IValue> Result<U> intersectSet(SetResult s, AbstractAST ast) {
			return makeResult(type.lub(s.type), getValue().intersect(s.getValue()));
		}
		
		@Override 
		protected <U extends IValue> Result<U> intersectRelation(RelationResult s, AbstractAST ast) {
			return makeResult(type.lub(s.type), getValue().intersect(s.getValue()));
		}
		
		
		@Override
		protected <U extends IValue> Result<U> equalToRelation(RelationResult that, AbstractAST ast) {
			return that.equalityBoolean(this);
		}
		
		@Override
		protected <U extends IValue> Result<U> equalToSet(SetResult that, AbstractAST ast) {
			return that.equalityBoolean(this);
		}

		@Override
		protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that, AbstractAST ast) {
			return that.nonEqualityBoolean(this);
		}
		
		@Override
		protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, AbstractAST ast) {
			return that.nonEqualityBoolean(this);
		}

		

		@Override
		protected <U extends IValue> Result<U> lessThanSet(SetResult that, AbstractAST ast) {
			// note reversed args: we need that < this
			return bool(that.comparisonInts(this, ast) < 0);
		}
		
		@Override
		protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that, AbstractAST ast) {
			// note reversed args: we need that <= this
			return bool(that.comparisonInts(this, ast) <= 0);
		}

		@Override
		protected <U extends IValue> Result<U> greaterThanSet(SetResult that, AbstractAST ast) {
			// note reversed args: we need that > this
			return bool(that.comparisonInts(this, ast) > 0);
		}
		
		@Override
		protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that, AbstractAST ast) {
			// note reversed args: we need that >= this
			return bool(that.comparisonInts(this, ast) >= 0);
		}
		
		@Override
		protected <U extends IValue> Result<U> lessThanRelation(RelationResult that, AbstractAST ast) {
			// note reversed args: we need that < this
			return bool(that.comparisonInts(this, ast) < 0);
		}
		
		@Override
		protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that, AbstractAST ast) {
			// note reversed args: we need that <= this
			return bool(that.comparisonInts(this, ast) <= 0);
		}

		@Override
		protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that, AbstractAST ast) {
			// note reversed args: we need that > this
			return bool(that.comparisonInts(this, ast) > 0);
		}
		
		@Override
		protected <U extends IValue> Result<U> greaterThanOrEqualRelation(RelationResult that, AbstractAST ast) {
			// note reversed args: we need that >= this
			return bool(that.comparisonInts(this, ast) >= 0);
		}
		
		@Override
		protected <U extends IValue> Result<U> composeRelation(RelationResult that, AbstractAST ast) {
			RelationResult left = that;
			RelationResult right = this;
			Type leftrelType = left.getType(); 
			Type rightrelType = right.getType();
			int leftArity = leftrelType.getArity();
			int rightArity = rightrelType.getArity();
				
			if (leftArity != 0 && leftArity != 2) {
				throw new ArityError(2, leftArity, null);
			}
				
			if (rightArity != 0 && rightArity != 2) {
				throw new ArityError(2, rightArity, null);
			}
			Type resultType = leftrelType.compose(rightrelType);
			return makeResult(resultType, left.getValue().compose(right.getValue()));
		}


		@Override
		protected <U extends IValue> Result<U> multiplyRelation(RelationResult that, AbstractAST ast) {
			Type tupleType = getTypeFactory().tupleType(that.type.getElementType(), type.getElementType());
			// Note the reverse in .product
			return makeResult(getTypeFactory().relTypeFromTuple(tupleType), that.getValue().product(getValue()));
		}
		
		@Override
		protected <U extends IValue> Result<U> multiplySet(SetResult that, AbstractAST ast) {
			Type tupleType = getTypeFactory().tupleType(that.type.getElementType(), type.getElementType());
			// Note the reverse in .product
			return makeResult(getTypeFactory().relTypeFromTuple(tupleType), that.getValue().product(getValue()));
		}
		
			
		@Override
		protected <U extends IValue> Result<U> compareRelation(RelationResult that, AbstractAST ast) {
			// Note reverse args
			return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ast));
		}
		
		@Override
		protected <U extends IValue> Result<U> compareSet(SetResult that, AbstractAST ast) {
			// Note reverse args
			return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ast));
		}
		
		private void checkCompatibleArity(RelationResult that, AbstractAST ast) {
			checkArity(getType().getArity(), that.getType().getArity(), ast);
		}
		
		private static void checkArity(int expected, int given, AbstractAST ast) {
			if (expected != given) {
				throw new ArityError(expected, given, ast);
			}
		}

		<U extends IValue, V extends IValue> Result<U> insertTuple(TupleResult tuple, AbstractAST ast) {
			// TODO: check arity 
			Type newType = getTypeFactory().relTypeFromTuple(tuple.getType().lub(getType().getElementType()));
			return makeResult(newType, (IRelation) getValue().insert(tuple.getValue()));
		}

		
		
		
		
}


