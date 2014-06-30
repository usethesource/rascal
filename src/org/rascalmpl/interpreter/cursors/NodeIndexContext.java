package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.util.Cursor;

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

	@Override
	public IList toPath(IValueFactory vf) {
		return ctx.toPath(vf).append(vf.constructor(Cursor.Nav_argumentPosition, vf.integer(index)));
	}
}
