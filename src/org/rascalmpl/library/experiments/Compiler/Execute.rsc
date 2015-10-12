module experiments::Compiler::Execute

import IO;
import ValueIO;
import String;
import Type;
import Message;
import List;
import Map;
import Set;
import ParseTree;
import util::Benchmark;
import analysis::graphs::Graph;
import DateTime;

import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Load;

import experiments::Compiler::RVM::AST;   // Strange: using import here, gives RVMModule not defined errors later on
import experiments::Compiler::RVM::ExecuteProgram;
import experiments::Compiler::Compile;

import experiments::Compiler::muRascal2RVM::mu2rvm;
import experiments::Compiler::muRascal2RVM::StackValidator; // TODO: hide these two
import experiments::Compiler::muRascal2RVM::PeepHole;
import util::Reflective;

private loc MuLibrary() = getSearchPathLocation("experiments/Compiler/muRascal2RVM/Library.mu");

loc getMuLibraryCompiled(loc bindir = |home:///bin|) = getDerivedLocation(MuLibrary(), "rvm.gz", compressed=true, bindir = bindir);

loc getMergedImportsLocation(loc mainSourceLoc, loc bindir = |home:///bin|){
    merged_imports_src = mainSourceLoc[path = replaceLast(mainSourceLoc.path, ".rsc", "-imports.rsc")];
    return getDerivedLocation(merged_imports_src, "rvm.gz", compressed=true, bindir = bindir);
}

alias Resolved = tuple[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors];

//private map[loc,tuple[datetime modified, RVMModule rvmModule]] importCache = ();

RVMModule getImport(loc importedLoc){
    return getImport1(importedLoc, lastModified(importedLoc));
}

@memo
RVMModule getImport1(loc importedLoc, datetime lastModified){
    return readBinaryValueFile(#RVMModule, importedLoc);
}
 
list[experiments::Compiler::RVM::AST::Declaration] parseMuLibrary(bool verbose = false, loc bindir = |home:///bin|){
    if(verbose) println("execute: Recompiling library <basename(MuLibrary())>.mu");
    MuLibraryCompiled = getMuLibraryCompiled(bindir = bindir);
    libModule = load(MuLibrary());
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
  
    writeBinaryValueFile(MuLibraryCompiled, functions);
    if(verbose) println("execute: Writing compiled version of library <MuLibraryCompiled>");
    
    return functions; 
}

bool valid(loc mergedImportsLoc, RVMModule mergedProgram, RVMModule program, loc bindir = |home:///bin|){

    if(mergedProgram.imports == program.imports && mergedProgram.extends == program.extends){
        mm = lastModified(mergedImportsLoc);
        for(imp <- mergedProgram.imports){
            if(contains(imp, "\\")){
               imp1 = replaceAll(imp, "\\", "");
               println("valid: <imp> -\> <imp1>");
               imp = imp1;
            }
            if(lastModified(RVMModuleLocation(getModuleLocation(imp), bindir)) > mm){
                return false;
            }
        }
        
        for(ext <- mergedProgram.extends){
            if(lastModified(RVMModuleLocation(getModuleLocation(ext), bindir)) > mm){
                return false;
            }
        }
        return true;
    }
    return false;
}

RVMProgram mergeImports(RVMModule mainModule, bool useJVM = false, bool serialize=false, bool verbose = false, loc bindir = |home:///bin|){
                        
   map[str,map[str,str]] imported_moduleTags = ();
   map[str,Symbol] imported_types = ();
   list[experiments::Compiler::RVM::AST::Declaration] imported_declarations = [];
   lrel[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors] imported_overloaded_functions = [];
   map[str,int] imported_overloading_resolvers = ();
   set[Message] messages = mainModule.messages;
   
   if(any(msg <- messages, error(_,_) := msg)){
        for(msg <- messages){
            println(msg);
        }
        throw messages;
   }
   
   mergedImportsLoc = getMergedImportsLocation(mainModule.src, bindir=bindir);
   if(exists(mergedImportsLoc)){
      startTime = cpuTime();
      rvmMergedImports = readBinaryValueFile(#RVMModule, mergedImportsLoc);
      println("Reading: <mergedImportsLoc>: <(cpuTime() - startTime)/1000000>ms");
      if(valid(mergedImportsLoc, rvmMergedImports, mainModule)){
         pos_delta = size(rvmMergedImports.overloaded_functions);
         mainModule.resolver = ( ofname : mainModule.resolver[ofname] + pos_delta | str ofname <- mainModule.resolver );
         return rvmProgram(mainModule,
                 rvmMergedImports.module_tags,
                 rvmMergedImports.types,
                 rvmMergedImports.declarations,
                 rvmMergedImports.resolver,
                 rvmMergedImports.overloaded_functions
                 );
           }
   }
   
   // Read the muLibrary, recompile if necessary
   //println("MuLibrary: <MuLibrary>");
   MuLibraryCompiled = getMuLibraryCompiled(bindir = bindir);
   //println("MuLibraryCompiled: <MuLibraryCompiled>");
   if(exists(MuLibraryCompiled) && lastModified(MuLibraryCompiled) > lastModified(MuLibrary())){
      try {
           imported_declarations = readBinaryValueFile(#list[experiments::Compiler::RVM::AST::Declaration], MuLibraryCompiled);
           // Temporary work around related to issue #343
           imported_declarations = visit(imported_declarations) { case type[value] t : { insert type(t.symbol,t.definitions); }}
           if(verbose) println("execute: Using compiled library version <basename(MuLibraryCompiled)>.rvm");
      } catch: {
           imported_declarations = parseMuLibrary(verbose=verbose, bindir=bindir);
      }
   } else {
     imported_declarations = parseMuLibrary(verbose=verbose, bindir=bindir);
   }
   
   rel[str,str] extending_modules = {};
   
   void processImports(RVMModule rvmModule) {
       list[str] orderedImports = reverse(order(rvmModule.importGraph))/* - rvmModule.name*/;
       //println("Ordered import graph <rvmModule.name>: <orderedImports>");
       
       for(str impName <- orderedImports) {
          // println("execute: IMPORT <impName>");
           
           imp = getModuleLocation(impName);
           importedLoc = RVMModuleLocation(imp, bindir);
           try {
               RVMModule importedRvmModule = getImport(importedLoc);
               
               extensions = {};
                
               for(ext <- importedRvmModule.extends){
                  // println("execute: <importedRvmModule.name> EXTENDS <ext>");
                   extensions += {<importedRvmModule.name, ext>};
               }
           
               messages += importedRvmModule.messages;
               //imported_moduleTags[importedRvmModule.name] = importedRvmModule.tags;
               imported_moduleTags += importedRvmModule.module_tags;
               
               // Temporary work around related to issue #343
               importedRvmModule = visit(importedRvmModule) { case type[value] t : { insert type(t.symbol,t.definitions); }}
              
               imported_types = imported_types + importedRvmModule.types;
               new_declarations = importedRvmModule.declarations;
               
               if(!isEmpty(extensions)){
                    extending_modules += extensions;
                    //println("extending_modules = <extending_modules>");
                    resolve_module_extensions(importedRvmModule.name, imported_declarations, new_declarations);
               }    
               
               imported_declarations += new_declarations;
               
               // Merge overloading functions and resolvers: all indices in the current resolver have to be incremented by the number of imported overloaded functions
               pos_delta = size(imported_overloaded_functions); 
               imported_overloaded_functions = imported_overloaded_functions + importedRvmModule.overloaded_functions;
               imported_overloading_resolvers = imported_overloading_resolvers + ( ofname : (importedRvmModule.resolver[ofname] + pos_delta) | str ofname <- importedRvmModule.resolver );
           
           } catch x: throw "execute: Reading <importedLoc> did not succeed: <x>";      
       }
   }
   
   bool does_extend(str moduleName, list[str] functions) {
        res = any(str fname <- functions, str fnameModule := fname[0 .. findFirst(fname, "/")], <moduleName, fnameModule> in extending_modules);
        //println("does_extend =\> <res> for <moduleName>, <functions>");
        return res;
   }
   
   void resolve_module_extensions(str importName, list[experiments::Compiler::RVM::AST::Declaration] imported_declarations, list[experiments::Compiler::RVM::AST::Declaration] new_declarations){
        
       for(decl <- new_declarations){
          //println("resolve_module_extensions: <decl>");
          if(decl has ftype){
            
             overloads = imported_overloaded_functions[decl.uqname, decl.ftype, decl.scopeIn];
             if(overloads != []){
                //println("decl = <decl>");
                imported_overloaded_functions =
                    for(Resolved tup: <str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors> <- imported_overloaded_functions){
                        //println("tup = <tup>");
                        if(name == decl.uqname && funType == decl.ftype && scope == decl.scopeIn, decl.qname notin tup.ofunctions && does_extend(importName, tup.ofunctions)){
                            
                            //if(verbose) println("execute: *** added as extension: <decl.uqname>, it overlaps with: <overloads> ***");
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
   
   processImports(mainModule);
  
   if(any(msg <- messages, error(_,_) := msg)){
        for(e: error(_,_) <- messages){
            println(e);
        }
        throw "Cannot execute due to compilation errors";
   }
   
   rvmMergedImports =
   rvmModule(mainModule.name,
            imported_moduleTags,
            messages,
            mainModule.imports,
            mainModule.extends,
            imported_types, 
            (), //mainModule.symbol_definitions,
            imported_declarations,
            [], //mainModule.initialization, 
            imported_overloading_resolvers, 
            imported_overloaded_functions,
            mainModule.importGraph,
            mainModule.src
            );
 
   //if(serialize){        
      writeBinaryValueFile(mergedImportsLoc, rvmMergedImports);
   //}
   
   pos_delta = size(imported_overloaded_functions);
   mainModule.resolver = ( ofname : mainModule.resolver[ofname] + pos_delta | str ofname <- mainModule.resolver );
   
   
  return rvmProgram(mainModule,
           imported_moduleTags,
           imported_types,
           imported_declarations,
           imported_overloading_resolvers,
           imported_overloaded_functions
           );
}

value execute(RVMProgram program, map[str,value] keywordArguments = (), bool debug=false, 
                                    bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, 
                                    bool coverage = false, bool useJVM = false, bool serialize=false, bool verbose = false, loc bindir = |home:///bin|){
    v = executeProgram(RVMExecutableLocation(program.main_module.src, bindir),
                           program,
                           keywordArguments, 
                           debug, 
                           testsuite, 
                           profile, 
                           trackCalls, 
                           coverage,
                           useJVM,
                           serialize);
   return v;                            
}

value execute(RVMModule mainModule, map[str,value] keywordArguments = (), bool debug=false, 
                                    bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, 
                                    bool coverage = false, bool useJVM = false, bool serialize=false, bool verbose = false, loc bindir = |home:///bin|){
    start_linking = cpuTime();   
    merged = mergeImports(mainModule, verbose=verbose, useJVM=useJVM, bindir=bindir);
    link_time = cpuTime() - start_linking;
    println("linking: <link_time/1000000> msec");
    return execute(merged, keywordArguments=keywordArguments, debug=debug, testsuite=testsuite,recompile=recompile,profile=profile,trackCalls=trackCalls,coverage=coverage,useJVM=useJVM,serialize=serialize,verbose=verbose,bindir=bindir);             
}

value execute(loc rascalSource, map[str,value] keywordArguments = (), bool debug=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false,  bool coverage=false, bool useJVM=false, bool serialize=true, bool verbose = false, loc bindir = |home:///bin|){
   if(!recompile){
      executable = RVMExecutableLocation(rascalSource, bindir);
      compressed = RVMExecutableCompressedLocation(rascalSource, bindir);
      if(exists(compressed)){
         if(verbose) println("Using <compressed>");
         v = executeProgram(compressed, keywordArguments, debug, testsuite, profile, trackCalls, coverage, useJVM);
         if(!testsuite && verbose){
            println("Result = <v>");
         }  
         return v;
      }
   }
   startTime = cpuTime();
   mainModule = compile(rascalSource, verbose=verbose, bindir=bindir);
   //println("Compiling: <(cpuTime() - startTime)/1000000> ms");
   //<cfg, mainModule> = compile(rascalSource, bindir=bindir);
   startTime = cpuTime();
   v = execute(mainModule, keywordArguments=keywordArguments, debug=debug, testsuite=testsuite, profile=profile, verbose=verbose, bindir=bindir, trackCalls=trackCalls, coverage=coverage, useJVM=useJVM, serialize=serialize);
   println("Executing: <(cpuTime() - startTime)/1000000> ms");
   return v;
}

RVMProgram compileAndLink(loc rascalSource,  bool useJVM=false, bool serialize=true, bool verbose = false, loc bindir = |home:///bin|){
   startTime = cpuTime();
   mainModule = compile(rascalSource, verbose=verbose, bindir=bindir);
   //println("Compiling: <(cpuTime() - startTime)/1000000> ms");
   start_linking = cpuTime();   
   merged = mergeImports(mainModule, verbose=verbose, useJVM=useJVM, bindir=bindir, serialize=serialize);
   link_time = cpuTime() - start_linking;
   println("linking: <link_time/1000000> msec");
   return merged;
}


//value executeBinary(loc executable, list[value] arguments, bool debug=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false,  bool coverage=false, bool useJVM = false, bool verbose = true, loc bindir = |home:///bin|){
//  if(exists(executable)){
//     if(verbose) println("Using <executable>");
//     <v, t> = executeProgram(executable, arguments, debug, testsuite, profile, trackCalls, coverage, useJVM);
//    
//     if(!testsuite && verbose){
//        println("Result = <v>, [execute: <t> msec]");
//     }  
//     return v;
//  }
//}

//value execute_and_time(loc rascalSource, list[value] arguments, bool debug=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls=false,  bool coverage=false, bool useJVM=false, bool serialize=false, bool verbose = true, loc bindir = |home:///bin|){
//   mainModule = compile(rascalSource, bindir=bindir);
//   return execute(mainModule, arguments, debug=debug, testsuite=testsuite, profile=profile, verbose = verbose, bindir = bindir, trackCalls=trackCalls, coverage=coverage, useJVM=useJVM, serialize=serialize);
//}

value executeTests(loc rascalSource){
   mainModule = compile(rascalSource);
   return execute(mainModule, testsuite=true);
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
