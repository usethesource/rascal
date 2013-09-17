module experiments::Compiler::Execute

import Prelude;

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;

import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::Compile;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;

import experiments::Compiler::muRascal2RVM::mu2rvm;
import experiments::Compiler::muRascal2RVM::StackSize;

public loc MuLibrary = |std:///experiments/Compiler/muRascal2RVM/Library.mu|;
public loc MuLibraryCompiled = |std:///experiments/Compiler/muRascal2RVM/Library.rvm|;

list[Declaration] parseMuLibrary(){
    println("rascal2rvm: Recompiling library.mu");
 	libModule = parse(MuLibrary);
 	functions = [];
 
  	for(fun <- libModule.functions){
  	    required_frame_size = fun.nlocals + estimate_stack_size(fun.body);
    	functions += FUNCTION(fun.qname, fun.ftype, fun.scopeIn, fun.nformals, fun.nlocals, required_frame_size, tr(fun.body),());
  	}
  
  	writeTextValueFile(MuLibraryCompiled, functions);
    println("rascal2rvm: Writing compiled version of Library.mu");
  	
  	return functions; 
}

tuple[value, num] execute_and_time(RVMProgram rvmProgram, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
   imported_functions = [];
   
    if(exists(MuLibraryCompiled) && lastModified(MuLibraryCompiled) > lastModified(MuLibrary)){
     try {
  	       imported_functions = readTextValueFile(#list[Declaration], MuLibraryCompiled);
  	       println("rascal2rvm: Using compiled version of Library.mu");
  	 } catch:
  	       imported_functions = parseMuLibrary();
   } else {
     imported_functions = parseMuLibrary();
   }
  
   for(imp <- rvmProgram.imports){
      importedLoc = imp.parent + (basename(imp) + ".rvm");
       try {
  	       importedRvmProgram = readTextValueFile(#RVMProgram, importedLoc);
  	       imported_functions += [ importedRvmProgram.declarations[fname] | fname <-importedRvmProgram.declarations ];
  	   } catch x: println("rascal2rvm: Reading <importedLoc> did not succeed: <x>");      
   }
   <v, t> = executeProgram(rvmProgram, imported_functions, debug, 1, testsuite);
   println("Result = <v>, [<t> msec]");
   return <v, t>;
}

value execute(RVMProgram rvmProgram, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
	<v, t> = execute_and_time(rvmProgram, debug=debug, listing=listing, testsuite=testsuite,recompile=recompile);
	return v;
}

value execute(loc rascalSource, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute(rvmProgram, debug=debug, testsuite=testsuite);
}

value execute(str rascalSource, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute(rvmProgram, debug=debug, testsuite=testsuite);
}

tuple[value, num] execute_and_time(loc rascalSource, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute_and_time(rvmProgram, debug=debug, testsuite=testsuite);
}