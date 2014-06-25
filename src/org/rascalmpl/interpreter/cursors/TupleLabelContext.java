package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;

public class TupleLabelContext extends Context {

	private final Context ctx;
	private final String label;
	private final ITuple tuple;

	public TupleLabelContext(Context ctx, String label, ITuple tuple) {
		this.ctx = ctx;
		this.label = label;
		this.tuple = tuple;
	}

	@Override
	public IValue up(IValue focus) {
		return new TupleCursor(tuple.set(label, focus), ctx);
	}

}
