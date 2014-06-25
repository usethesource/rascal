package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IValue;

public abstract class Context {

	public abstract IValue up(IValue focus);
}
