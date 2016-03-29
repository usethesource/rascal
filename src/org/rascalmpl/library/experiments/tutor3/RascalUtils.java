package org.rascalmpl.library.experiments.tutor3;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class RascalUtils {
	IValueFactory vf;
	private OverloadedFunction extractRemoteConcepts;
	
	private RVMCore rvm;
	
	public RascalUtils(IValueFactory vf){
		this.vf = vf;
		if(rvm == null){
			rvm = ExecutionTools.initializedRVM("compressed+home", "bin/experiments/tutor3/RascalUtils.rvm.ser.gz");
		}
		try {
			extractRemoteConcepts  = rvm.getOverloadedFunction("map[str,str] extractRemoteConcepts(str L, str root)");
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
	public IMap extractRemoteConcepts(IString L, IString root, IMap kwArgs){
		return (IMap) rvm.executeRVMFunction(extractRemoteConcepts, new IValue[] { L, root, kwArgs});
	}
}
