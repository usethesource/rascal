package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.INode;

public class NodeResult extends ValueResult {

	private INode node;
	
	public NodeResult(INode node) {
		this.node = node;
	}
	
	@Override
	public INode getValue() {
		return node;
	}
	

}
