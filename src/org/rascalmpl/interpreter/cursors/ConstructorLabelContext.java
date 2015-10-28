package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;

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
