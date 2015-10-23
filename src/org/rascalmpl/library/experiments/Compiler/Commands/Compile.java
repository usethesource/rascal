package org.rascalmpl.library.experiments.Compiler.Commands;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

public class Compile extends Command {
	

	/**
	 * Main function for compile command: rascalc
	 * 
	 * @param args	list of command-line arguments
	 */
	public static void main(String[] args) {
		
		handleArgs("rascalc", args);
		commandKwArgs.put("serialize",  vf.bool(true));
		
		RascalExecutionContext rex = new RascalExecutionContext("Compile", vf, System.out, System.err);
		
		RVM rvmKernel = RVM.readFromFileAndInitialize(kernelBinaryLocation, rex);
		
		TypeFactory tf = TypeFactory.getInstance();
		Function compileFunction = rvmKernel.getFunction("compile", tf.abstractDataType(new TypeStore(), "RVMModule"), tf.tupleType(tf.stringType()));
		
		if(compileFunction == null){
			System.err.println("Cannot find compile function");
			System.exit(-1);;
		}
		rvmKernel.executeFunction(compileFunction, commandArgs, commandKwArgs);
	}
}
