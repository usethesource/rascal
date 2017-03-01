package org.rascalmpl.library.experiments.Compiler;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.CoverageFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValueFactory;

public class CoverageCompiled extends Coverage {
	
	private CoverageFrameObserver coverageObserver;
	
	public CoverageCompiled(IValueFactory values){
		super(values);
	}
	
	public void startCoverage(RascalExecutionContext rex){
		if(coverageObserver == null){
			coverageObserver = new CoverageFrameObserver(null);
		}
		coverageObserver.startObserving();
	}
	
	public void stopCoverage(RascalExecutionContext rex){
		coverageObserver.stopObserving();
	}
	
	public IList getCoverage(RascalExecutionContext rex){
		assert coverageObserver != null: "startCoverage not called before getCoverage";
		IList res = coverageObserver.getData();
		coverageObserver.stopObserving();
		return res;
	}
	
//	public void printCoverage(RascalExecutionContext rex){
//		System.err.println("printCoverage");
//		coverageCollector.printData(rex.getStdOut());
//		rex.getRVM().resetLocationCollector();
//	}
}
