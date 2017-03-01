package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;

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
