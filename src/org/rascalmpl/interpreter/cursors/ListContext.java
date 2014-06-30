package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.util.Cursor;

public class ListContext extends Context {
	private final Context ctx;
	private final int index;
	private final IList list;

	public ListContext(Context ctx, int index, IList list) {
		this.ctx = ctx;
		this.index = index;
		this.list = list;
	}

	@Override
	public IValue up(IValue focus) {
		return new ListCursor(list.put(index, focus), ctx);
	}

	@Override
	public IList toPath(IValueFactory vf) {
		return ctx.toPath(vf).append(vf.constructor(Cursor.Nav_element, vf.integer(index)));
	}

	
}
