package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class TupleSelectContext extends Context {
	private final Context ctx;
	private final int fields[];
	private final ITuple tuple;

	public TupleSelectContext(Context ctx, int fields[], ITuple tuple) {
		this.ctx = ctx;
		this.fields = fields;
		this.tuple = tuple;
	}

	@Override
	public IValue up(IValue focus) {
		ITuple sub = (ITuple)focus;
		int i = 0;
		ITuple newTuple = tuple;
		for (IValue elt: sub) {
			newTuple = newTuple.set(fields[i], elt);
			i++;
		}
		return new TupleCursor(newTuple, ctx);
	}

	@Override
	public IList toPath(IValueFactory vf) {
		IInteger[] labels = new IInteger[fields.length];
		for (int i = 0; i < fields.length; i++) {
			labels[i] = vf.integer(fields[i]);
		}
		return ctx.toPath(vf).append(vf.constructor(Cursor.Nav_selectByIndex, vf.list(labels)));
	}

}
