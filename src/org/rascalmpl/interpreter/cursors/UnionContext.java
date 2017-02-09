package org.rascalmpl.interpreter.cursors;

import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;

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
