package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValue;

public class UnionContext extends Context {
	private final Context ctx;
	private final ISet backing;

	public UnionContext(Context ctx, ISet backing) {
		this.ctx = ctx;
		this.backing = backing;
	}

	@Override
	public IValue up(IValue focus) {
		return new SetCursor(backing.union((ISet) focus), ctx);
	}

}
