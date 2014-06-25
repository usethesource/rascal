package org.rascalmpl.library.util;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.cursors.Cursor;
import org.rascalmpl.cursors.ICursor;
import org.rascalmpl.cursors.TopContext;
import org.rascalmpl.cursors.TypeToCursor;

public class Cursors {

	public Cursors(IValueFactory vf) {
	}

	public IValue makeCursor(IValue typ, IValue v) {
		IValue c = TypeToCursor.makeCursor(v, new TopContext());
		return c;
	}

	public IValue update(IValue typ, IValue cursor, IValue v) {
		return TypeToCursor.makeCursor(v, ((ICursor) cursor).getCtx());
	}

	public IValue getRoot(IValue typ, IValue v) {
		if (v instanceof ICursor) {
			return ((ICursor) v).root();
		}
		// TODO: check type of v (?)
		return v;
	}

}
