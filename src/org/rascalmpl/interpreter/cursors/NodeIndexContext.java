package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

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
