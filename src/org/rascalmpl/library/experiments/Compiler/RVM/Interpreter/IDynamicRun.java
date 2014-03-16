package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IValue;

public interface IDynamicRun {
	public Object dynRun(String fname,  IValue[] args) ;
}
