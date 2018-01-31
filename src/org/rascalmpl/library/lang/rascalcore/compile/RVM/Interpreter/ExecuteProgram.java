package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;

import org.rascalmpl.interpreter.IEvaluatorContext;  // TODO: remove import? NOT YET: Only used as argument of reflective library function
import org.rascalmpl.library.util.PathConfig;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;


public class ExecuteProgram {
	IValueFactory vf;
	
	public ExecuteProgram(IValueFactory vf) {
		this.vf = vf;
	}
	
	// Library function to serialize a RVMProgram

	public void linkAndSerializeProgram(
			ISourceLocation rvmProgramLoc,
			IConstructor rvmProgram,
			IBool jvm,
			IMap classRenamings
			) throws IOException {

		RVMExecutable exec = ExecutionTools.link(rvmProgram, jvm, classRenamings);
		exec.write(rvmProgramLoc, 6);
	}
	
	// Library function to execute a RVMProgram
	// (Interpreter version)

	public IValue executeProgram(
			IConstructor rvmProgram,
			IMap keywordArguments,
			IBool debug,
			IBool debugRVM, 
			IBool testsuite, 
			IBool profile, 
			IBool trace, 
			IBool coverage, 
			IBool jvm,
			IBool verbose,
			IEvaluatorContext ctx
			) throws IOException {
		
		RVMExecutable executable = ExecutionTools.link(rvmProgram, jvm, vf.mapWriter().done());
		if(executable.isValid()){
			RascalExecutionContext rex = null;
			// TODO: the new PathConfig() with only defaults here is syspe
			rex = ExecutionTools.makeRex(new PathConfig(), executable, ctx.getStdOut(), ctx.getStdErr(), debug, debugRVM, testsuite, profile, trace, coverage, jvm, verbose);
			
			rex.getConfiguration().setRascalJavaClassPathProperty(ctx.getConfiguration().getRascalJavaClassPathProperty());
			
			return ExecutionTools.executeProgram(executable, new KWArgs(vf).add(keywordArguments).build(), rex);
		} else {
			throw new IOException("Cannot execute program with errors: " + executable.getErrors().toString());
		}
	}

	// Library function to execute an RVMProgram
	// (Compiler version)

	public IValue executeProgram(
			IConstructor rvmProgram,
			IMap keywordArguments,
			IBool debug,
			IBool debugRVM, 
			IBool testsuite, 
			IBool profile, 
			IBool trace, 
			IBool coverage, 
			IBool jvm,
			IBool verbose,
			RascalExecutionContext rex
			) throws IOException {

		RVMExecutable executable = ExecutionTools.link(rvmProgram, jvm, vf.mapWriter().done());

		if(executable.isValid()){
			RascalExecutionContext rex2 = ExecutionTools.makeRex(rex.getPathConfig(), executable, rex.getStdOut(), rex.getStdErr(), debug, debugRVM, testsuite, profile, trace, coverage, jvm,verbose);
			
			return ExecutionTools.executeProgram(executable, new KWArgs(vf).add(keywordArguments).build(), rex2);
		} else {
			throw new IOException("Cannot execute program with errors: " + executable.getErrors().toString());
		}
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
			IBool trace, 
			IBool coverage,
			IBool jvm,
			IBool verbose,
			IEvaluatorContext ctx
			) throws IOException {
               
	    RVMExecutable executable = ExecutionTools.load(rvmExecutableLoc);
	    if(executable.isValid()){
	        RascalExecutionContext rex = ExecutionTools.makeRex(new PathConfig(), executable, ctx.getStdOut(), ctx.getStdErr(), debug, debugRVM, testsuite, profile, trace, coverage, jvm, verbose);
	        rex.getConfiguration().setRascalJavaClassPathProperty(ctx.getConfiguration().getRascalJavaClassPathProperty());

	        return ExecutionTools.executeProgram(executable, new KWArgs(vf).add(keywordArguments).build(), rex);
	    } else {
	        throw new IOException("Cannot execute program with errors: " + executable.getErrors().toString());
	    }
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
			IBool trace, 
			IBool coverage,
			IBool jvm,
			IBool verbose,
			RascalExecutionContext rex
			) throws IOException {

	    RVMExecutable executable = ExecutionTools.load(rvmExecutableLoc);
		if(executable.isValid()){
			RascalExecutionContext rex2 = ExecutionTools.makeRex(rex.getPathConfig(), executable, rex.getStdOut(), rex.getStdErr(), debug, debugRVM, testsuite, profile, trace, coverage, jvm, verbose);
			
			return ExecutionTools.executeProgram(executable, new KWArgs(vf).add(keywordArguments).build(), rex2);
		} else {
			throw new IOException("Cannot execute program with errors: " + executable.getErrors().toString());
		}
	}

}
