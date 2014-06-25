package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;

public class ListContext extends Context {
	private final Context ctx;
	private final IList left;
	private final IList right;

	public ListContext(Context ctx, IList left, IList right) {
		this.ctx = ctx;
		this.left = left;
		this.right = right;
	}

	@Override
	public IValue up(IValue focus) {
		return new ListCursor(left.append(focus).concat(right), ctx);
	}

	
}
