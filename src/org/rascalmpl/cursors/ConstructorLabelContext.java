package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;

public class ConstructorLabelContext extends Context {

	private final Context ctx;
	private final String label;
	private final IConstructor constructor;

	public ConstructorLabelContext(Context ctx, String label, IConstructor constructor) {
		this.ctx = ctx;
		this.label = label;
		this.constructor = constructor;
	}

	@Override
	public IValue up(IValue focus) {
		return new ConstructorCursor(constructor.set(label, focus), ctx);
	}

}
