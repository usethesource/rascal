module experiments::Compiler::Execute

import IO;
import ValueIO;
import String;
import Type;
import Message;
import List;

//import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Load;

import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::Compile;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;

import experiments::Compiler::muRascal2RVM::mu2rvm;
import experiments::Compiler::muRascal2RVM::StackSize;
import experiments::Compiler::muRascal2RVM::PeepHole;
import util::Reflective;

public loc MuLibrary = getSearchPathLocation("/experiments/Compiler/muRascal2RVM/LibraryGamma.mu");
public loc MuLibraryCompiled = getSearchPathLocation("experiments/Compiler/muRascal2RVM/LibraryGamma.rvm");

// Specific for delimited continuations (experimental)
// public loc MuLibrary = |std:///experiments/Compiler/muRascal2RVM/LibraryDelimitedCont.mu|;
// public loc MuLibraryCompiled = |std:///experiments/Compiler/muRascal2RVM/LibraryDelimitedCont.rvm|;
// map[str,Symbol] libTypes = ();

public list[loc] defaultImports = [];  //[|std:///Exception.rsc|, |std:///ParseTree.rsc| ];
 
list[experiments::Compiler::RVM::AST::Declaration] parseMuLibrary(loc bindir = |home:///bin|){
    println("rascal2rvm: Recompiling library <basename(MuLibrary)>.mu");
 	libModule = load(MuLibrary);
 	functions = [];
// 	libTypes = libModule.types; 
 
  	for(fun <- libModule.functions) {
  		functionScope = fun.qname;
  		set_nlocals(fun.nlocals);
  	    body = peephole(tr(fun.body));
  	    required_frame_size = get_nlocals() + estimate_stack_size(fun.body);
    	functions += (fun is muCoroutine) ? COROUTINE(fun.qname, fun. uqname, fun.scopeIn, fun.nformals, get_nlocals(), (), fun.refs, fun.src, required_frame_size, body, [])
    									  : FUNCTION(fun.qname, fun.uqname, fun.ftype, fun.scopeIn, fun.nformals, get_nlocals(), (), false, fun.src, required_frame_size, body, []);
  	}
  	// Specific to delimited continuations (experimental)
//  	functions += [ shiftClosures[qname] | str qname <- shiftClosures ];
//  	shiftClosures = ();
  
  	writeTextValueFile(MuLibraryCompiled, functions);
    println("rascal2rvm: Writing compiled version of library <MuLibraryCompiled>");
  	
  	return functions; 
}

tuple[value, num] execute_and_time(RVMProgram rvmProgram, list[value] arguments, bool debug=false, bool listing=false, 
									bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, bool coverage = false, loc bindir = |home:///bin|){
   map[str,Symbol] imported_types = ();
   list[experiments::Compiler::RVM::AST::Declaration] imported_functions = [];
   lrel[str,list[str],list[str]] imported_overloaded_functions = [];
   map[str,int] imported_overloading_resolvers = ();
   set[Message] messages = rvmProgram.messages;
   
   if(any(msg <- messages, error(_,_) := msg)){
        throw "Cannot execute due to compilation errors";
   }
   
   // Read the muLibrary, recompile if necessary
   MuLibraryCompiled = RVMProgramLocation(MuLibrary, bindir);
   
   if(exists(MuLibraryCompiled) && lastModified(MuLibraryCompiled) > lastModified(MuLibrary)){
      try {
  	       imported_functions = readTextValueFile(#list[experiments::Compiler::RVM::AST::Declaration], MuLibraryCompiled);
  	       // Temporary work around related to issue #343
  	       imported_functions = visit(imported_functions) { case type[value] t : { insert type(t.symbol,t.definitions); }}
  	       println("rascal2rvm: Using compiled library version <basename(MuLibraryCompiled)>.rvm");
  	  } catch: {
  	       imported_functions = parseMuLibrary();
// 	       imported_types += libTypes;
  	  }
   } else {
     imported_functions = parseMuLibrary();
//   imported_types += libTypes;
   }
   
   // Recompile the default imports, if necessary
   
   for(loc def <- defaultImports){
       compiledDef = RVMProgramLocation(def, bindir);
       if(!exists(compiledDef) || lastModified(compiledDef) < lastModified(def)){
          rvm_def = compile(def, bindir = bindir);
          messages += rvm_def.messages;
       }
   }
   
   set[loc] processed = {};
   void processImports(RVMProgram rvmProgram) {
       for(imp <- rvmProgram.imports + defaultImports, imp notin processed) {
           println("importing: <imp>");
           processed += imp;
           //importedLoc = imp.parent + (basename(imp) + ".rvm");
           importedLoc = RVMProgramLocation(imp, bindir);
           try {
  	           importedRvmProgram = readTextValueFile(#RVMProgram, importedLoc);
  	           messages += importedRvmProgram.messages;
  	           
  	           // Temporary work around related to issue #343
  	           importedRvmProgram = visit(importedRvmProgram) { case type[value] t : { insert type(t.symbol,t.definitions); }}
  	           
  	           processImports(importedRvmProgram);
  	          
  	           imported_types = imported_types + importedRvmProgram.types;
  	           imported_functions += [ importedRvmProgram.declarations[fname] | str fname <-importedRvmProgram.declarations ];
  	       
  	           // We need to merge overloading resolvers regarding overloaded function indices
  	           pos_delta = size(imported_overloaded_functions); 
  	           imported_overloaded_functions = imported_overloaded_functions + importedRvmProgram.overloaded_functions;
  	           imported_overloading_resolvers = imported_overloading_resolvers + ( ofname : (importedRvmProgram.resolver[ofname] + pos_delta) | str ofname <- importedRvmProgram.resolver );
  	       
  	       } catch x: println("rascal2rvm: Reading <importedLoc> did not succeed: <x>");      
       }
   }
   
   processImports(rvmProgram);
  
   if(any(msg <- messages, error(_,_) := msg)){
        for(e: error(_,_) <- messages){
            println(e);
        }
        throw "Cannot execute due to compilation errors";
   
   }
   pos_delta = size(imported_overloaded_functions);
   rvmProgram.resolver = ( ofname : rvmProgram.resolver[ofname] + pos_delta | str ofname <- rvmProgram.resolver );
   <v, t> = executeProgram(rvmProgram, imported_types,
   									   imported_functions, 
   									   imported_overloaded_functions, imported_overloading_resolvers, 
   									   arguments, debug, testsuite, profile, trackCalls, coverage);
   if(!testsuite){
   	println("Result = <v>, [<t> msec]");
   }	
   return <v, t>;
}

value execute(RVMProgram rvmProgram, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, bool coverage=false, loc bindir = |home:///bin|){
	<v, t> = execute_and_time(rvmProgram, arguments, debug=debug, listing=listing, testsuite=testsuite,recompile=recompile, profile=profile, trackCalls=trackCalls, coverage=coverage);
	if(testsuite){
   	return printTestReport(v);
   }
   return v;
}

value execute(loc rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false,  bool coverage=false, loc bindir = |home:///bin|){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute(rvmProgram, arguments, debug=debug, testsuite=testsuite,profile=profile, bindir = bindir, trackCalls=trackCalls, coverage=coverage);
}

value execute(str rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls=false,  bool coverage=false, loc bindir = |home:///bin|){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   res = execute(rvmProgram, arguments, debug=debug, testsuite=testsuite,profile=profile, bindir = bindir, trackCalls=trackCalls, coverage=coverage);
}

tuple[value, num] execute_and_time(loc rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls=false,  bool coverage=false, loc bindir = |home:///bin|){
   rvmProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute_and_time(rvmProgram, arguments, debug=debug, testsuite=testsuite, profile=profile, bindir = bindir, trackCalls=trackCalls, coverage=coverage);
}

value executeTests(loc rascalSource){
   rvmProgram = compile(rascalSource);
   return execute(rvmProgram, [], testsuite=true);
}

str makeTestSummary(lrel[loc,int,str] test_results) = "<size(test_results)> tests executed; < size(test_results[_,0])> failed; < size(test_results[_,2])> ignored";

bool printTestReport(value results){
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
	  return size(failed) == 0;
  } else {
    throw "cannot create report for test results: <results>";
  }
}
