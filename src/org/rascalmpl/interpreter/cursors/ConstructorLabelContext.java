package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.util.Cursor;

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
		Type type = constructor.getConstructorType();
		return ctx.toPath(vf).append(vf.constructor(Cursor.Nav_argumentPosition, 
				vf.integer(type.getFieldIndex(label))));
	}

}
