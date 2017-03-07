package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.interpreter.result.ICallableValue;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class InvertorContext extends Context {
	private final Context ctx;
	private final ICallableValue invert;

	public InvertorContext(Context ctx, ICallableValue invert) {
		assert invert.getArity() == 1;
		this.ctx = ctx;
		this.invert = invert;
	}

	@Override
	public IValue up(IValue focus) {
		return CursorFactory.makeCursor(invert.call(
				new Type[] {focus.getType()} , 
				new IValue[] { focus }, 
				null).getValue(), ctx);
	}


}
