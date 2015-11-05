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
import util::Reflective;
//import experiments::Compiler::Inspect;

RVMModule compile(str moduleName, PathConfig pcfg, bool verbose = false) =
    experiments::Compiler::Compile::compile(moduleName, pcfg, verbose = verbose);
    
RVMProgram compileAndLink(str moduleName,  PathConfig pcfg, bool useJVM=false, bool serialize=true, bool verbose = false) =
     experiments::Compiler::Execute::compileAndLink(moduleName, pcfg, useJVM=useJVM, serialize=serialize, verbose = verbose);

RVMProgram compileAndLinkIncremental(str moduleName,  bool reuseConfig, PathConfig pcfg, bool useJVM=false, bool serialize=true, bool verbose = false) =
     experiments::Compiler::Execute::compileAndLinkIncremental(moduleName, reuseConfig, pcfg, useJVM=useJVM, serialize=serialize, verbose = verbose);
    
value execute(str moduleName, PathConfig pcfg, map[str,value] keywordArguments = (), bool debug=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false,  bool coverage=false, bool useJVM=false, bool serialize=true, bool verbose = false)
     =
     experiments::Compiler::Execute::execute(moduleName, pcfg, keywordArguments = keywordArguments, debug=debug, testsuite=testsuite, recompile=recompile, profile=profile, trackCalls= trackCalls, coverage=coverage, useJVM=useJVM, serialize=serialize, verbose = verbose);
