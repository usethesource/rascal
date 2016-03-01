package org.rascalmpl.library.experiments.Compiler.Examples;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

import experiments.Compiler.Examples.Fac$Compiled;	// <== experiments.Compiler.Examples.Fac.rsc compiled to a class file

public class Fac {

	Fac$Compiled facCompiled;
	IValueFactory vf;

	Fac(IValueFactory vf){
		this.vf = vf;
		ISourceLocation facBinLoc = null;
		try {
			facBinLoc = vf.sourceLocation("compressed+home", "","bin/experiments/Compiler/Examples/Fac.rvm.ser.gz");
		} catch (URISyntaxException e) {
			System.err.println("Could not create FacBin location");;
		}
		RVMExecutable rvmExecutable = null; 
		try {
			rvmExecutable = RVMExecutable.read(facBinLoc);
		} catch (IOException e) {
			e.printStackTrace();
		}

		RascalExecutionContext rex = 
				RascalExecutionContextBuilder.normalContext(vf, System.out, System.err)
				.forModule("Fac")
				.setJVM(true)
				.build();

		facCompiled = new Fac$Compiled(rvmExecutable, rex);
	}

	IInteger fac(IInteger n){
		return (IInteger)  facCompiled.executeFunction("experiments::Compiler::Examples::Fac/fac(int();)#5", 
														new IValue[] { n }, 
														null);
	}
}
