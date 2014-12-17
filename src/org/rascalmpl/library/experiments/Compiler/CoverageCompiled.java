package org.rascalmpl.library.experiments.Compiler;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CoverageLocationCollector;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;

public class CoverageCompiled {
	
	IValueFactory values;
	CoverageLocationCollector coverageCollector;
	
	CoverageCompiled(IValueFactory values){
		this.values = values;
	}
	
	void startCoverage(RascalExecutionContext rex){
		coverageCollector = new CoverageLocationCollector();
		rex.getRVM().setLocationCollector(coverageCollector);
	}
	
	ISet getCoverage(RascalExecutionContext rex){
		ISet res = coverageCollector.get();
		rex.getRVM().resetLocationCollector();
		return res;
	}
	
	void printCoverage(RascalExecutionContext rex){
		coverageCollector.print(rex.getStdOut());
		rex.getRVM().resetLocationCollector();
	}
}
