package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

public class Compile extends Command {
	

	/**
	 * Main function for compile command: rascalc
	 * 
	 * @param args	list of command-line arguments
	 * @throws  
	 */
	public static void main(String[] args) {
		
		handleArgs("rascalc", args);
		commandKwArgs.put("serialize",  vf.bool(true));
		
		RascalExecutionContext rex = new RascalExecutionContext("Compile", vf, System.out, System.err);
		
		RVM rvmKernel = null;
		try {
			rvmKernel = RVM.readFromFileAndInitialize(kernelBinaryLocation, rex);
		} catch (IOException e) {
			System.err.println("Cannot initialize: " + e.getMessage());
			System.exit(-1);;
		}
		
		TypeFactory tf = TypeFactory.getInstance();
		Function compileFunction = rvmKernel.getFunction("compileAndLink", tf.abstractDataType(new TypeStore(), "RVMProgram"), tf.tupleType(tf.stringType()));
		
		if(compileFunction == null){
			System.err.println("Cannot find compile function");
			System.exit(-1);;
		}
		rvmKernel.executeFunction(compileFunction, commandArgs, commandKwArgs);
	}
}
