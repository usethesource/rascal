package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.util.Cursors;

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

	@Override
	public IList toPath(IValueFactory vf) {
		return ctx.toPath(vf).append(vf.constructor(Cursors.Nav_field, vf.string(label)));
	}

}
