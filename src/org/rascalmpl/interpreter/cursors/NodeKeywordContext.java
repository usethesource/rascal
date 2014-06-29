package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.util.Cursors;

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

	@Override
	public IList toPath(IValueFactory vf) {
		return ctx.toPath(vf).append(vf.constructor(Cursors.Nav_field, vf.string(keyword)));
	}

}
