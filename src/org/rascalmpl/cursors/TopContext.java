package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.IValue;

public class TopContext extends Context {
	@Override
	public IValue up(IValue focus) {
		return focus;
	}
}
