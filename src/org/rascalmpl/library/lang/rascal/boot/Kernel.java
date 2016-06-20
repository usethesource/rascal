package org.rascalmpl.library.lang.rascal.boot;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class Kernel {
	private static final String PATH_TO_LINKED_KERNEL = "lang/rascal/boot/Kernel.rvm.ser.gz";
    IValueFactory vf;
	private OverloadedFunction compile;
	private OverloadedFunction compileN;
	private OverloadedFunction compileAndLink;
	private OverloadedFunction compileAndLinkN;
	private OverloadedFunction compileAndMergeIncremental;
	private OverloadedFunction compileMuLibrary;
	private OverloadedFunction bootstrapRascalParser;
	private OverloadedFunction rascalTests;
	
	private final RVMCore rvm;

	public Kernel(IValueFactory vf, RascalExecutionContext rex) throws IOException, NoSuchRascalFunction, URISyntaxException {
	    this(vf, rex, URIUtil.correctLocation("boot", "", "/"));
	}
        
	public Kernel(IValueFactory vf, RascalExecutionContext rex, ISourceLocation binaryKernelLoc) throws IOException, NoSuchRascalFunction, URISyntaxException {
		this.vf = vf;
	
		if (!binaryKernelLoc.getScheme().startsWith("compressed")) {
		    binaryKernelLoc = URIUtil.changeScheme(binaryKernelLoc, "compressed+" + binaryKernelLoc.getScheme());
		}
		
		if (!binaryKernelLoc.getPath().endsWith(PATH_TO_LINKED_KERNEL)) {
		    binaryKernelLoc = URIUtil.getChildLocation(binaryKernelLoc, PATH_TO_LINKED_KERNEL);
		}
		   
		this.rvm = ExecutionTools.initializedRVM(binaryKernelLoc, rex);

		compile    		= rvm.getOverloadedFunction("RVMModule compile(str qname, list[loc] srcs, list[loc] libs, loc boot, loc bin)");
		compileN    	= rvm.getOverloadedFunction("list[RVMModule] compile(list[str] qnames, list[loc] srcs, list[loc] libs, loc boot, loc bin)");
		compileMuLibrary= rvm.getOverloadedFunction("void compileMuLibrary(list[loc] srcs, list[loc] libs, loc boot, loc bin)");
		compileAndLink  = rvm.getOverloadedFunction("RVMProgram compileAndLink(str qname, list[loc] srcs, list[loc] libs, loc boot, loc bin)");
		compileAndLinkN = rvm.getOverloadedFunction("list[RVMProgram] compileAndLink(list[str] qnames, list[loc] srcs, list[loc] libs, loc boot, loc bin)");
		compileAndMergeIncremental 
						= rvm.getOverloadedFunction("RVMProgram compileAndMergeIncremental(str qname, bool reuseConfig, list[loc] srcs, list[loc] libs, loc boot, loc bin)");
		rascalTests   	= rvm.getOverloadedFunction("value rascalTests(list[str] qnames, list[loc] srcs, list[loc] libs, loc boot, loc bin)");
//		bootstrapRascalParser = rvm.getOverloadedFunction("void bootstrapRascalParser(loc src)");
	}
	
	/**
	 * Compile a Rascal module
	 * @param qname		Qualified module name
	 * @param srcs	List of source directories
	 * @param libs	List of library directories
	 * @param boot	Boot directory
	 * @param bin	Binary directory
	 * @param kwArgs	Keyword arguments
	 * @return The result (RVMProgram) of compiling the given module
	 */
	public IConstructor compile(IString qname, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, IMap kwArgs){
	  return (IConstructor) rvm.executeRVMFunction(compile, new IValue[] { qname, srcs, libs, boot, bin, kwArgs });
	}
	
	/**
	 * Compile a list of Rascal modules
	 * @param qnames	List of qualified module names
	 * @param srcs	List of source directories
	 * @param libs	List of library directories
	 * @param boot	Boot directory
	 * @param bin	Binary directory
	 * @param kwArgs	Keyword arguments
	 * @return A list of RVMPrograms
	 */
	public IList compile(IList qnames, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, IMap kwArgs){
		return (IList) rvm.executeRVMFunction(compileN, new IValue[] { qnames, srcs, libs, boot, bin, kwArgs });
	}
	
	/**
	 * Used only in bootstrapping stages to recompile the MuLibrary with a new compiler.
	 * 
	 * @param srcs
	 * @param libs
	 * @param boot
	 * @param bin
	 * @param kwArgs
	 */
	public void compileMuLibrary(IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, IMap kwArgs) {
	    rvm.executeRVMFunction(compileMuLibrary, new IValue[] { srcs, libs, boot, bin, kwArgs });
	}
	
	/**
	 * Used only in bootstrapping stages to regenerate the parser for Rascal itself. Writes
	 * in a source directory!
	 * @param srcs
	 */
	public void bootstrapRascalParser(IList srcs) {
	    rvm.executeRVMFunction(bootstrapRascalParser, new IValue[] { srcs });
	}
	
	/**
	 * 	Compile and link a Rascal module
	 * @param qname		Qualified module name
	 * @param srcs	List of source directories
	 * @param libs	List of library directories
	 * @param boot	Boot directory
	 * @param bin	Binary directory
	 * @param kwArgs	Keyword arguments
	 * @return The result (RVMProgram) of compiling the given module. The linked version (RVMExecutable) is stored as file.
	 */
	public IConstructor compileAndLink(IString qname,  IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin,  IMap kwArgs){
		return (IConstructor) rvm.executeRVMFunction(compileAndLink, new IValue[] { qname, srcs, libs, boot, bin, kwArgs });
	}
	
	/**
	 * 	Compile and link a list of Rascal modules. The linked version (RVMExecutable) is stored as file.
	 * @param qname		List of qualified module names
	 * @param srcs	List of source directories
	 * @param libs	List of library directories
	 * @param boot	Boot directory
	 * @param bin	Binary directory
	 * @param kwArgs	Keyword arguments
	 * @return A list of resulting RVMExecutables
	 */
	public IList compileAndLink(IList qnames,  IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin,  IMap kwArgs){
		return (IList) rvm.executeRVMFunction(compileAndLinkN, new IValue[] { qnames, srcs, libs, boot, bin, kwArgs });
	}
	
	/**
	 * Incrementally compile and link a Rascal module (used in RascalShell)
	 * @param qname			Qualified module name
	 * @param reuseConfig	true if the previous typechcker configuration should be reused
	 * @param srcs		List of source directories
	 * @param libs		List of library directories
	 * @param boot		Boot directory
	 * @param bin		Binary directory
	 * @param kwArgs		Keyword arguments
	 * @return The compiled and linked (RVMExecutable) version of the given module
	 * @throws IOException
	 */
	public RVMExecutable compileAndMergeIncremental(IString qname, IBool reuseConfig, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, IMap kwArgs) throws IOException{
		IConstructor rvmProgram = (IConstructor) rvm.executeRVMFunction(compileAndMergeIncremental, new IValue[] { qname, reuseConfig, srcs, libs, boot, bin, kwArgs });
		return ExecutionTools.link(rvmProgram, vf.bool(true));
	}
	
	/**
	 * 	Run tests in a list of Rascal modules
	 * @param qnames	List of qualified module name
	 * @param srcs	List of source directories
	 * @param libs	List of library directories
	 * @param boot	Boot directory
	 * @param bin	Binary directory
	 * @param kwArgs	Keyword arguments
	 * @return The outcome of the tests
	 */
	
	public IValue rascalTests(IList qnames, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, IMap kwArgs){
		return rvm.executeRVMFunction(rascalTests, new IValue[] { qnames, srcs, libs, boot, bin, kwArgs });
	}

  
}
