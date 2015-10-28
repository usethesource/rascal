package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class TupleSelectByFieldNamesContext extends Context {
	private final Context ctx;
	private final String fields[];
	private final ITuple tuple;

	public TupleSelectByFieldNamesContext(Context ctx, String fields[], ITuple tuple) {
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
		}
		return new TupleCursor(newTuple, ctx);
	}
	
	@Override
	public IList toPath(IValueFactory vf) {
		IString[] labels = new IString[fields.length];
		for (int i = 0; i < fields.length; i++) {
			labels[i] = vf.string(fields[i]);
		}
		return ctx.toPath(vf).append(vf.constructor(Cursor.Nav_selectByLabel, vf.list(labels)));
	}

}
