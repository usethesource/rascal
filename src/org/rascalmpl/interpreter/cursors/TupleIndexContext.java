package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class TupleIndexContext extends Context {

	// TODO: get index/field logic in shared superclass.
	// same for nodes/constructors.
	
	private final Context ctx;
	private final int index;
	private final ITuple tuple;

	public TupleIndexContext(Context ctx, int index, ITuple tuple) {
		this.ctx = ctx;
		this.index = index;
		this.tuple = tuple;
	}

	@Override
	public IValue up(IValue focus) {
		return new TupleCursor(tuple.set(index, focus), ctx);
	}

	@Override
	public IList toPath(IValueFactory vf) {
		return ctx.toPath(vf).append(vf.constructor(Cursor.Nav_fieldPosition, vf.integer(index)));
	}
}
