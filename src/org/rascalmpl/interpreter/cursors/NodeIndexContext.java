package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

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
