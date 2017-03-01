package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class TopContext extends Context {
	private final String name;

	public TopContext() {
		this("");
	}
	
	public TopContext(String name) {
		this.name = name;
	}

	@Override
	public IValue up(IValue focus) {
		return focus;
	}

	@Override
	public IList toPath(IValueFactory vf) {
		if (!name.isEmpty()) {
			return vf.list(vf.constructor(Cursor.Nav_root, vf.string(name)));
		}
		return vf.list();
	}
}
