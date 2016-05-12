package org.rascalmpl.library.experiments.tutor3;

import java.io.IOException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class RascalUtils {
	IValueFactory vf;
	private OverloadedFunction extractRemoteConcepts;
	
	private RVMCore rvm;
	
	public RascalUtils(IValueFactory vf) throws IOException{
		this.vf = vf;
		if(rvm == null){
			RascalExecutionContext rex = 
					RascalExecutionContextBuilder.normalContext(vf, System.out, System.err)
						.setJVM(true)					// options for complete repl
						.setTrace(false)
						.build();
			rvm = ExecutionTools.initializedRVM(URIUtil.correctLocation("compressed+home", "", "bin/experiments/tutor3/RascalUtils.rvm.ser.gz"), rex);
		}
		try {
			extractRemoteConcepts  = rvm.getOverloadedFunction("str extractRemoteConcepts(str parent, str L)");
		} catch (NoSuchRascalFunction e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Extract concepts from a "remote" Rascal files, i.e. outside a documentation hierarchy
	 * @param L			Location (as a string!) of the Rascal source file
	 * @param kwArgs	Keyword arguments
	 * @return A 		Map with (subconcep-name, subconcept-text) pairs
	 */
	public IString extractRemoteConcepts(IString parent, IString L, IMap kwArgs){
		try {
			return (IString) rvm.executeRVMFunction(extractRemoteConcepts, new IValue[] { parent, L, kwArgs});
		} catch (Exception e){
			e.printStackTrace(System.err);
		}
		return vf.string("");
	}
}
