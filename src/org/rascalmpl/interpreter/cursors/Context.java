package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public abstract class Context {

	public abstract IValue up(IValue focus);
	
	public  IList toPath(IValueFactory vf) {
		throw RuntimeExceptionFactory.illegalArgument(null, null);
	}
}
