module lang::rascal::boot::Kernel
 
/*
 * The Kernel module collects all Rascal modules that are needed
 * to compile and execute Rascal programs.
 *
 * The Kernel is self-contained and comprehensive: a compiled version of the Kernel
 * is all that is needed for a full bootstrap of Rascal.
 *
 * An up-to-date, compiled version of the Kernel should always reside in the /boot directory 
 * of the Rascal project.
 */
 
extend experiments::Compiler::Compile;
extend experiments::Compiler::Execute;
extend experiments::Compiler::CompileMuLibrary;
extend lang::rascal::grammar::Bootstrap;

//import util::Reflective;
//import experiments::Compiler::RVM::AST;
//
//RVMModule compile(loc moduleLoc, PathConfig pcfg, bool verbose = false) =
//    experiments::Compiler::Compile::compile(moduleLoc, pcfg, verbose=verbose);
//
//RVMModule compile(str qualifiedModuleName, PathConfig pcfg, bool verbose = false) =
//    experiments::Compiler::Compile::compile(qualifiedModuleName, pcfg, verbose=verbose);
//    
//RVMModule compile(str qualifiedModuleName, list[loc] srcPath, list[loc] libPath, loc bootDir, loc binDir, bool verbose = false) =
//    experiments::Compiler::Compile::compile(qualifiedModuleName, srcPath, libPath, bootDir, binDir, verbose=verbose);
//
//RVMModule compileIncremental(str qualifiedModuleName, bool reuseConfig, PathConfig pcfg, bool verbose = false) =
//    experiments::Compiler::Compile::compileIncremental(qualifiedModuleName, reuseConfig, pcfg, verbose=verbose);
//    
//RVMProgram compileAndLink(str qualifiedModuleName, PathConfig pcfg, bool jvm=true, bool verbose = false) =
//    experiments::Compiler::Execute::compileAndLink(qualifiedModuleName, pcfg, jvm=jvm, verbose=verbose);
//    
//list[RVMDeclaration] compileMuLibrary(PathConfig pcfg, bool verbose = false, bool jvm=true) =
//    experiments::Compiler::CompileMuLibrary::compileMuLibrary(pcfg, verbose=verbose,jvm=jvm);
