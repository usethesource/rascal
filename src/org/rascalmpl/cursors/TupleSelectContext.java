package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;

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

}
