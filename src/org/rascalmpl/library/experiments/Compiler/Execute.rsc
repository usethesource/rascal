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

import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::ExecuteProgram;
import experiments::Compiler::Compile;

import experiments::Compiler::muRascal2RVM::mu2rvm;
import util::Reflective;

private loc MuLibraryLoc(PathConfig pcfg) = getSearchPathLoc("experiments/Compiler/muRascal2RVM/MuLibrary.mu", pcfg);

private str MuLibrary() = "experiments::Compiler::muRascal2RVM::MuLibrary";

tuple[bool, loc] getMuLibraryCompiledReadLoc(PathConfig pcfg) {
    muLib = |compressed+boot:///MuLibrary.rvm.gz|;
    return <exists(muLib), muLib>;
    //return getDerivedReadLoc(MuLibrary(), "rvm.gz", pcfg);
}

loc getMuLibraryCompiledWriteLoc(PathConfig pcfg) = getDerivedWriteLoc(MuLibrary(), "rvm.gz", pcfg);

tuple[bool,loc] getMergedImportsReadLoc(str mainQualifiedName, PathConfig pcfg){
    merged_imports_qname = mainQualifiedName + "_imports";
    return getDerivedReadLoc(merged_imports_qname, "rvm.gz", pcfg);
}

loc getMergedImportsWriteLoc(str mainQualifiedName, PathConfig pcfg){
    merged_imports_qname = mainQualifiedName + "_imports";
    return getDerivedWriteLoc(merged_imports_qname, "rvm.gz", pcfg);
}

alias Resolved = tuple[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors];

RVMModule getImport(loc importedLoc){
    return getImport1(importedLoc, lastModified(importedLoc));
}

@memo
RVMModule getImport1(loc importedLoc, datetime lastModified){
    return readBinaryValueFile(#RVMModule, importedLoc);
}

bool valid(loc mergedImportsLoc, RVMModule mergedProgram, RVMModule program, PathConfig pcfg){

    if(mergedProgram.imports == program.imports && mergedProgram.extends == program.extends){
        mm = lastModified(mergedImportsLoc);
        for(imp <- mergedProgram.imports){
            if(contains(imp, "\\")){
               imp1 = replaceAll(imp, "\\", "");
               println("valid: <imp> -\> <imp1>");
               imp = imp1;
            }
            
            <existsImp, impLoc> = RVMModuleReadLoc(imp, pcfg);
            if(!existsImp || lastModified(impLoc) > mm){
                return false;
            }
        }
        
        for(ext <- mergedProgram.extends){
            <existsExt, extLoc> = RVMModuleReadLoc(ext, pcfg);
            if(!existsExt || lastModified(extLoc) > mm){
                return false;
            }
        }
        return true;
    }
    return false;
}

RVMProgram mergeImports(RVMModule mainModule, PathConfig pcfg, bool useJVM = false, bool serialize=false, bool verbose = false){
                        
   map[str,map[str,str]] imported_moduleTags = ();
   map[str,Symbol] imported_types = ();
   list[experiments::Compiler::RVM::AST::Declaration] imported_declarations = [];
   lrel[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors] imported_overloaded_functions = [];
   map[str,int] imported_overloading_resolvers = ();
   set[Message] messages = mainModule.messages;
   
   if(any(msg <- messages, error(_,_) := msg)){
       return errorRVMProgram(mainModule);        
   }
   
   if(<true, mergedImportsLoc> := getMergedImportsReadLoc(mainModule.name, pcfg)){
      startTime = cpuTime();
   
      rvmMergedImports = readBinaryValueFile(#RVMModule, mergedImportsLoc);
    
      println("Reading: <mergedImportsLoc>: <(cpuTime() - startTime)/1000000>ms");
      if(valid(mergedImportsLoc, rvmMergedImports, mainModule, pcfg)){
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
   
   // Read the muLibrary
   <existsMuLibraryCompiled, MuLibraryCompiled>  = getMuLibraryCompiledReadLoc(pcfg);
   println("MuLibraryCompiled: <existsMuLibraryCompiled>, <MuLibraryCompiled>, <MuLibraryCompiled.scheme>");
   if(existsMuLibraryCompiled){
       try {
           imported_declarations = readBinaryValueFile(#list[experiments::Compiler::RVM::AST::Declaration], MuLibraryCompiled);
           // Temporary work around related to issue #343
           imported_declarations = visit(imported_declarations) { case type[value] t : { insert type(t.symbol,t.definitions); }}
           if(verbose) println("execute: Using compiled library version <MuLibraryCompiled>");
      } catch: {
           throw "Cannot read <MuLibraryCompiled>";
      }
   }
   
   rel[str,str] extending_modules = {};
   
   void processImports(RVMModule rvmModule) {
       list[str] orderedImports = reverse(order(rvmModule.importGraph))/* - rvmModule.name*/;
       //println("Ordered import graph <rvmModule.name>: <orderedImports>");
       
       for(str impName <- orderedImports) {
          // println("execute: IMPORT <impName>");
           
           imp = getModuleLocation(impName, pcfg);
           <existsImportedLoc, importedLoc> = RVMModuleReadLoc(impName, pcfg);
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
        return errorRVMProgram(messages);
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
      mergedImportsLoc = getMergedImportsWriteLoc(mainModule.name, pcfg);       
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

value execute(RVMProgram program, PathConfig pcfg, map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false,
                                  bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, 
                                  bool coverage = false, bool useJVM = false, bool serialize=false, bool verbose = false){

   v = executeProgram(RVMExecutableCompressedWriteLoc(program.main_module.name, pcfg),
                           program,
                           keywordArguments,
                           debug, 
                           debugRVM, 
                           testsuite, 
                           profile, 
                           trackCalls, 
                           coverage,
                           useJVM,
                           serialize);
   return v;                            
}

value execute(RVMModule mainModule, PathConfig pcfg, map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false,
                                    bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, 
                                    bool coverage = false, bool useJVM = false, bool serialize=false, bool verbose = false){
   start_linking = cpuTime();   
   merged = mergeImports(mainModule, pcfg, verbose=verbose, serialize=serialize, useJVM=useJVM);
   link_time = cpuTime() - start_linking;
   println("linking: <link_time/1000000> msec");
   return execute(merged, pcfg, keywordArguments=keywordArguments, debug=debug, debugRVM=debugRVM, testsuite=testsuite,recompile=recompile,profile=profile,trackCalls=trackCalls,coverage=coverage,useJVM=useJVM,serialize=serialize,verbose=verbose);             
}
value execute(loc moduleLoc, PathConfig pcfg, map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false,  bool coverage=false, bool useJVM=false, bool serialize=false, bool verbose = false) =
   execute(getModuleName(moduleLoc, pcfg), pcfg, keywordArguments = keywordArguments, debug=debug, debugRVM=debugRVM, testsuite=testsuite, profile=profile, trackCalls=trackCalls, coverage=coverage,useJVM=useJVM,serialize=serialize,verbose=verbose);


value execute(str qualifiedModuleName, PathConfig pcfg, map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false,  bool coverage=false, bool useJVM=false, bool serialize=false, bool verbose = false){
   if(!recompile){
      if(<true, compressed> := RVMExecutableCompressedReadLoc(qualifiedModuleName, pcfg)){
         if(verbose) println("Using <compressed>");
         v = executeProgram(compressed, keywordArguments, debug, debugRVM, testsuite, profile, trackCalls, coverage, useJVM);
         if(!testsuite && verbose){
            println("Result = <v>");
         }  
         return v;
      }
      throw "Executable not found, compile first or use recompile=true";
   }
   startTime = cpuTime();
   mainModule = compile(qualifiedModuleName, pcfg, verbose=verbose);
   //println("Compiling: <(cpuTime() - startTime)/1000000> ms");
   startTime = cpuTime();
   v = execute(mainModule, pcfg, keywordArguments=keywordArguments, debug=debug, debugRVM=debugRVM, testsuite=testsuite, profile=profile, verbose=verbose, trackCalls=trackCalls, coverage=coverage, useJVM=useJVM, serialize=serialize);
   println("Executing: <(cpuTime() - startTime)/1000000> ms");
   return v;
}

RVMProgram compileAndLink(str qualifiedModuleName, PathConfig pcfg, bool useJVM=false, bool serialize=false, bool verbose = false){
   startTime = cpuTime();
   mainModule = compile(qualifiedModuleName, pcfg, verbose=verbose);
   if(verbose) println("Compiling: <(cpuTime() - startTime)/1000000> ms");
   start_linking = cpuTime();   
   merged = mergeImports(mainModule, pcfg, verbose=verbose, useJVM=useJVM, serialize=serialize);
   link_time = cpuTime() - start_linking;
   if(verbose) println("linking: <link_time/1000000> msec");
   if(serialize){
      mergedLoc = getDerivedWriteLoc(mainModule.name, "rvm.ser.gz", pcfg);       
      serializeProgram(mergedLoc, merged, useJVM);
   }
   return merged;
}

RVMProgram compileAndLinkIncremental(str qualifiedModuleName, bool reuseConfig, bool useJVM=false, bool serialize=false, bool verbose = false){
   startTime = cpuTime();
   pcfg = pathConfig(srcPath=[|std:///|, |test-modules:///|]);
   mainModule = compileIncremental(qualifiedModuleName, reuseConfig, pcfg, verbose=verbose);
   //println("Compiling: <(cpuTime() - startTime)/1000000> ms");
   start_linking = cpuTime();   
   merged = mergeImports(mainModule, pcfg, verbose=verbose, useJVM=useJVM, serialize=serialize);
   link_time = cpuTime() - start_linking;
   println("linking: <link_time/1000000> msec");
   return merged;
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
