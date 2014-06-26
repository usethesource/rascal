package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.util.Cursors;

public class MapContext extends Context {
	// todo: pull up ctx
	private final Context ctx;
	private final IValue key;
	private final IMap map;

	public MapContext(Context ctx, IValue key, IMap map) {
		this.ctx = ctx;
		this.key = key;
		this.map = map;
	}

	@Override
	public IValue up(IValue focus) {
		return new MapCursor(map.put(key, focus), ctx);
	}

	@Override
	public IList toPath(IValueFactory vf) {
		return ctx.toPath(vf).append(vf.constructor(Cursors.Nav_lookup, key));
	}

}
