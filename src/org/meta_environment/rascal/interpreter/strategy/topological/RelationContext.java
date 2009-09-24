package org.meta_environment.rascal.interpreter.strategy.topological;

import org.eclipse.imp.pdb.facts.IRelation;
import org.meta_environment.rascal.interpreter.strategy.IVisitable;

public class RelationContext {
	
	private IRelation relation;
	private IVisitable currentNode;
	
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

	public IVisitable getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(IVisitable currentNode) {
		this.currentNode = currentNode;
	}

}
