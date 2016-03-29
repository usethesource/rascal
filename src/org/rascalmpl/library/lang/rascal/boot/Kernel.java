package org.rascalmpl.library.lang.rascal.boot;

import java.io.IOException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class Kernel {
	IValueFactory vf;
	private OverloadedFunction compile;
	private OverloadedFunction compileAndLink;
	private OverloadedFunction compileAndMergeIncremental;
	private OverloadedFunction rascalTests;
	
	private RVMCore rvm;

	public Kernel(IValueFactory vf, RascalExecutionContext rex){
		this.vf = vf;
		if(rvm == null){
			rvm = ExecutionTools.initializedRVM("compressed+boot", "lang/rascal/boot/Kernel.rvm.ser.gz", rex);
		}
		try {
			compile    		= rvm.getOverloadedFunction("RVMModule compile(str qname, list[loc] srcPath, list[loc] libPath, loc bootDir, loc binDir)");
			compileAndLink  = rvm.getOverloadedFunction("RVMProgram compileAndLink(str qname, list[loc] srcPath, list[loc] libPath, loc bootDir, loc binDir)");
			compileAndMergeIncremental	
							= rvm.getOverloadedFunction("RVMProgram compileAndMergeIncremental(str qname, bool reuseConfig)");
			rascalTests   	= rvm.getOverloadedFunction("value rascalTests(list[str] qnames, list[loc] srcPath, list[loc] libPath, loc bootDir, loc binDir)");
		} catch (NoSuchRascalFunction e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compile a Rascal module
	 * @param qname		Qualified module name
	 * @param srcPath	List of source directories
	 * @param libPath	List of library directories
	 * @param bootDir	Boot directory
	 * @param binDir	Binary directory
	 * @param kwArgs	Keyword arguments
	 * @return The result (RVMProgram) of compiling the given module
	 */
	public IConstructor compile(IString qname, IList srcPath, IList libPath, ISourceLocation bootDir, ISourceLocation binDir, IMap kwArgs){
		return (IConstructor) rvm.executeRVMFunction(compile, new IValue[] { qname, srcPath, libPath, bootDir, binDir, kwArgs });
	}
	
	/**
	 * 	Compile and link a Rascal module
	 * @param qname		Qualified module name
	 * @param srcPath	List of source directories
	 * @param libPath	List of library directories
	 * @param bootDir	Boot directory
	 * @param binDir	Binary directory
	 * @param kwArgs	Keyword arguments
	 * @return The result (RVMProgram) of compiling the given module. The linked version (RVMExecutable) is stored as file.
	 */
	public IConstructor compileAndLink(IString qname,  IList srcPath, IList libPath, ISourceLocation bootDir, ISourceLocation binDir,  IMap kwArgs){
		return (IConstructor) rvm.executeRVMFunction(compileAndLink, new IValue[] { qname, srcPath, libPath, bootDir, binDir, kwArgs });
	}
	
	/**
	 * Incrementally compile and link a Rascal module (used in RascalShell)
	 * @param qname			Qualified module name
	 * @param reuseConfig	true if the previous typechcker configuration should be reused
	 * @param kwArgs		Keyword arguments
	 * @return The compiled and linked (RVMExecutable) version of the given module
	 * @throws IOException
	 */
	public RVMExecutable compileAndLinkIncremental(IString qname, IBool reuseConfig, IMap kwArgs) throws IOException{
		IConstructor rvmProgram = (IConstructor) rvm.executeRVMFunction(compileAndMergeIncremental, new IValue[] { qname, reuseConfig, kwArgs });
		return ExecutionTools.link(rvmProgram, vf.bool(true));
	}
	
	/**
	 * 	Run tests in a list of Rascal modules
	 * @param qnames	List of qualified module name
	 * @param srcPath	List of source directories
	 * @param libPath	List of library directories
	 * @param bootDir	Boot directory
	 * @param binDir	Binary directory
	 * @param kwArgs	Keyword arguments
	 * @return The outcome of the tests
	 */
	
	public IValue rascalTests(IList qnames, IList srcPath, IList libPath, ISourceLocation bootDir, ISourceLocation binDir, IMap kwArgs){
		return rvm.executeRVMFunction(rascalTests, new IValue[] { qnames, srcPath, libPath, bootDir, binDir, kwArgs });
	}
}
