package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import io.usethesource.vallang.IValue;

public interface IVisitFunction {
	IValue execute(IValue v, TraversalState ts);
}
