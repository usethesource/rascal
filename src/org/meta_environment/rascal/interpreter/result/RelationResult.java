package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.exceptions.TypeErrorException;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class RelationResult extends CollectionResult {

		private IRelation rel;

		public RelationResult(Type type, IRelation rel) {
			super(type, rel);
			this.rel = rel;
		}

		@Override
		public IRelation getValue() {
			return rel;
		}
		
		
		@Override
		public AbstractResult add(AbstractResult result) {
			return result.addRelation(this);
		}
		
		@Override
		public AbstractResult subtract(AbstractResult result) {
			return result.subtractRelation(this);
		}
		
		
		////
		
		@Override
		public AbstractResult transitiveClosure() {
			checkArity(2, getValue().arity());
			return makeResult(type, getValue().closure());
		}
		

		@Override
		public AbstractResult transitiveReflexiveClosure() {
			checkArity(2, getValue().arity());
			return makeResult(type, getValue().closureStar());
		}
		
		
		@Override
		protected RelationResult addRelation(RelationResult r) {
			checkCompatibleArity(r);
			return makeResult(type.lub(r.type), (IRelation)rel.union(r.rel));
		}
		

		@Override 
		protected RelationResult subtractRelation(RelationResult r) {			
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
		CollectionResult insertElement(ValueResult result) {
			// TODO: check that result is tuple and that arity is ok. 
			return makeResult(resultTypeWhenAddingElement(result), (IRelation) getValue().insert(result.getValue()));
		}
		
		
		
}


