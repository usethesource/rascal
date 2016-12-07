/** 
 * Copyright (c) 2016, paulklint, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.library.lang.rascal.boot;

import java.io.IOException;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalKeywordParameters;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalModule;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;

@RascalModule("lang::rascal::boot::Kernel")
public interface IKernel {
  /**
   * Compile a Rascal module
   * @param qname   Qualified module name
   * @param srcs    List of source directories
   * @param libs    List of library directories
   * @param boot    Boot directory
   * @param bin     Binary directory
   * @param reloc   Relocate locations in binaries to reloc
   * @param kwArgs  Keyword arguments
   * @return        The result (RVMProgram) of compiling the given module
   */
  public IConstructor compile(IString qname, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, ISourceLocation reloc, KWcompile kwArgs);

  /**
   * Compile a list of Rascal modules
   * @param qnames  List of qualified module names
   * @param srcs    List of source directories
   * @param libs    List of library directories
   * @param boot    Boot directory
   * @param bin     Binary directory
   * @param reloc   Relocate locations in binaries to reloc
   * @param kwArgs  Keyword arguments
   * @return        A list of RVMPrograms
   */
  public IList compile(IList qnames, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, ISourceLocation reloc, KWcompile kwArgs);

  @RascalKeywordParameters
  interface KWcompile {
      KWcompile verbose(boolean val);
      KWcompile optimize(boolean val);
      KWcompile enableAsserts(boolean val);
  }
  KWcompile kw_compile();
  
  /**
   * Recompile the MuLibrary with a new compiler (used only in bootstrapping stages)
   * @param srcs    List of source directories
   * @param libs    List of library directories
   * @param boot    Boot directory
   * @param bin     Binary directory
   * @param kwArgs  Keyword arguments
   */
  public void compileMuLibrary(IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, KWcompileMu kwArgs);

  @RascalKeywordParameters
  interface KWcompileMu {
      KWcompile verbose(boolean val);
      KWcompile jvm(boolean val);
  }
  KWcompileMu kw_compileMu();
  
  /**
   * Regenerate the parser for Rascal itself (used only in bootstrapping stages).
   * Writes in a source directory!
   * @param srcs    List of source directories
   */
  public void bootstrapRascalParser(IList srcs);

  /**
   * Compile and link a Rascal module
   * @param qname   Qualified module name
   * @param srcs    List of source directories
   * @param libs    List of library directories
   * @param boot    Boot directory
   * @param bin     Binary directory
   * @param kwArgs  Keyword arguments
   * @return        The result (RVMProgram) of compiling the given module. The linked version (RVMExecutable) is stored as file.
   */
  public IConstructor compileAndLink(IString qname,  IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, ISourceLocation reloc, KWcompileAndLink kwArgs);

  @RascalKeywordParameters
  interface KWcompileAndLink {
    KWcompileAndLink reloc(ISourceLocation val);
    KWcompileAndLink jvm(boolean val);
    KWcompileAndLink enableAsserts(boolean val);
    KWcompileAndLink verbose(boolean val);
    KWcompileAndLink optimize(boolean val);
  }
  KWcompileAndLink kw_compileAndLink();  
    
  /**
   * Compile and link a list of Rascal modules. The linked version (RVMExecutable) is stored as file.
   * @param qname   List of qualified module names
   * @param srcs    List of source directories
   * @param libs    List of library directories
   * @param boot    Boot directory
   * @param bin     Binary directory
   * @param kwArgs  Keyword arguments
   * @return        A list of resulting RVMExecutables
   */
  public IList compileAndLink(IList qnames,  IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, ISourceLocation reloc, KWcompileAndLink kwArgs);

  /**
   * Incrementally compile and link a Rascal module (used in RascalShell)
   * @param qname   Qualified module name
   * @param reuseConfig 
   *                true if the previous typechcker configuration should be reused
   * @param srcs    List of source directories
   * @param libs    List of library directories
   * @param boot    Boot directory
   * @param bin     Binary directory
   * @param kwArgs  Keyword arguments
   * @return        The compiled and linked (RVMExecutable) version of the given module
   * @throws IOException
   */
  public IConstructor compileAndMergeProgramIncremental(IString qname, IBool reuseConfig, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, KWcompileAndMergeProgramIncremental kwArgs);
    
//  default public RVMExecutable compileAndMergeIncremental(IString qname, IBool reuseConfig, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, KWcompileAndMergeProgramIncremental kwArgs) throws IOException{
//      IConstructor rvmProgram = compileAndMergeProgramIncremental(qname, reuseConfig, srcs, libs, boot, bin, kwArgs);
//      TypeStore typeStore = new TypeStore();
//      return ExecutionTools.link(rvmProgram, ValueFactoryFactory.getValueFactory().bool(true), typeStore);
//  }
  
 // bool jvm=true, bool verbose = false, bool optimize = true
      
  @RascalKeywordParameters
  interface KWcompileAndMergeProgramIncremental {
    KWcompileAndMergeProgramIncremental jvm(boolean val);
    KWcompileAndMergeProgramIncremental verbose(boolean val);
    KWcompileAndMergeProgramIncremental optimize(boolean val);
  } 
  
  KWcompileAndMergeProgramIncremental kw_compileAndMergeProgramIncremental();
  
  /**
   * Get the vocabulary (all names of declared entities) of the current module (used in rascalShell)
   * @param kwArgs   Keyword arguments
   * @return Vocabulary
   */
  public ISet getIncrementalVocabulary(Map<String, IValue> kwArgs);

  /**
   * Run tests in a list of Rascal modules
   * @param qnames  List of qualified module name
   * @param srcs    List of source directories
   * @param libs    List of library directories
   * @param boot    Boot directory
   * @param bin     Binary directory
   * @param recompile Recompile when no binary is found
   * @param kwArgs  Keyword arguments
   * @return        The outcome of the tests
   */
  public IValue rascalTests(IList qnames, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, boolean recompile, KWrascalTests kwArgs);

//  bool debug=false, bool debugRVM=false, bool profile=false, 
//      bool trace= false,  bool coverage=false, bool jvm=true, bool verbose = false
      
  @RascalKeywordParameters
  interface KWrascalTests {
    KWrascalTests debug(boolean val);
    KWrascalTests debugRVM(boolean val);
    KWrascalTests profile(boolean val);
    KWrascalTests trace(boolean val);
    KWrascalTests coverage(boolean val);
    KWrascalTests jvm(boolean val);
    KWrascalTests verbose(boolean val);
  } 
  
  KWrascalTests kw_rascalTests();
  
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
  public IConstructor rascalTestsRaw(IList qnames, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin, boolean recompile, KWrascalTests kwArgs);

  /**
   * Create a module summary to implement IDE features
   * @param qualifiedModuleName   Module name
   * @param srcs    List of source directories
   * @param libs    List of library directories
   * @param boot    Boot directory
   * @param bin     Binary directory
   * @param kwArgs  Keyword arguments
   * @return  Summary for this module
   */
  public IConstructor makeSummary(IString qualifiedModuleName, IList srcs, IList libs, ISourceLocation boot, ISourceLocation bin);

  /**
   * @param summary   A module summary
   * @param use       A use in this module
   * @param kwArgs    Keyword arguments
   * @return All definitions for this use
   */
  public ISet getDefinitions(IConstructor summary, ISourceLocation use);

  /**
   * @param summary   A module summary
   * @param use       A use in this module
   * @param kwArgs    Keyword arguments
   * @return  The type of this use
   */
  public IConstructor getType(IConstructor summary, ISourceLocation use);

  /**
   * @param summary   A module summary
   * @param def       A definition in this module
   * @param kwArgs    Keyword arguments
   * @return All uses of this definition
   */
  public ISet getUses(IConstructor summary, ISourceLocation def);

  /**
   * @param A definition in some module
   * @param kwArgs
   * @return The contents of the doc string of that definition
   */
  public IString getDocForDefinition(ISourceLocation def);
  
}
