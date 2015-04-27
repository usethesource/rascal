module experiments::Compiler::Execute

import IO;
import ValueIO;
import String;
import Type;
import Message;
import List;
import Set;
import ParseTree;
import util::Benchmark;

//import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Load;

import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::Compile;

//import lang::rascal::types::TestChecker;
//import lang::rascal::types::CheckTypes;

import experiments::Compiler::muRascal2RVM::mu2rvm;
import experiments::Compiler::muRascal2RVM::StackValidator;
import experiments::Compiler::muRascal2RVM::PeepHole;
import util::Reflective;

public loc MuLibrary = getSearchPathLocation("/experiments/Compiler/muRascal2RVM/LibraryGamma.mu");
public loc MuLibraryCompiled = getSearchPathLocation("experiments/Compiler/muRascal2RVM/LibraryGamma.rvm");

public list[loc] defaultImports = [];  //[|std:///Exception.rsc|, |std:///ParseTree.rsc| ];

alias Resolved = tuple[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors];
 
list[experiments::Compiler::RVM::AST::Declaration] parseMuLibrary(loc bindir = |home:///bin|){
    println("rascal2rvm: Recompiling library <basename(MuLibrary)>.mu");
 	libModule = load(MuLibrary);
 	functions = [];
 
  	for(fun <- libModule.functions) {
  		setFunctionScope(fun.qname);
  		set_nlocals(fun.nlocals);
  	    body = peephole(tr(fun.body));
  	    <maxSP, exceptions> = validate(fun.src, body, []);
  	    required_frame_size = get_nlocals() + maxSP;
    	functions += (fun is muCoroutine) ? COROUTINE(fun.qname, fun. uqname, fun.scopeIn, fun.nformals, get_nlocals(), (), fun.refs, fun.src, required_frame_size, body, [])
    									  : FUNCTION(fun.qname, fun.uqname, fun.ftype, fun.scopeIn, fun.nformals, get_nlocals(), (), false, false, false, fun.src, required_frame_size, 
    									  			 false, 0, 0, body, []);
  	}
  
  	writeTextValueFile(MuLibraryCompiled, functions);
    println("rascal2rvm: Writing compiled version of library <MuLibraryCompiled>");
  	
  	return functions; 
}

tuple[value, num] execute_and_time(RVMProgram mainProgram, list[value] arguments, bool debug=false, bool listing=false, 
									bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, bool coverage = false, loc bindir = |home:///bin|){
									
   start_loading = cpuTime();
   map[str,Symbol] imported_types = ();
   list[experiments::Compiler::RVM::AST::Declaration] imported_declarations = [];
   lrel[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors] imported_overloaded_functions = [];
   map[str,int] imported_overloading_resolvers = ();
   set[Message] messages = mainProgram.messages;
   map[str,map[str,str]] imported_moduleTags = ();
   
   if(any(msg <- messages, error(_,_) := msg)){
        throw "Cannot execute due to compilation errors";
   }
   
   // Read the muLibrary, recompile if necessary
   MuLibraryCompiled = RVMProgramLocation(MuLibrary, bindir);
   println("MuLibrary: <MuLibrary>");
   println("MuLibraryCompiled: <MuLibraryCompiled>");
   if(exists(MuLibraryCompiled) && lastModified(MuLibraryCompiled) > lastModified(MuLibrary)){
      try {
  	       imported_declarations = readTextValueFile(#list[experiments::Compiler::RVM::AST::Declaration], MuLibraryCompiled);
  	       // Temporary work around related to issue #343
  	       imported_declarations = visit(imported_declarations) { case type[value] t : { insert type(t.symbol,t.definitions); }}
  	       println("rascal2rvm: Using compiled library version <basename(MuLibraryCompiled)>.rvm");
  	  } catch: {
  	       imported_declarations = parseMuLibrary();
  	  }
   } else {
     imported_declarations = parseMuLibrary();
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
   rel[str,str] extending_modules = {};
   void processImports(RVMProgram rvmProgram) {
       for(imp <- rvmProgram.imports + defaultImports, imp notin processed) {
           println("<rvmProgram.name> importing: <imp>");
           
           processed += imp;
           importedLoc = RVMProgramLocation(imp, bindir);
           try {
  	           RVMProgram importedRvmProgram = readTextValueFile(#RVMProgram, importedLoc);
  	           
  	           if(imp in rvmProgram.extends){
           			println("<rvmProgram.name> EXTENDS <imp>");
           			extending_modules += {<rvmProgram.name, importedRvmProgram.name>};
           		}
           
  	           messages += importedRvmProgram.messages;
  	           imported_moduleTags[importedRvmProgram.name] = importedRvmProgram.tags;
  	           
  	           // Temporary work around related to issue #343
  	           importedRvmProgram = visit(importedRvmProgram) { case type[value] t : { insert type(t.symbol,t.definitions); }}
  	           
  	           processImports(importedRvmProgram);
  	          
  	           imported_types = imported_types + importedRvmProgram.types;
  	           new_declarations = [ importedRvmProgram.declarations[dname] | str dname <-importedRvmProgram.declarations ];
  	           
  	           //println("extending_modules = <extending_modules>");
  	 
  	 		   if(!isEmpty(extending_modules)){
  	       	   		resolve_module_extensions(importedRvmProgram.name, imported_declarations, new_declarations);
  	       	   }	
  	       	   
  	       	   imported_declarations += new_declarations;
  	       	   
  	           // Merge overloading functions and resolvers: all indices in the current resolver have to be incremented by the number of imported overloaded functions
  	           pos_delta = size(imported_overloaded_functions); 
  	           imported_overloaded_functions = imported_overloaded_functions + importedRvmProgram.overloaded_functions;
  	           imported_overloading_resolvers = imported_overloading_resolvers + ( ofname : (importedRvmProgram.resolver[ofname] + pos_delta) | str ofname <- importedRvmProgram.resolver );
  	       
  	       } catch x: println("rascal2rvm: Reading <importedLoc> did not succeed: <x>");      
       }
   }
   
   bool does_extend(str moduleName, list[str] functions) =
   		any(fname <- functions, <moduleName, fname[0 .. findFirst(fname, "/")]> in extending_modules);

   
   
   void resolve_module_extensions(str importName, list[experiments::Compiler::RVM::AST::Declaration] imported_declarations, list[experiments::Compiler::RVM::AST::Declaration] new_declarations){
   		
   		println("resolve_module_extensions while importing <importName>:");
   		//for(d <- imported_declarations) println("\timported_declarations: <d>");
   		//for(d <- new_declarations) println("\tnew_declarations: <d>");
   		
   	   //new_names = { decl.uqname | decl <- new_declarations, decl has ftype}; // TODO: public?
   	   //
   	   //current_public_names = public_function_names;
   	   //public_function_names += new_names;
   	   //if(isEmpty(current_public_names & new_names)){
   	   //		return;
   	   //}
	   for(decl <- new_declarations){
	   	if(decl has ftype){
	   	    
	   		 overloads = imported_overloaded_functions[decl.uqname, decl.ftype, decl.scopeIn];
	   		 if(overloads != []){
	   			//println("decl = <decl>");
	   			imported_overloaded_functions =
	   				for(Resolved tup: <str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors> <- imported_overloaded_functions){
	   					//println("tup = <tup>");
	   					if(name == decl.uqname && funType == decl.ftype && scope == decl.scopeIn, decl.qname notin tup.ofunctions && does_extend(importName, tup.ofunctions)){
	   					    
	   						println("*** added <decl.uqname> *** it overlaps with: <overloads>");
	   						append <name, 
	   								funType, 
	   								decl.scopeIn, 
	   							    decl.isDefault ? tup.ofunctions + decl.qname : decl.qname + tup.ofunctions,
	   							    tup.oconstructors>;
	   					} else {
	   						append tup;
	   					}	
	   				};
	   			}
	   		}
	   }
   }
   
   processImports(mainProgram);
   
   //println("==== imported:");
   //println("imported_declarations:"); for(fn <- imported_declarations) /*if(contains(fn[0].uqname, "subtype"))*/ println("<fn>");
   //println("imported_overloaded_functions:"); for(fn <- imported_overloaded_functions) println("<fn>");
   //println("imported_overloading_resolvers:"); for(res <- imported_overloading_resolvers) println("<res>: <imported_overloading_resolvers[res]>");
  
   if(any(msg <- messages, error(_,_) := msg)){
        for(e: error(_,_) <- messages){
            println(e);
        }
        throw "Cannot execute due to compilation errors";
   
   }
   pos_delta = size(imported_overloaded_functions);
   mainProgram.resolver = ( ofname : mainProgram.resolver[ofname] + pos_delta | str ofname <- mainProgram.resolver );
   
   //println("==== <mainProgram.name>:");
   //println("functions:"); for(fn <- mainProgram.declarations) println("<fn>: <mainProgram.declarations[fn]>");
   
   if(!isEmpty(mainProgram.extends)){
   		resolve_module_extensions(mainProgram.name, imported_declarations, [ mainProgram.declarations[dname] | str dname <-mainProgram.declarations ]);
   }	
   
   //println("imported_declarations:"); for(fn <- imported_declarations) /*if(contains(fn[0].uqname, "subtype"))*/ println("<fn>");
   //println("<mainProgram.name>: final imported_overloaded_functions:"); for(fn <- imported_overloaded_functions) println("\t<fn>");
   //println("<mainProgram.name>: final imported_overloading_resolvers:"); for(res <- imported_overloading_resolvers) println("\t<res>: <imported_overloading_resolvers[res]>");
   
   //println("updated imported_overloaded_functions:"); for(fn <- imported_overloaded_functions) println("<fn>");
   
   //println("overloaded_functions:");  for(fn <- mainProgram.overloaded_functions) if(contains(fn[0], "subtype"))  println("<fn>");
   //println("overloading_resolvers:"); for(res <- mainProgram.resolver) println("<res>: <mainProgram.resolver[res]>");
   
   load_time = cpuTime() - start_loading;
   <v, t> = executeProgram(
  						   RVMExecutableLocation(mainProgram.src, bindir),
   						   mainProgram, 
   						   imported_moduleTags,
   						   imported_types,
   						   imported_declarations, 
   						   imported_overloaded_functions, 
   						   imported_overloading_resolvers, 
   						   arguments, 
   						   debug, 
   						   testsuite, 
   						   profile, 
   						   trackCalls, 
   						   coverage);
   if(!testsuite){
   		println("Result = <v>, [load: <load_time/1000000> msec, execute: <t> msec]");
   }	
   return <v, t>;
}

value execute(RVMProgram mainProgram, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, bool coverage=false, loc bindir = |home:///bin|){
	<v, t> = execute_and_time(mainProgram, arguments, debug=debug, listing=listing, testsuite=testsuite,recompile=recompile, profile=profile, trackCalls=trackCalls, coverage=coverage);
	//if(testsuite){
 //  	   return printTestReport(v);
 //   }
   return v;
}

value execute(loc rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false,  bool coverage=false, loc bindir = |home:///bin|){
   if(!recompile){
      executable = RVMExecutableLocation(rascalSource, bindir);
      if(exists(executable)){
      	 <v, t> = executeProgram(executable, arguments, debug, testsuite, profile, trackCalls, coverage);
      	 return v;
      }
   }
   
   mainProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute(mainProgram, arguments, debug=debug, testsuite=testsuite,profile=profile, bindir = bindir, trackCalls=trackCalls, coverage=coverage);
}

value execute(str rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls=false,  bool coverage=false, loc bindir = |home:///bin|){
   mainProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute(mainProgram, arguments, debug=debug, testsuite=testsuite,profile=profile, bindir = bindir, trackCalls=trackCalls, coverage=coverage);
}

tuple[value, num] execute_and_time(loc rascalSource, list[value] arguments, bool debug=false, bool listing=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls=false,  bool coverage=false, loc bindir = |home:///bin|){
   mainProgram = compile(rascalSource, listing=listing, recompile=recompile);
   return execute_and_time(mainProgram, arguments, debug=debug, testsuite=testsuite, profile=profile, bindir = bindir, trackCalls=trackCalls, coverage=coverage);
}

value executeTests(loc rascalSource){
   mainProgram = compile(rascalSource);
   return execute(mainProgram, [], testsuite=true);
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
	  println("\nTEST SUMMARY: " + makeTestSummary(test_results));
	  return size(failed) == 0;
  } else {
    throw "cannot create report for test results: <results>";
  }
}
