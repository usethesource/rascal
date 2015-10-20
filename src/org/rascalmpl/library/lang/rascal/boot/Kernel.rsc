module lang::rascal::boot::Kernel

/*
 * The Kernel module collects all Rascal modules that are needed
 * to compile and execute Rascal programs.
 *
 * The Kernel is self-contained and comprehensive: a compiled version of the Kernel
 * is all that is needed for a full bootstrap of Rascal.
 *
 * An up-to-date, compiled version of the Kernel should always reside in the /boot directory 
 * of the Rascal project
 */
 
 
// TODO: it would make sense to use extend here, however compiler does not handle that properly.


import experiments::Compiler::RVM::AST;
import experiments::Compiler::Compile;
import experiments::Compiler::Execute;
//import experiments::Compiler::Inspect;

RVMModule compile(loc moduleLoc, bool verbose = false, loc bindir = |home:///bin|) =
    experiments::Compiler::Compile::compile(moduleLoc, verbose = verbose, bindir = bindir);
    
RVMProgram compileAndLink(loc rascalSource,  bool reuseConfig, bool useJVM=false, bool serialize=true, bool verbose = false, loc bindir = |home:///bin|) =
     experiments::Compiler::Execute::compileAndLink(rascalSource, reuseConfig, useJVM=useJVM, serialize=serialize, verbose = verbose, bindir = bindir);
    
value execute(loc rascalSource, map[str,value] keywordArguments = (), bool debug=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false,  bool coverage=false, bool useJVM=false, bool serialize=true, bool verbose = false, loc bindir = |home:///bin|)
     =
     experiments::Compiler::Execute::execute(rascalSource, keywordArguments = keywordArguments, debug=debug, testsuite=testsuite, recompile=recompile, profile=profile, trackCalls= trackCalls, coverage=coverage, useJVM=useJVM, serialize=serialize, verbose = verbose, bindir = bindir);
