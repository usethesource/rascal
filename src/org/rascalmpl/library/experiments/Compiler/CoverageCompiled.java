package org.rascalmpl.library.experiments.Compiler;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CoverageLocationCollector;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValueFactory;

public class CoverageCompiled extends Coverage {
	
	private static CoverageLocationCollector coverageCollector;
	
	public CoverageCompiled(IValueFactory values){
		super(values);
	}
	
	public void startCoverage(RascalExecutionContext rex){
		if(coverageCollector == null){
			coverageCollector = new CoverageLocationCollector();
		}
		rex.getRVM().setLocationCollector(coverageCollector);
		//System.err.println("startCoverage");
	}
	
	public void stopCoverage(RascalExecutionContext rex){
		rex.getRVM().resetLocationCollector();
	}
	
	public ISet getCoverage(RascalExecutionContext rex){
		//System.err.println("getCoverage");
		assert coverageCollector != null: "startCoverage not called before getCoverage";
		ISet res = CoverageCompiled.coverageCollector.getData();
		rex.getRVM().resetLocationCollector();
		//System.err.println("getCoverage, returns " + res);
		coverageCollector = null;
		return res;
	}
	
//	public void printCoverage(RascalExecutionContext rex){
//		System.err.println("printCoverage");
//		coverageCollector.printData(rex.getStdOut());
//		rex.getRVM().resetLocationCollector();
//	}
}
