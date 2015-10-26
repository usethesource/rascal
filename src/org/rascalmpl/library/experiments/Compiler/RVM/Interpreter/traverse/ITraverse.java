package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

import org.rascalmpl.value.IValue;

public interface ITraverse {

	IValue once(IValue subject, TraversalState tr);

}
