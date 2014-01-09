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
    									  : FUNCTION(fun.qname, fun.ftype, fun.scopeIn, fun.nformals, get_nlocals(), false, required_frame_size, body,[]);
  	}
  
  	writeTextValueFile(MuLibraryCompiled, functions);
    println("rascal2rvm: Writing compiled version of Library.mu");
  	
  	return functions; 
}

tuple[value, num] execute_and_time(RVMProgram rvmProgram, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false){
   map[str,Symbol] imported_types = ();
   list[Declaration] imported_functions = [];
   lrel[str,list[str],list[str]] imported_overloaded_functions = [];
   map[str,int] imported_overloading_resolvers = ();
   
    if(exists(MuLibraryCompiled) && lastModified(MuLibraryCompiled) > lastModified(MuLibrary)){
     try {
  	       imported_functions = readTextValueFile(#list[Declaration], MuLibraryCompiled);
  	       println("rascal2rvm: Using compiled version of Library.mu");
  	 } catch:
  	       imported_functions = parseMuLibrary();
   } else {
     imported_functions = parseMuLibrary();
   }
   
   set[loc] processed = {};
   void processImports(RVMProgram rvmProgram) {
       for(imp <- rvmProgram.imports, imp notin processed) {
           println("importing: <imp>");
           processed += imp;
           importedLoc = imp.parent + (basename(imp) + ".rvm");
           try {
  	           importedRvmProgram = readTextValueFile(#RVMProgram, importedLoc);
  	           
  	           // Temporary work around related to issue #343
  	           importedRvmProgram = visit(importedRvmProgram) { case type[value] t => type(t.symbol,t.definitions) }
  	           
  	           processImports(importedRvmProgram);
  	          
  	           imported_types = imported_types + importedRvmProgram.types;
  	           imported_functions += [ importedRvmProgram.declarations[fname] | fname <-importedRvmProgram.declarations ];
  	       
  	           // We need to merge overloading resolvers regarding overloaded function indices
  	           pos_delta = size(imported_overloaded_functions); 
  	           imported_overloaded_functions = imported_overloaded_functions + importedRvmProgram.overloaded_functions;
  	           imported_overloading_resolvers = imported_overloading_resolvers + ( ofname : (importedRvmProgram.resolver[ofname] + pos_delta) | str ofname <- importedRvmProgram.resolver );
  	       
  	       } catch x: println("rascal2rvm: Reading <importedLoc> did not succeed: <x>");      
       }
   }
   
   processImports(rvmProgram);
   
   pos_delta = size(imported_overloaded_functions);
   rvmProgram.resolver = ( ofname : rvmProgram.resolver[ofname] + pos_delta | str ofname <- rvmProgram.resolver );
   <v, t> = executeProgram(rvmProgram, imported_types,
   									   imported_functions, 
   									   imported_overloaded_functions, imported_overloading_resolvers, 
   									   arguments, debug, testsuite, profile);
   println("Result = <v>, [<t> msec]");
   return <v, t>;
}

value execute(RVMProgram rvmProgram, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false){
	<v, t> = execute_and_time(rvmProgram, arguments, debug=debug, listing=listing, testsuite=testsuite,recompile=recompile, profile=profile);
	return v;
}

value execute(loc rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute(rvmProgram, arguments, debug=debug, testsuite=testsuite,profile=profile);
}

value execute(str rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute(rvmProgram, arguments, debug=debug, testsuite=testsuite,profile=profile);
}

tuple[value, num] execute_and_time(loc rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute_and_time(rvmProgram, arguments, debug=debug, testsuite=testsuite, profile=profile);
}

str makeTestSummary(lrel[loc,int,str] test_results) = "<size(test_results)> tests executed; < size(test_results[_,0])> failed; < size(test_results[_,2])> ignored";

void printTestReport(value results){
  if(lrel[loc,int,str] test_results := results){
	  failed = test_results[_,0];
	  if(size(failed) > 0){
		  println("\nFAILED TESTS:");
		  for(<l, 0, msg> <- test_results){
		      println("<l>: FALSE <msg>");
		  }
	  }
	  ignored = test_results[_,2];
	  if(size(ignored) > 0){
		  println("\nIGNORED TESTS:");
		  for(<l, 2, msg> <- test_results){
		      println("<l>: IGNORED");
		  }
	  }
	  println("\nSUMMARY: " + makeTestSummary(test_results));
  } else {
    throw "cannot create report for test results: <resuls>";
  }
}
