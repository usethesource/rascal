package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IRelation;
import org.meta_environment.rascal.interpreter.errors.TypeError;

public class RelationResult extends AbstractResult {

		private IRelation rel;

		public RelationResult(IRelation rel) {
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
		protected RelationResult addRelation(RelationResult r) {
			checkArities(r);
			return new RelationResult((IRelation)rel.union(r.rel));
		}
		

		@Override 
		protected RelationResult subtractRelation(RelationResult r) {			
			checkArities(r);
			return new RelationResult((IRelation) r.getValue().union(getValue()));
		}

		private void checkArities(RelationResult r) {
			// TODO: fix this
			if (r.getValue().arity() != getValue().arity()) {
				throw new TypeError("Incompatible arities in relatios operation");
			}
		}
		
		
		
}


