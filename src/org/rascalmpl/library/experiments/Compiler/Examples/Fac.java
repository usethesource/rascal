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
	private final OverloadedFunction fac;
	private final OverloadedFunction d1;
	private final OverloadedFunction d2;
	private final OverloadedFunction getA;
	private final OverloadedFunction getAs;
	private final OverloadedFunction sizeAs;
	
	private final RVMCore rvm;

	Fac(IValueFactory vf){
		rvm    = ExecutionTools.initializedRVM("compressed+home", "bin/experiments/Compiler/Examples/Fac.rvm.ser.gz");
		fac    = rvm.getOverloadedFunction("int fac (int n)");
		d1     = rvm.getOverloadedFunction("D d1(int n)");
		d2     = rvm.getOverloadedFunction("D d2(str s)");
		getA   = rvm.getOverloadedFunction("A getA()");
		getAs  = rvm.getOverloadedFunction("As getAs(int n)");
		sizeAs = rvm.getOverloadedFunction("int size(As as)");
	}

	IInteger fac(IInteger n){
		return (IInteger) rvm.executeRVMFunction(fac, new IValue[] { n });
	}
	
	IConstructor d1(IInteger n){
		return (IConstructor) rvm.executeRVMFunction(d1, new IValue[] { n });
	}
	
	IConstructor d2(IString s){
		return (IConstructor) rvm.executeRVMFunction(d2, new IValue[] { s });
	}
	
	IConstructor getA(){
		return (IConstructor) rvm.executeRVMFunction(getA, new IValue[] { });
	}
	
	IConstructor getAs(IInteger n){
		return (IConstructor) rvm.executeRVMFunction(getAs, new IValue[] { n });
	}
	
	IInteger sizeAs(IConstructor c){
		return (IInteger) rvm.executeRVMFunction(sizeAs, new IValue[] {c });
	}
}
