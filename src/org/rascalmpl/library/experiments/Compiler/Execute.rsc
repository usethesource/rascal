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
import experiments::Compiler::muRascal2RVM::PeepHole;

public loc MuLibrary = |rascal:///experiments/Compiler/muRascal2RVM/Library.mu|;
public loc MuLibraryCompiled = |rascal:///experiments/Compiler/muRascal2RVM/Library.rvm|;

list[Declaration] parseMuLibrary(){
    println("rascal2rvm: Recompiling library.mu");
 	libModule = parse(MuLibrary);
 	functions = [];
 
  	for(fun <- libModule.functions) {
  		functionScope = fun.qname;
  		set_nlocals(fun.nlocals);
  	    body = peephole(tr(fun.body));
  	    required_frame_size = get_nlocals() + estimate_stack_size(fun.body);
    	functions += (fun is muCoroutine) ? COROUTINE(fun.qname, fun.scopeIn, fun.nformals, get_nlocals(), fun.refs, required_frame_size, body)
    									  : FUNCTION(fun.qname, fun.ftype, fun.scopeIn, fun.nformals, get_nlocals(), required_frame_size, body,[]);
  	}
  
  	writeTextValueFile(MuLibraryCompiled, functions);
    println("rascal2rvm: Writing compiled version of Library.mu");
  	
  	return functions; 
}

tuple[value, num] execute_and_time(RVMProgram rvmProgram, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
   imported_types = ();
   imported_functions = [];
   imported_overloaded_functions = [];
   imported_overloading_resolvers = ();
   imported_grammars = ();
   
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
      println("importing: <imp>");
      importedLoc = imp.parent + (basename(imp) + ".rvm");
       try {
  	       importedRvmProgram = readTextValueFile(#RVMProgram, importedLoc);
  	       imported_types = imported_types + importedRvmProgram.types;
  	       imported_functions += [ importedRvmProgram.declarations[fname] | fname <-importedRvmProgram.declarations ];
  	       
  	       // We need to merge overloading resolvers regarding overloaded function indices
  	       pos_delta = size(imported_overloaded_functions); 
  	       imported_overloaded_functions = imported_overloaded_functions + importedRvmProgram.overloaded_functions;
  	       imported_overloading_resolvers = imported_overloading_resolvers + ( ofname : importedRvmProgram.resolver[ofname] + pos_delta | str ofname <- importedRvmProgram.resolver );
  	       
  	       imported_grammars[importedRvmProgram.name] = importedRvmProgram.grammar;
  	       println("adding grammar for <importedRvmProgram.name>");
  	   } catch x: println("rascal2rvm: Reading <importedLoc> did not succeed: <x>");      
   }
   
   pos_delta = size(imported_overloaded_functions);
   rvmProgram.resolver = ( ofname : rvmProgram.resolver[ofname] + pos_delta | str ofname <- rvmProgram.resolver );
   <v, t> = executeProgram(rvmProgram, imported_types,
   									   imported_functions, 
   									   imported_overloaded_functions, imported_overloading_resolvers, 
   									   imported_grammars, arguments, debug, testsuite);
   println("Result = <v>, [<t> msec]");
   return <v, t>;
}

value execute(RVMProgram rvmProgram, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
	<v, t> = execute_and_time(rvmProgram, arguments, debug=debug, listing=listing, testsuite=testsuite,recompile=recompile);
	return v;
}

value execute(loc rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute(rvmProgram, arguments, debug=debug, testsuite=testsuite);
}

value execute(str rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute(rvmProgram, arguments, debug=debug, testsuite=testsuite);
}

tuple[value, num] execute_and_time(loc rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute_and_time(rvmProgram, arguments, debug=debug, testsuite=testsuite);
}