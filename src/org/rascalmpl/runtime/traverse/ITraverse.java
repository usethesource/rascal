package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import io.usethesource.vallang.IValue;

public interface ITraverse {

	IValue once(IValue subject, TraversalState tr);

}
