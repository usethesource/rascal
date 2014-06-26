package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class TopContext extends Context {
	@Override
	public IValue up(IValue focus) {
		return focus;
	}

	@Override
	public IList toPath(IValueFactory vf) {
		return vf.list();
	}
}
