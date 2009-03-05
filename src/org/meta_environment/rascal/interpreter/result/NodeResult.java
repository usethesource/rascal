package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;

public class NodeResult extends ValueResult<INode> {

	public NodeResult(Type type, INode node) {
		super(type, node);
	}
	

}
