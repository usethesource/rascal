package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IValue;

public class TopContext extends Context {
	@Override
	public IValue up(IValue focus) {
		return focus;
	}
}
