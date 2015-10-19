package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public abstract class Context {

	public abstract IValue up(IValue focus);
	
	public  IList toPath(IValueFactory vf) {
		throw RuntimeExceptionFactory.illegalArgument(null, null);
	}
}
