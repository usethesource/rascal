package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;

public class NodeIndexContext extends Context {
	private final Context ctx;
	private final int index;
	private final INode node;
	
	public NodeIndexContext(Context ctx, int index, INode node) {
		this.ctx = ctx;
		this.index = index;
		this.node = node;
	}
	
	@Override
	public IValue up(IValue focus) {
		return new NodeCursor(node.set(index, focus), ctx);
	}

}
