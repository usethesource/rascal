package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;

public class NodeResult extends ValueResult {

	private INode node;
	
	public NodeResult(Type type, INode node) {
		super(type, node);
		this.node = node;
	}
	
	@Override
	public INode getValue() {
		return node;
	}
	

}
