package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;

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

}
