package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.errors.TypeError;

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
			// TODO: check arity
			return new RelationResult(type, getValue().closure());
		}
		
		@Override
		public AbstractResult transitiveReflexiveClosure() {
			// TODO: check arity
			return new RelationResult(type, getValue().closureStar());
		}
		
		
		@Override
		protected RelationResult addRelation(RelationResult r) {
			checkArities(r);
			return new RelationResult(type, (IRelation)rel.union(r.rel));
		}
		

		@Override 
		protected RelationResult subtractRelation(RelationResult r) {			
			checkArities(r);
			return new RelationResult(type, (IRelation) r.getValue().union(getValue()));
		}

		private void checkArities(RelationResult r) {
			// TODO: fix this
			if (r.getValue().arity() != getValue().arity()) {
				throw new TypeError("Incompatible arities in relational operation");
			}
		}

		@Override
		CollectionResult insertElement(ValueResult result) {
			// TODO: typechecking
			return new RelationResult(type, (IRelation) getValue().insert(result.getValue()));
		}
		
		
		
}


