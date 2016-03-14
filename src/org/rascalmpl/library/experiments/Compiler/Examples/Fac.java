package org.rascalmpl.library.experiments.Compiler.Examples;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class Fac {
	IValueFactory vf;
//	private final OverloadedFunction fac_in_rascal;
//	private final OverloadedFunction d1_in_rascal;
//	private final OverloadedFunction d2_in_rascal;
	private final OverloadedFunction getA_in_rascal;
	private final OverloadedFunction getAs_in_rascal;
	private final OverloadedFunction sizeAs_in_rascal;
	
	private final RVMCore rvm;

	Fac(IValueFactory vf){
		rvm = ExecutionTools.initializedRVM("compressed+home", "bin/experiments/Compiler/Examples/Fac.rvm.ser.gz");
//		fac_in_rascal = rvm.getOverloadedFunction("int fac (int n)");
//		d1_in_rascal = rvm.getOverloadedFunction("D d1(int n)");
//		d2_in_rascal = rvm.getOverloadedFunction("D d2(str s)");
		getA_in_rascal = rvm.getOverloadedFunction("A getA()");
		getAs_in_rascal = rvm.getOverloadedFunction("As getAs(int n)");
		sizeAs_in_rascal = rvm.getOverloadedFunction("int size(As as)");
	}

//	IInteger fac(IInteger n){
//		return (IInteger) rvm.executeRVMFunction(fac_in_rascal, new IValue[] { n });
//	}
//	
//	IConstructor d1(IInteger n){
//		return (IConstructor) rvm.executeRVMFunction(d1_in_rascal, new IValue[] { n });
//	}
//	
//	IConstructor d2(IString s){
//		return (IConstructor) rvm.executeRVMFunction(d2_in_rascal, new IValue[] { s });
//	}
	
	IConstructor getA(){
		return (IConstructor) rvm.executeRVMFunction(getA_in_rascal, new IValue[] { });
	}
	
	IConstructor getAs(IInteger n){
		return (IConstructor) rvm.executeRVMFunction(getAs_in_rascal, new IValue[] { n });
	}
	
	IInteger sizeAs(IConstructor c){
		return (IInteger) rvm.executeRVMFunction(sizeAs_in_rascal, new IValue[] {c });
	}
}
