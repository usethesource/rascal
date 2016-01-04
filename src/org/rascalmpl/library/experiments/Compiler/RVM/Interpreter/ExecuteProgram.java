package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;

import org.rascalmpl.interpreter.IEvaluatorContext;  // TODO: remove import? NOT YET: Only used as argument of reflective library function
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;


public class ExecuteProgram {
	IValueFactory vf;
	

	public ExecuteProgram(IValueFactory vf) {
		this.vf =vf;
	}
	
	private boolean checkErrors(IConstructor rvmProgram) throws IOException{
		IConstructor main_module = (IConstructor) rvmProgram.get("main_module");
		ISet messages = (ISet) main_module.get("messages");
		ISetWriter w = vf.setWriter();
		for(IValue m : messages){
			if(((IConstructor) m).getName().equals("error"))
				w.insert(m);	
		}
		ISet errors = w.done();
		if(errors.size() > 0){
			throw new IOException("Cannot execute program with errors: " + errors.toString());
		}
		return false;
	}
	
	// Library function to serialize a RVMProgram

	public void serializeProgram(
			ISourceLocation rvmProgramLoc,
			IConstructor rvmProgram,
			IBool jvm
			) throws IOException {

		checkErrors(rvmProgram);

		ExecutionTools.load(rvmProgramLoc, rvmProgram, jvm);
	}
	
	// Library function to execute a RVMProgram
	// (Interpreter version)

	public IValue executeProgram(
			ISourceLocation rvmProgramLoc,
			IConstructor rvmProgram,
			IMap keywordArguments,
			IBool debug, 
			IBool debugRVM, 
			IBool testsuite, 
			IBool profile, 
			IBool trackCalls, 
			IBool coverage,
			IBool jvm,
			IEvaluatorContext ctx
			) throws IOException {

		checkErrors(rvmProgram);
		
		RVMExecutable executable = ExecutionTools.load(rvmProgramLoc, rvmProgram, jvm);

		RascalExecutionContext rex = ExecutionTools.makeRex(executable, debug, debugRVM, testsuite, profile, trackCalls, coverage, jvm, ctx.getEvaluator().getRascalResolver());
		return ExecutionTools.executeProgram(executable, keywordArguments, rex);
	}

	// Library function to execute an RVMProgram
	// (Compiler version)

	public IValue executeProgram(
			ISourceLocation rvmProgramLoc,
			IConstructor rvmProgram,
			IMap keywordArguments,
			IBool debug, 
			IBool debugRVM, 
			IBool testsuite, 
			IBool profile, 
			IBool trackCalls, 
			IBool coverage,
			IBool jvm, 
			RascalExecutionContext rex
			) throws IOException {

		checkErrors(rvmProgram);
		
		RVMExecutable executable = ExecutionTools.load(rvmProgramLoc, rvmProgram, jvm);

		RascalExecutionContext rex2 = ExecutionTools.makeRex(executable, debug, debugRVM, testsuite, profile, trackCalls, coverage, jvm, rex.getRascalSearchPath());
		return ExecutionTools.executeProgram(executable, keywordArguments, rex2);
	}

	// Library function to link and execute a RVM program from file
	// (Interpreter version)

	public IValue executeProgram(
			ISourceLocation rvmExecutableLoc,
			IMap keywordArguments,
			IBool debug,
			IBool debugRVM, 
			IBool testsuite, 
			IBool profile, 
			IBool trackCalls, 
			IBool coverage,
			IBool jvm,
			IEvaluatorContext ctx
			) throws IOException {

		RVMExecutable executable = ExecutionTools.load(rvmExecutableLoc);
		RascalExecutionContext rex = ExecutionTools.makeRex(executable, debug, debugRVM, testsuite, profile, trackCalls, coverage, jvm, ctx.getEvaluator().getRascalResolver());
		return ExecutionTools.executeProgram(executable, keywordArguments, rex);
		}
		
	// Library function to link and execute a RVM program from file
	// (Compiler version)

	public IValue executeProgram(
			ISourceLocation rvmExecutableLoc,
			IMap keywordArguments,
			IBool debug,
			IBool debugRVM, 
			IBool testsuite, 
			IBool profile, 
			IBool trackCalls, 
			IBool coverage,
			IBool jvm,
			RascalExecutionContext rex
			) throws IOException {
		RVMExecutable executable = ExecutionTools.load(rvmExecutableLoc);
		RascalExecutionContext rex2 = ExecutionTools.makeRex(executable, debug, debugRVM, testsuite, profile, trackCalls, coverage, jvm, rex.getRascalSearchPath());
		return ExecutionTools.executeProgram(executable, keywordArguments, rex2);
	}

}
