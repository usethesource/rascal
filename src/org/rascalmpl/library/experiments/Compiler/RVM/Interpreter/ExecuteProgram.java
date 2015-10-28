package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.rascalmpl.interpreter.IEvaluatorContext;  // TODO: remove import? NOT YET: Only used as argument of reflective library function
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class ExecuteProgram {

	public ExecuteProgram(IValueFactory vf) {
	}
	
	// Library function to execute a RVMProgram
	// (Interpreter version)

	public IValue executeProgram(
			ISourceLocation rvmProgramLoc,
			IConstructor rvmProgram,
			IMap keywordArguments,
			IBool debug, 
			IBool testsuite, 
			IBool profile, 
			IBool trackCalls, 
			IBool coverage,
			IBool useJVM,
			IBool serialize, 
			IEvaluatorContext ctx
			) {

		RVMExecutable executable = ExecutionTools.load(rvmProgramLoc, rvmProgram, useJVM, serialize);

		RascalExecutionContext rex = ExecutionTools.makeRex(executable, debug, testsuite, profile, trackCalls, coverage, useJVM, ctx.getEvaluator().getRascalResolver());
		return ExecutionTools.executeProgram(executable, keywordArguments, rex);
	}

	// Library function to execute an RVMProgram
	// (Compiler version)

	public IValue executeProgram(
			ISourceLocation rvmProgramLoc,
			IConstructor rvmProgram,
			IMap keywordArguments,
			IBool debug, 
			IBool testsuite, 
			IBool profile, 
			IBool trackCalls, 
			IBool coverage,
			IBool useJVM,
			IBool serialize, 
			RascalExecutionContext rex
			) {

		RVMExecutable executable = ExecutionTools.load(rvmProgramLoc, rvmProgram, useJVM, serialize);

		RascalExecutionContext rex2 = ExecutionTools.makeRex(executable, debug, testsuite, profile, trackCalls, coverage, useJVM, rex.getRascalSearchPath());
		return ExecutionTools.executeProgram(executable, keywordArguments, rex2);
	}

	// Library function to link and execute a RVM program from file
	// (Interpreter version)

	public IValue executeProgram(
			ISourceLocation rvmExecutableLoc,
			IMap keywordArguments,
			IBool debug, 
			IBool testsuite, 
			IBool profile, 
			IBool trackCalls, 
			IBool coverage,
			IBool useJVM,
			IEvaluatorContext ctx
			) {

		RVMExecutable executable = ExecutionTools.load(rvmExecutableLoc);
		RascalExecutionContext rex = ExecutionTools.makeRex(executable, debug, testsuite, profile, trackCalls, coverage, useJVM, ctx.getEvaluator().getRascalResolver());
		return ExecutionTools.executeProgram(executable, keywordArguments, rex);
		}
		
	// Library function to link and execute a RVM program from file
	// (Compiler version)

	public IValue executeProgram(
			ISourceLocation rvmExecutableLoc,
			IMap keywordArguments,
			IBool debug, 
			IBool testsuite, 
			IBool profile, 
			IBool trackCalls, 
			IBool coverage,
			IBool useJVM,
			RascalExecutionContext rex
			) {
		RVMExecutable executable = ExecutionTools.load(rvmExecutableLoc);
		RascalExecutionContext rex2 = ExecutionTools.makeRex(executable, debug, testsuite, profile, trackCalls, coverage, useJVM, rex.getRascalSearchPath());
		return ExecutionTools.executeProgram(executable, keywordArguments, rex2);
	}

}
