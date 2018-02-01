package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

import io.usethesource.vallang.IValue;

public interface ITraverse {

	IValue once(IValue subject, TraversalState tr);

}
