package org.rascalmpl.library.lang.rascal.boot;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
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
	private final OverloadedFunction compile;
	private final OverloadedFunction compileAndLink;
	private final OverloadedFunction compileAndLinkIncremental;
	private final OverloadedFunction rascalTests;
	
	private final RVMCore rvm;

	public Kernel(IValueFactory vf, RascalExecutionContext rex){
		rvm    			= ExecutionTools.initializedRVM("compressed+boot", "lang/rascal/boot/Kernel.rvm.ser.gz", rex);
		
		compile    		= rvm.getOverloadedFunction("RVMModule compile(str qname, list[loc] srcPath, list[loc] libPath, loc bootDir, loc binDir)");
		compileAndLink  = rvm.getOverloadedFunction("RVMProgram compileAndLink(str qname, list[loc] srcPath, list[loc] libPath, loc bootDir, loc binDir)");
		compileAndLinkIncremental	
						= rvm.getOverloadedFunction("RVMProgram compileAndLinkIncremental(str qname, bool reuseConfig)");
		rascalTests   	= rvm.getOverloadedFunction("value rascalTests(list[str] qnames, list[loc] srcPath, list[loc] libPath, loc bootDir, loc binDir)");
	}
	
	public IConstructor compile(IString qname, IList srcPath, IList libPath, ISourceLocation bootDir, ISourceLocation binDir, IMap kwArgs){
		return (IConstructor) rvm.executeRVMFunction(compile, new IValue[] { qname, srcPath, libPath, bootDir, binDir, kwArgs });
	}
	
	public IConstructor compileAndLink(IString qname,  IList srcPath, IList libPath, ISourceLocation bootDir, ISourceLocation binDir,  IMap kwArgs){
		return (IConstructor) rvm.executeRVMFunction(compileAndLink, new IValue[] { qname, srcPath, libPath, bootDir, binDir, kwArgs });
	}
	
	public IConstructor compileAndLinkIncremental(IString qname, IBool reuseConfig, IMap kwArgs){
		return (IConstructor) rvm.executeRVMFunction(compileAndLinkIncremental, new IValue[] { qname, reuseConfig, kwArgs });
	}
	
	public IValue rascalTests(IList qnames, IList srcPath, IList libPath, ISourceLocation bootDir, ISourceLocation binDir, IMap kwArgs){
		return rvm.executeRVMFunction(rascalTests, new IValue[] { qnames, srcPath, libPath, bootDir, binDir, kwArgs });
	}
}
