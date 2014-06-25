package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;

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

}
