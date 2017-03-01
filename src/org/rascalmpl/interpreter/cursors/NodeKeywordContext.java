package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

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
		return new NodeCursor(node.asWithKeywordParameters().setParameter(keyword, focus), ctx);
//		return new NodeCursor(node.set(node.getKeywordIndex(keyword), focus), ctx);
	}

	@Override
	public IList toPath(IValueFactory vf) {
		return ctx.toPath(vf).append(vf.constructor(Cursor.Nav_argumentName, vf.string(keyword)));
	}

}
