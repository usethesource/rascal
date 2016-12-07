package org.rascalmpl.library.lang.rascal.boot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

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
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.TypeStore;

public class Kernel {
  IValueFactory vf;
  private OverloadedFunction compile;
  private OverloadedFunction compileN;
  private OverloadedFunction compileAndLink;
  private OverloadedFunction compileAndLinkN;
  private OverloadedFunction compileAndMergeProgramIncremental;
  private OverloadedFunction compileMuLibrary;
  private OverloadedFunction bootstrapRascalParser;
  private OverloadedFunction rascalTests;
  private OverloadedFunction rascalTestsRaw;

  private final RVMCore rvm;
  private OverloadedFunction makeSummary;
  private OverloadedFunction getDefinitions;
  private OverloadedFunction getType;
  private OverloadedFunction getUses;
  private OverloadedFunction getDocForDefinition;
  private OverloadedFunction getIncrementalVocabulary;

  public Kernel(IValueFactory vf, RascalExecutionContext rex) throws IOException, NoSuchRascalFunction, URISyntaxException {
    this(vf, rex, URIUtil.correctLocation("boot", "", "/"));
  }

//  private OverloadedFunction safeGet(String s) {
//    try {
//      return rvm.getOverloadedFunction(s);
//    }
//    catch (Throwable e) {
//      // For bootstrapping purposes sometimes unused kernel functions will not bind correctly.
//      // If such function is not used, this is ok, but we print a warning here to explain
//      // the subsequent NPE when the function does end up being used:
//      System.err.println("WARNING: ignoring function " + s);
//      e.printStackTrace();
//      return null;
//    }
//  }
        
  public Kernel(IValueFactory vf, RascalExecutionContext rex, ISourceLocation bootDir) throws IOException, NoSuchRascalFunction, URISyntaxException {
    this.vf = vf;		   
    this.rvm = ExecutionTools.initializedRVM(rex.getKernel(), rex);

//    compile    		           = safeGet("RVMModule compile(str qname, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc)");
//    compileN    	           = safeGet("list[RVMModule] compile(list[str] qnames, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc)");
//    compileMuLibrary           = safeGet("void compileMuLibrary(list[loc] srcs, list[loc] libs, loc boot, loc bin)");
//    compileAndLink             = safeGet("RVMProgram compileAndLink(str qname, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc)");
//    compileAndLinkN            = safeGet("list[RVMProgram] compileAndLink(list[str] qnames, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc)");
//    compileAndMergeProgramIncremental
//                               = safeGet("RVMProgram compileAndMergeProgramIncremental(str qname, bool reuseConfig, list[loc] srcs, list[loc] libs, loc boot, loc bin)");
//    getIncrementalVocabulary   = safeGet("ISet getIncrementalVocabulary()");
//
//    rascalTests   	           = safeGet("value rascalTests(list[str] qnames, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool recompile)");
//    rascalTestsRaw             = safeGet("TestResults rascalTestsRaw(list[str] qnames, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool recompile)");
//    makeSummary                = safeGet("ModuleSummary makeSummary(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin)");
//    getDefinitions             = safeGet("set[loc] getDefinitions(ModuleSummary summary, loc use)");
//    getType                    = safeGet("Symbol getType(ModuleSummary summary, loc use)");
//    getUses                    = safeGet("set[loc] getUses(ModuleSummary s, loc def)");
//    getDocForDefinition        = safeGet("str getDocForDefinition(loc def)");

    //		bootstrapRascalParser = safeGet("void bootstrapRascalParser(loc src)");
  }

  /**
   * Compile a Rascal module
   * @param qname		Qualified module name
   * @param srcs		List of source directories
   * @param libs		List of library directories
   * @param boot		Boot directory
   * @param bin		Binary directory
   * @param reloc     Relocate locations in binaries to reloc
   * @param kwArgs	Keyword arguments
   * @return 			The result (RVMProgram) of compiling the given module
   */
  public IConstructor compile(IString qname, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, ISourceLocation reloc, Map<String,IValue> kwArgs){
    return (IConstructor) rvm.executeRVMFunction(compile, new IValue[] { qname, srcs, libs, boot, bin, reloc }, kwArgs);
  }

  /**
   * Compile a list of Rascal modules
   * @param qnames	List of qualified module names
   * @param srcs		List of source directories
   * @param libs		List of library directories
   * @param boot		Boot directory
   * @param bin		Binary directory
   * @param reloc     Relocate locations in binaries to reloc
   * @param kwArgs	Keyword arguments
   * @return 			A list of RVMPrograms
   */
  public IList compile(IList qnames, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, ISourceLocation reloc, Map<String, IValue> kwArgs){
    return (IList) rvm.executeRVMFunction(compileN, new IValue[] { qnames, srcs, libs, boot, bin}, kwArgs);
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
  public void compileMuLibrary(IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, Map<String, IValue> kwArgs) {
    rvm.executeRVMFunction(compileMuLibrary, new IValue[] { srcs, libs, boot, bin }, kwArgs);
  }

  /**
   * Used only in bootstrapping stages to regenerate the parser for Rascal itself. Writes
   * in a source directory!
   * @param srcs
   */
  public void bootstrapRascalParser(IList srcs) {
    rvm.executeRVMFunction(bootstrapRascalParser, new IValue[] { srcs }, null);
  }

  /**
   * Compile and link a Rascal module
   * @param qname		Qualified module name
   * @param srcs		List of source directories
   * @param libs		List of library directories
   * @param boot		Boot directory
   * @param bin		Binary directory
   * @param kwArgs	Keyword arguments
   * @return 			The result (RVMProgram) of compiling the given module. The linked version (RVMExecutable) is stored as file.
   */
  public IConstructor compileAndLink(IString qname,  IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, ISourceLocation reloc, Map<String, IValue> kwArgs){
    return (IConstructor) rvm.executeRVMFunction(compileAndLink, new IValue[] { qname, srcs, libs, boot, bin, reloc }, kwArgs);
  }

  /**
   * 	Compile and link a list of Rascal modules. The linked version (RVMExecutable) is stored as file.
   * @param qname		List of qualified module names
   * @param srcs		List of source directories
   * @param libs		List of library directories
   * @param boot		Boot directory
   * @param bin		Binary directory
   * @param kwArgs	Keyword arguments
   * @return 			A list of resulting RVMExecutables
   */
  public IList compileAndLink(IList qnames,  IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, ISourceLocation reloc, Map<String, IValue> kwArgs){
    return (IList) rvm.executeRVMFunction(compileAndLinkN, new IValue[] { qnames, srcs, libs, boot, bin, reloc }, kwArgs);
  }

  /**
   * Incrementally compile and link a Rascal module (used in RascalShell)
   * @param qname		Qualified module name
   * @param reuseConfig	
   * 					true if the previous typechcker configuration should be reused
   * @param srcs		List of source directories
   * @param libs		List of library directories
   * @param boot		Boot directory
   * @param bin		Binary directory
   * @param kwArgs	Keyword arguments
   * @return 			The compiled and linked (RVMExecutable) version of the given module
   * @throws IOException
   */
  public RVMExecutable compileAndMergeIncremental(IString qname, IBool reuseConfig, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, Map<String, IValue> kwArgs) throws IOException{
    IConstructor rvmProgram = (IConstructor) rvm.executeRVMFunction(compileAndMergeProgramIncremental, new IValue[] { qname, reuseConfig, srcs, libs, boot, bin }, kwArgs);
    TypeStore typeStore = new TypeStore();
    return ExecutionTools.link(rvmProgram, vf.bool(true), typeStore);
  }

  /**
   * Get the vocabulary of the current module (used in rascalShell)
   * @param kwArgs   Keyword arguments
   * @return Vocabulary
   */
  public ISet getIncrementalVocabulary(Map<String, IValue> kwArgs){
    return (ISet) rvm.executeRVMFunction(getIncrementalVocabulary, new IValue[] { }, kwArgs);
  }

  /**
   * 	Run tests in a list of Rascal modules
   * @param qnames	List of qualified module name
   * @param srcs		List of source directories
   * @param libs		List of library directories
   * @param boot		Boot directory
   * @param bin		Binary directory
   * @param recompile Recompile when no binary is found
   * @param kwArgs	Keyword arguments
   * @return 			The outcome of the tests
   */

  public IValue rascalTests(IList qnames, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, boolean recompile, Map<String, IValue> kwArgs){
    return rvm.executeRVMFunction(rascalTests, new IValue[] { qnames, srcs, libs, boot, bin, vf.bool(recompile) }, kwArgs);
  }

  /**
   *  Run tests in a list of Rascal modules
   * @param qnames    List of qualified module name
   * @param srcs      List of source directories
   * @param libs      List of library directories
   * @param boot      Boot directory
   * @param bin       Binary directory
   * @param recompile Recompile when no binary is found
   * @param kwArgs    Keyword arguments
   * @return          The outcome of the tests
   */

  public IConstructor rascalTestsRaw(IList qnames, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, boolean recompile, Map<String, IValue> kwArgs){
    return (IConstructor) rvm.executeRVMFunction(rascalTestsRaw, new IValue[] { qnames, srcs, libs, boot, bin, vf.bool(recompile) }, kwArgs);
  }

  /**
   * @param qualifiedModuleName   Module name
   * @param srcs    List of source directories
   * @param libs    List of library directories
   * @param boot    Boot directory
   * @param bin     Binary directory
   * @param kwArgs  Keyword arguments
   * @return  Summary for this module
   */
  public IConstructor makeSummary(IString qualifiedModuleName, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, Map<String, IValue> kwArgs){
    return (IConstructor) rvm.executeRVMFunction(makeSummary, new IValue[] { qualifiedModuleName, srcs, libs, boot, bin}, kwArgs);
  }

  /**
   * @param summary   A module summary
   * @param use       A use in this module
   * @param kwArgs    Keyword arguments
   * @return All definitions for this use
   */
  public ISet getDefinitions(IConstructor summary, ISourceLocation use, Map<String, IValue> kwArgs){
    return (ISet) rvm.executeRVMFunction(getDefinitions, new IValue[] { summary, use}, kwArgs);
  }

  /**
   * @param summary   A module summary
   * @param use       A use in this module
   * @param kwArgs    Keyword arguments
   * @return  The type of this use
   */
  public IConstructor getType(IConstructor summary, ISourceLocation use, Map<String, IValue> kwArgs){
    return (IConstructor) rvm.executeRVMFunction(getType, new IValue[] { summary, use}, kwArgs);
  }

  /**
   * @param summary   A module summary
   * @param def       A definition in this module
   * @param kwArgs    Keyword arguments
   * @return All uses of this definition
   */
  public ISet getUses(IConstructor summary, ISourceLocation def, Map<String, IValue> kwArgs){
    return (ISet) rvm.executeRVMFunction(getUses, new IValue[] { summary, def}, kwArgs);
  }

  /**
   * @param A definition in some module
   * @param kwArgs
   * @return The contents of the doc string of that definition
   */
  public IString getDocForDefinition(ISourceLocation def, Map<String, IValue> kwArgs){
    return (IString) rvm.executeRVMFunction(getDocForDefinition, new IValue[] { def}, kwArgs);
  }

}
