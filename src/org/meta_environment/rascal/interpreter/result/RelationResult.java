package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.exceptions.TypeErrorException;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class RelationResult extends CollectionResult<IRelation> {

		public RelationResult(Type type, IRelation rel) {
			super(type, rel);
		}

		@Override
		public <U extends IValue, V extends IValue> AbstractResult<U> add(AbstractResult<V> result) {
			return result.addRelation(this);
		}
		
		@Override
		public  <U extends IValue, V extends IValue> AbstractResult<U> subtract(AbstractResult<V> result) {
			return result.subtractRelation(this);
		}
		
		
		////
		
		@Override
		public  <U extends IValue> AbstractResult<U> transitiveClosure() {
			checkArity(2, getValue().arity());
			return makeResult(type, getValue().closure());
		}
		

		@Override
		public  <U extends IValue> AbstractResult<U> transitiveReflexiveClosure() {
			checkArity(2, getValue().arity());
			return makeResult(type, getValue().closureStar());
		}
		
		
		@Override
		protected <U extends IValue> AbstractResult<U> addRelation(RelationResult r) {
			checkCompatibleArity(r);
			return makeResult(type.lub(r.type), (IRelation)getValue().union(r.getValue()));
		}
		

		@Override 
		protected <U extends IValue> AbstractResult<U> subtractRelation(RelationResult r) {			
			checkCompatibleArity(r);
			return makeResult(type.lub(r.type), (IRelation) r.getValue().union(getValue()));
		}


		private void checkCompatibleArity(RelationResult that) {
			checkArity(getValue().arity(), that.getValue().arity());
		}
		
		private void checkArity(int expected, int given) {
			if (expected != given) {
				throw new TypeErrorException("Incompatible arities in relational operation; expected " + expected + ", got " + given);
			}
		}

		@Override
		<U extends IValue, V extends IValue> AbstractResult<U> insertElement(ValueResult<V> result) {
			// TODO: check that result is tuple and that arity is ok. 
			return makeResult(resultTypeWhenAddingElement(result), (IRelation) getValue().insert(result.getValue()));
		}
		
		
		
}


