package org.rascalmpl.library.experiments.Compiler;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValueFactory;

public class Coverage {
	protected final IValueFactory values;
	
	public Coverage(IValueFactory values){
		super();
		this.values = values;
	}
	
	@SuppressWarnings("unused")
    public void startCoverage(IEvaluatorContext ctx){
		throw new CompilerError("startCoverage only implemented for compiled code");
	}
	
	@SuppressWarnings("unused")
    public ISet stopCoverage(IEvaluatorContext ctx){
		throw new CompilerError("stopCoverage only implemented for compiled code");
	}
	
	@SuppressWarnings("unused")
    public ISet getCoverage(IEvaluatorContext ctx){
		throw new CompilerError("getCoverage only implemented for compiled code");
	}
	
//	public void printCoverage(IEvaluatorContext ctx){
//		throw new CompilerError("printCoverage only implemented for compiled code");
//	}
}
