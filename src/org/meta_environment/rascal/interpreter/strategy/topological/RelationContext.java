package org.meta_environment.rascal.interpreter.strategy.topological;

import org.eclipse.imp.pdb.facts.IRelation;

public class RelationContext {
	
	private IRelation relation;

	public RelationContext(IRelation relation) {
		super();
		this.relation = relation;
	}

	public IRelation getRelation() {
		return relation;
	}

	public void setRelation(IRelation relation) {
		this.relation = relation;
	}

}
