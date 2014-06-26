package org.rascalmpl.library.util;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.cursors.CursorFactory;
import org.rascalmpl.interpreter.cursors.ICursor;
import org.rascalmpl.interpreter.cursors.TopContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Cursors {

	public Cursors(IValueFactory vf) {
	}

	public IValue makeCursor(IValue v) {
		IValue c = CursorFactory.makeCursor(v, new TopContext());
		return c;
	}

	public IValue update(IValue cursor, IValue v) {
		checkCursorness(cursor);
		return CursorFactory.makeCursor(v, ((ICursor) cursor).getCtx());
	}

	public IValue getRoot(IValue typ, IValue cursor) {
		checkCursorness(cursor);
		return ((ICursor) cursor).root();
	}

	private static void checkCursorness(IValue cursor) {
		if (!(cursor instanceof ICursor)) {
			throw RuntimeExceptionFactory.illegalArgument(cursor, null, null,
					"second argument should be a cursor");
		}
	}

}
