package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;

public class SubListContext extends Context {

	private final Context ctx;
	private final int offset;
	private final int length;
	private final IList list;

	public SubListContext(Context ctx, int offset, int length, IList list) {
		this.ctx = ctx;
		this.offset = offset;
		this.length = length;
		this.list = list;
	}

	@Override
	public IValue up(IValue focus) {
		IList fl = (IList)focus;
		return new ListCursor(list.sublist(0, offset).concat(fl.concat(list.sublist(offset + length, list.length() - (offset + length)))), ctx);
	}

}
