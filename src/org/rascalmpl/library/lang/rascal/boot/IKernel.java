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

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalKeywordParameters;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalModule;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;

/**
 * IKernel provides access to the kernel functionality of the Rascal system
 * including compiling, linking, and testing Rascal modules and
 * extracting information from them for use in IDE or REPL (use/def, vocabulary).
 */
@RascalModule("lang::rascal::boot::Kernel")
public interface IKernel extends IJava2Rascal {
  /**
   * Compile a Rascal module
   * @param qname   Qualified module name
   * @param pcfg    PathConfig
   * @param kwArgs  Keyword arguments
   * @return        The result (RVMProgram) of compiling the given module
   */
  public IConstructor compile(IString qname, IConstructor pcfg, KWcompile kwArgs);
  
  /**
   * Compile a group of Rascal modules
   * @param moduleRoot   folder to compile all modules from, has to be one of the source roots 
   *                     in pcfg or a sub-path of one of these
   * @param pcfg         PathConfig
   * @param kwArgs       Keyword arguments
   * @return             A list of RVMPrograms
   */
  public IList compileAll(ISourceLocation moduleRoot, IConstructor pcfg, KWcompile kwArgs);

  /**
   * Compile a list of Rascal modules
   * @param qnames  List of qualified module names
   * @param pcfg    PathConfig
   * @param kwArgs  Keyword arguments
   * @return        A list of RVMPrograms
   */
  public IList compile(IList qnames, IConstructor pcfg, KWcompile kwArgs);

  @RascalKeywordParameters
  interface KWcompile {
      KWcompile verbose(boolean val);
      KWcompile reloc(ISourceLocation val);
      KWcompile optimize(boolean val);
      KWcompile enableAsserts(boolean val);
  }
  KWcompile kw_compile();
  
  /**
   * Recompile the MuLibrary with a new compiler (used only in bootstrapping stages)
   * @param pcfg    PathConfig
   * @param kwArgs  Keyword arguments
   */
  public void compileMuLibrary(IConstructor pcfg, KWcompileMu kwArgs);

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
   * @param pcfg    PathConfig
   * @param kwArgs  Keyword arguments
   * @return        The result (RVMProgram) of compiling the given module. The linked version (RVMExecutable) is stored as file.
   */
  public IConstructor compileAndLink(IString qname, IConstructor pcfg, KWcompileAndLink kwArgs);

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
   * @param pcfg    PathConfig
   * @param kwArgs  Keyword arguments
   * @return        A list of resulting RVMExecutables
   */
  public IList compileAndLink(IList qnames, IConstructor pcfg, KWcompileAndLink kwArgs);

  /**
   * Incrementally compile and link a Rascal module (used in RascalShell)
   * @param qname   Qualified module name
   * @param reuseConfig 
   *                true if the previous typechcker configuration should be reused
   * @param pcfg    PathConfig
   * @param kwArgs  Keyword arguments
   * @return        The compiled and linked (RVMExecutable) version of the given module
   * @throws IOException
   */
  public IConstructor compileAndMergeProgramIncremental(IString qname, IBool reuseConfig, IConstructor pcfg, KWcompileAndMergeProgramIncremental kwArgs);
      
  @RascalKeywordParameters
  interface KWcompileAndMergeProgramIncremental {
    KWcompileAndMergeProgramIncremental jvm(boolean val);
    KWcompileAndMergeProgramIncremental optimize(boolean val);
    KWcompileAndMergeProgramIncremental verbose(boolean val);
  } 
  
  KWcompileAndMergeProgramIncremental kw_compileAndMergeProgramIncremental();
  
  /**
   * Get the vocabulary (all names of declared entities) of the current module (used in rascalShell)
   * @return Vocabulary
   */
  public ISet getIncrementalVocabulary();

  /**
   * Run tests in a list of Rascal modules
   * @param qnames  List of qualified module name
   * @param pcfg    PathConfig
   * @param kwArgs  Keyword arguments
   * @return        The outcome of the tests
   */
  public IValue rascalTests(IList qnames, IConstructor pcfg, KWrascalTests kwArgs);
      
  @RascalKeywordParameters
  interface KWrascalTests {
    KWrascalTests coverage(boolean val);
    KWrascalTests jvm(boolean val);
    KWrascalTests debug(boolean val);
    KWrascalTests debugRVM(boolean val);
    KWrascalTests profile(boolean val);
    KWrascalTests recompile(boolean val);
    KWrascalTests trace(boolean val);
    KWrascalTests verbose(boolean val);
  } 
  
  KWrascalTests kw_rascalTests();
  
  /**
   *  Run tests in a list of Rascal modules
   * @param qnames    List of qualified module name
   * @param pcfg      PathConfig
   * @param kwArgs    Keyword arguments
   * @return          The outcome of the tests
   */
  public IConstructor rascalTestsRaw(IList qnames, IConstructor pcfg, KWrascalTests kwArgs);

  /**
   * Create a module summary to implement IDE features
   * @param qname     Module name
   * @param pcfg      PathConfig
   * @param kwArgs    Keyword arguments
   * @return          Summary for this module
   */
  public IConstructor makeSummary(IString qname, IConstructor pcfg);

  /**
   * @param summary   A module summary
   * @param use       A use in this module
   * @param kwArgs    Keyword arguments
   * @return          All definitions for this use
   */
  public ISet getDefinitions(IConstructor summary, ISourceLocation use);

  /**
   * @param summary   A module summary
   * @param use       A use in this module
   * @param kwArgs    Keyword arguments
   * @return          The type of this use
   */
  public IConstructor getType(IConstructor summary, ISourceLocation use);

  /**
   * @param summary   A module summary
   * @param def       A definition in this module
   * @param kwArgs    Keyword arguments
   * @return          All uses of this definition
   */
  public ISet getUses(IConstructor summary, ISourceLocation def);

  /**
   * @param def       A definition in some module
   * @return          The contents of the doc string of that definition
   */
  public IString getDocForDefinition(ISourceLocation def);
  
  /*
   * Some Kernel management methods
   */
  
  /**
   * Set an IFrameObserver on this Kernel; allows external debugging
   * @param observer  Observer to be added
   */
  public void setFrameObserver(IFrameObserver observer);
  
  /**
   * Shutdown this kernel (gives opportunity for observers to report etc)
   */
  public void shutdown();
}
