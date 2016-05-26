package org.rascalmpl.library.experiments.Compiler.RascalExtraction;

import java.io.IOException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class RascalExtraction {
	IValueFactory vf;
	private OverloadedFunction extractDoc;
	
	private RVMCore rvm;
	
	public RascalExtraction(IValueFactory vf) throws IOException{
		this.vf = vf;
		if(rvm == null){
			RascalExecutionContext rex = 
					RascalExecutionContextBuilder.normalContext(vf, System.out, System.err)
						.setJVM(true)					// options for complete repl
						.setTrace(false)
						.build();
			rvm = ExecutionTools.initializedRVM(URIUtil.correctLocation("compressed+home", "", "bin/experiments/Compiler/RascalExtraction/RascalExtraction.rvm.ser.gz"), rex);
		}
		try {
			extractDoc = rvm.getOverloadedFunction("str extractDoc(str parent, loc moduleLoc)");
		} catch (NoSuchRascalFunction e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Extract concepts from a "remote" Rascal files, i.e. outside a documentation hierarchy
	 * @param moduleLoc	Location of the Rascal source file
	 * @param kwArgs	Keyword arguments
	 * @return A string with extracted documentation
	 */
	public IString extractDoc(IString parent, ISourceLocation moduleLoc, IMap kwArgs){
		try {
			return (IString) rvm.executeRVMFunction(extractDoc, new IValue[] { parent, moduleLoc, kwArgs});
		} catch (Exception e){
			e.printStackTrace(System.err);
		}
		return vf.string("");
	}
}
