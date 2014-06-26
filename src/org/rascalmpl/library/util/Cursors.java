package org.rascalmpl.library.util;

import java.util.Collections;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.cursors.Context;
import org.rascalmpl.interpreter.cursors.CursorFactory;
import org.rascalmpl.interpreter.cursors.ICursor;
import org.rascalmpl.interpreter.cursors.InvertorContext;
import org.rascalmpl.interpreter.cursors.TopContext;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Cursors {

	public Cursors(IValueFactory vf) {
	}

	public IValue makeCursor(IValue v) {
		IValue c = CursorFactory.makeCursor(v, new TopContext());
		return c;
	}

	public IValue update(IValue cursor, IValue v) {
		checkCursorness("first", cursor);
		return CursorFactory.makeCursor(v, ((ICursor) cursor).getCtx());
	}

	public IValue compute(IValue cursor, IValue to, IValue from) {
		checkCursorness("first", cursor);
		checkUnaryFunction("second", to);
		checkUnaryFunction("third", from);
		ICursor c = (ICursor)cursor;
		Context ctx = new InvertorContext(c.getCtx(), (ICallableValue)from);
		ICallableValue f = (ICallableValue)to;
		IValue computed = f.call(new Type[] {c.getWrappedValue().getType()}, new IValue[] { c.getWrappedValue() }, null).getValue();
		return CursorFactory.makeCursor(computed, ctx);
	}

	

	public IValue getRoot(IValue typ, IValue cursor) {
		checkCursorness("first", cursor);
		return ((ICursor) cursor).root();
	}

	private static void checkCursorness(String arg, IValue cursor) {
		if (!(cursor instanceof ICursor)) {
			throw RuntimeExceptionFactory.illegalArgument(cursor, null, null,
					arg + " argument should be a cursor");
		}
	}

	private static void checkUnaryFunction(String arg, IValue f) {
//		if (!(f instanceof ICallableValue) || ((ICallableValue)f).getArity() != 1) {
//			throw RuntimeExceptionFactory.illegalArgument(f, null, null,
//					arg + " argument should be a unary function");
//		}
	}
}
