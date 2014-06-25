package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;

public class SetContext extends Context {
	private final Context ctx;
	private final ISet rest;

	public SetContext(Context ctx, ISet rest) {
		this.ctx = ctx;
		this.rest = rest;
	}

	@Override
	public IValue up(IValue focus) {
		return new SetCursor(rest.insert(focus), ctx);
	}
}
