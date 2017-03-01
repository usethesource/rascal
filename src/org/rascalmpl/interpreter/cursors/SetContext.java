package org.rascalmpl.interpreter.cursors;

import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;

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
