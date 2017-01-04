package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.interpreter.IEvaluatorContext;  // TODO: remove import? NOT YET: Only used as argument of reflective library function
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;


public class ExecuteProgram {
	IValueFactory vf;
	
	public ExecuteProgram(IValueFactory vf) {
		this.vf = vf;
	}
	
	// Library function to serialize a RVMProgram

	public void linkAndSerializeProgram(
			ISourceLocation rvmProgramLoc,
			IConstructor rvmProgram,
			IBool jvm
			) throws IOException {

		RVMExecutable exec = ExecutionTools.link(rvmProgram, jvm);
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
			IEvaluatorContext ctx
			) throws IOException {
		
		RVMExecutable executable = ExecutionTools.link(rvmProgram, jvm);
		if(executable.isValid()){
			RascalExecutionContext rex = null;
            try {
                // TODO: the new PathConfig() with only defaults here is syspe
                rex = ExecutionTools.makeRex(new PathConfig(), executable, ctx.getStdOut(), ctx.getStdErr(), debug, debugRVM, testsuite, profile, trace, coverage, jvm);
            }
            catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			
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
			RascalExecutionContext rex
			) throws IOException {

		RVMExecutable executable = ExecutionTools.link(rvmProgram, jvm);

		if(executable.isValid()){
			RascalExecutionContext rex2 = ExecutionTools.makeRex(rex.getPathConfig(), executable, rex.getStdOut(), rex.getStdErr(), debug, debugRVM, testsuite, profile, trace, coverage, jvm);
			
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
			IEvaluatorContext ctx
			) throws IOException {
               
	    RVMExecutable executable = ExecutionTools.load(rvmExecutableLoc);
	    if(executable.isValid()){
	        RascalExecutionContext rex = null;
	        try {
	            rex = ExecutionTools.makeRex(new PathConfig(), executable, ctx.getStdOut(), ctx.getStdErr(), debug, debugRVM, testsuite, profile, trace, coverage, jvm);
	        }
	        catch (URISyntaxException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
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
			RascalExecutionContext rex
			) throws IOException {

	    RVMExecutable executable = ExecutionTools.load(rvmExecutableLoc);
		if(executable.isValid()){
			RascalExecutionContext rex2 = ExecutionTools.makeRex(rex.getPathConfig(), executable, rex.getStdOut(), rex.getStdErr(), debug, debugRVM, testsuite, profile, trace, coverage, jvm);
			
			return ExecutionTools.executeProgram(executable, new KWArgs(vf).add(keywordArguments).build(), rex2);
		} else {
			throw new IOException("Cannot execute program with errors: " + executable.getErrors().toString());
		}
	}

}
