package org.rascalmpl.library.experiments.Compiler.Commands;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
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
		
		RascalExecutionContext rex = new RascalExecutionContext(vf, System.out, System.err);
		rex.setCurrentModuleName("Compile");
		RVMExecutable rvmKernelExecutable = RVMExecutable.read(kernelBinaryLocation);
		RVM rvmKernel = ExecutionTools.initializedRVM(rvmKernelExecutable, rex);
		TypeFactory tf = TypeFactory.getInstance();
		Function compileFunction = rvmKernel.getFunction("compile", tf.abstractDataType(new TypeStore(), "RVMModule"), tf.tupleType(tf.stringType()));
		if(compileFunction == null){
			System.err.println("Cannot find compile function");
			System.exit(-1);;
		}
		IConstructor consoleRVMProgram = (IConstructor) rvmKernel.executeFunction(compileFunction, commandArgs, commandKwArgs);
	}
}
