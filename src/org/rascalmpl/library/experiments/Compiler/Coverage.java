package org.rascalmpl.library.experiments.Compiler;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;

public class Coverage {
	protected final IValueFactory values;
	
	public Coverage(IValueFactory values){
		super();
		this.values = values;
	}
	
	public void startCoverage(IEvaluatorContext ctx){
		throw new CompilerError("startCoverage only implemented for compiled code");
	}
	
	public ISet stopCoverage(IEvaluatorContext ctx){
		throw new CompilerError("stopCoverage only implemented for compiled code");
	}
	
	public ISet getCoverage(IEvaluatorContext ctx){
		throw new CompilerError("getCoverage only implemented for compiled code");
	}
	
//	public void printCoverage(IEvaluatorContext ctx){
//		throw new CompilerError("printCoverage only implemented for compiled code");
//	}
}
