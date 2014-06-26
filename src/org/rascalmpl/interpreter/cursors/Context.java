package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public abstract class Context {

	public abstract IValue up(IValue focus);
	
	public  IList toPath(IValueFactory vf) {
		throw RuntimeExceptionFactory.illegalArgument(null, null);
	}
}
