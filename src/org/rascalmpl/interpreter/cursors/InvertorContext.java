package org.rascalmpl.interpreter.cursors;

import java.util.Collections;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.ICallableValue;

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
