package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;

public class NodeKeywordContext extends Context {
	private final Context ctx;
	private final String keyword;
	private final INode node;

	public NodeKeywordContext(Context ctx, String keyword, INode node) {
		this.ctx = ctx;
		this.keyword = keyword;
		this.node = node;
	}

	@Override
	public IValue up(IValue focus) {
		return new NodeCursor(node.set(node.getKeywordIndex(keyword), focus), ctx);
	}

}
