package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

import org.eclipse.imp.pdb.facts.IValue;

public interface ITraverse {

	IValue once(IValue subject, TraversalState tr);

}
