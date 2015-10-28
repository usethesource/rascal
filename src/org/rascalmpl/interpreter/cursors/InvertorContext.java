package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

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
