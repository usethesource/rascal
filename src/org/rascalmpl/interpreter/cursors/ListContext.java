package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

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
