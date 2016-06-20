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
import experiments::Compiler::RVM::Interpreter::ExecuteProgram;
import experiments::Compiler::Compile;

import experiments::Compiler::muRascal2RVM::mu2rvm;
import util::Reflective;

private loc MuLibraryLoc(PathConfig pcfg) = getSearchPathLoc("experiments/Compiler/muRascal2RVM/MuLibrary.mu", pcfg);

private str MuLibrary() = "experiments::Compiler::muRascal2RVM::MuLibrary";

tuple[bool, loc] getMuLibraryCompiledReadLoc(PathConfig pcfg) {
    // TODO JURGEN: this does not use the boot path config yet? problem for bootstrapping code
    muLib = |compressed+boot:///MuLibrary.rvm.gz|;
    return <exists(muLib), muLib>;
    //return getDerivedReadLoc(MuLibrary(), "rvm.gz", pcfg);
}

loc getMuLibraryCompiledWriteLoc(PathConfig pcfg) = getDerivedWriteLoc(MuLibrary(), "rvm.gz", pcfg);

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

bool hasErrors(set[Message] messages) = any(msg <- messages, error(_,_) := msg);

RVMProgram mergeImports(RVMModule mainModule, PathConfig pcfg, bool jvm = true, bool verbose = false){
                        
   map[str,map[str,str]] imported_moduleTags = ();
   map[str,Symbol] imported_types = ();
   list[RVMDeclaration] imported_declarations = [];
   lrel[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors] imported_overloaded_functions = [];
   map[str,int] imported_overloading_resolvers = ();
   set[Message] messages = mainModule.messages;
   
   if(hasErrors(messages)){
       return errorRVMProgram(mainModule);        
   }
   
   if(<true, mergedImportsLoc> := getMergedImportsReadLoc(mainModule.name, pcfg)){
      startTime = cpuTime();
   
      rvmMergedImports = readBinaryValueFile(#RVMModule, mergedImportsLoc);
    
      if(verbose) println("Reading: <mergedImportsLoc>: <(cpuTime() - startTime)/1000000>ms");
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

   if(existsMuLibraryCompiled){
       try {
           imported_declarations = readBinaryValueFile(#list[RVMDeclaration], MuLibraryCompiled);
           // Temporary work around related to issue #343
           imported_declarations = visit(imported_declarations) { case type[value] t : { insert type(t.symbol,t.definitions); }}
           if(verbose) println("execute: Using compiled library version <MuLibraryCompiled>");
      } catch: {
           throw "Cannot read <MuLibraryCompiled>";
      }
   } else {
      throw "<MuLibraryCompiled> does not exist";
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
   
   void resolve_module_extensions(str importName, list[RVMDeclaration] imported_declarations, list[RVMDeclaration] new_declarations){
        
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
  
   if(hasErrors(messages)){
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
 
   mergedImportsLoc = getMergedImportsWriteLoc(mainModule.name, pcfg); 
   if(verbose) println("Writing: <mergedImportsLoc>");      
   writeBinaryValueFile(mergedImportsLoc, rvmMergedImports);
   
   pos_delta = size(imported_overloaded_functions);
   mainModule.resolver = ( ofname : mainModule.resolver[ofname] + pos_delta | str ofname <- mainModule.resolver );
   
   
   return rvmProgram(mainModule,
           imported_moduleTags + mainModule.module_tags,
           imported_types,
           imported_declarations,
           imported_overloading_resolvers,
           imported_overloaded_functions
           );
}

value execute(RVMProgram program, PathConfig pcfg, map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false,
                                  bool testsuite=false, bool recompile=false, bool profile=false, bool trace= false, 
                                  bool coverage = false, bool jvm = true, bool verbose = false){
   startTime = cpuTime();
   v = executeProgram(     program,
                           keywordArguments,
                           debug, 
                           debugRVM, 
                           testsuite, 
                           profile, 
                           trace, 
                           coverage,
                           jvm);
 
   println("Executing: <(cpuTime() - startTime)/1000000> ms");
   return v;                            
}

value execute(RVMModule mainModule, PathConfig pcfg, map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false,
                                    bool testsuite=false, bool recompile=false, bool profile=false, bool trace= false, 
                                    bool coverage = false, bool jvm = true, bool verbose = false){
   start_linking = cpuTime();   
   merged = mergeImports(mainModule, pcfg, verbose=verbose, jvm=jvm);
   link_time = cpuTime() - start_linking;
   println("linking: <link_time/1000000> msec");
   return execute(merged, pcfg, keywordArguments=keywordArguments, debug=debug, debugRVM=debugRVM, testsuite=testsuite,recompile=recompile,
                  profile=profile,trace=trace,coverage=coverage,jvm=jvm,verbose=verbose);             
}

value execute(loc moduleLoc, PathConfig pcfg, map[str,value] 
              keywordArguments = (), bool debug=false, bool debugRVM=false, bool testsuite=false, bool recompile=false, bool profile=false, 
              bool trace= false,  bool coverage=false, bool jvm=true, bool verbose = false) {
   return execute(getModuleName(moduleLoc, pcfg), pcfg, keywordArguments = keywordArguments, debug=debug, debugRVM=debugRVM, 
           testsuite=testsuite, profile=profile, trace=trace, coverage=coverage,jvm=jvm,verbose=verbose);    
}

value execute(str qualifiedModuleName, PathConfig pcfg, 
              map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false, bool testsuite=false, bool recompile=false, 
              bool profile=false, bool trace= false,  bool coverage=false, bool jvm=true, bool verbose = false){
   if(!recompile){
      if(<true, compressed> := RVMExecutableCompressedReadLoc(qualifiedModuleName, pcfg)){
         if(verbose) println("Using <compressed>");
         startTime = cpuTime();
         v = executeProgram(compressed, keywordArguments, debug, debugRVM, testsuite, profile, trace, coverage, jvm);
         println("Executing: <(cpuTime() - startTime)/1000000> ms");
         if(!testsuite && verbose){
            println("Result = <v>");
         }  
         return v;
      }
      throw "Executable not found, compile first or use recompile=true";
   }
   mainModule = compile(qualifiedModuleName, pcfg, verbose=verbose);
   return execute(mainModule, pcfg, keywordArguments=keywordArguments, debug=debug, debugRVM=debugRVM, testsuite=testsuite, profile=profile, verbose=verbose, trace=trace, coverage=coverage, jvm=jvm);
}

value rascalTests(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, 
                  map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false, bool recompile=false, bool profile=false, 
                  bool trace= false,  bool coverage=false, bool jvm=true, bool verbose = false){
    return rascalTests(qualifiedModuleNames, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin),
                       keywordArguments=keywordArguments,
                       debug=debug,
                       debugRVM=debugRVM,
                       recompile=recompile,
                       profile=profile,
                       trace=trace,
                       coverage=coverage,
                       jvm=jvm,
                       verbose=verbose);
}

value rascalTests(list[str] qualifiedModuleNames, PathConfig pcfg, 
                  map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false, bool recompile=false, bool profile=false, 
                  bool trace= false,  bool coverage=false, bool jvm=true, bool verbose = false){
   lrel[loc,int,str] all_test_results = [];
   
   exceptions = [];
   value v;
   
   for(qualifiedModuleName <- qualifiedModuleNames){
       try {
       if(!recompile && <true, compressed> := RVMExecutableCompressedReadLoc(qualifiedModuleName, pcfg)){
           if(verbose) println("Using <compressed>");
           v = executeProgram(compressed, keywordArguments, debug, debugRVM, true, profile, trace, coverage, jvm);
       } else {
           mainModule = compile(qualifiedModuleName, pcfg, verbose=verbose);
           v = execute(mainModule, pcfg, keywordArguments=keywordArguments, debug=debug, debugRVM=debugRVM, testsuite=true, profile=profile, verbose=verbose, trace=trace, coverage=coverage, jvm=jvm);
       }
       if(lrel[loc,int,str] test_results := v){
          all_test_results += test_results;
       } else {
          throw "cannot extract test results for <qualifiedModuleName>: <v>";
       }
       } catch e: {
         exceptions += "<qualifiedModuleName>: <e>";
       }
   }
   return printTestReport(all_test_results, exceptions);
}

RVMProgram compileAndLink(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin,  
                          bool jvm=true, bool verbose = false){
    return compileAndLink(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), jvm=jvm, verbose=verbose);
}

list[RVMProgram] compileAndLink(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin,  
                          bool jvm=true, bool verbose = false){
    pcfg = pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin);
    return [ compileAndLink(qualifiedModuleName, pcfg, jvm=jvm, verbose=verbose) | qualifiedModuleName <- qualifiedModuleNames ];        
}                          

RVMProgram compileAndLink(str qualifiedModuleName, PathConfig pcfg, bool jvm=true, bool verbose = false, bool optimize=true){
   startTime = cpuTime();
   mainModule = compile(qualifiedModuleName, pcfg, verbose=verbose, optimize=optimize);
   if(verbose) println("Compiling: <(cpuTime() - startTime)/1000000> ms");
   start_linking = cpuTime();   
   merged = mergeImports(mainModule, pcfg, verbose=verbose, jvm=jvm);
   link_time = cpuTime() - start_linking;
   if(verbose) println("linking: <link_time/1000000> msec");
   mergedLoc = getDerivedWriteLoc(mainModule.name, "rvm.ser.gz", pcfg);       
   linkAndSerializeProgram(mergedLoc, merged, jvm);
   return merged;
}

RVMProgram compileAndMergeIncremental(str qualifiedModuleName, bool reuseConfig, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool jvm=true, bool verbose = false, bool optimize = true){
   //pcfg = pathConfig(srcs=[|std:///|, |test-modules:///|], bin=|home:///bin-console|, libs=[|home:///bin-console|]);
   pcfg = pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin);
   if(!reuseConfig){
      mergedImportLoc = getMergedImportsWriteLoc(qualifiedModuleName, pcfg);
      try {
         remove(mergedImportLoc);
         //println("Removed: <mergedImportLoc>");
      } catch e:
          ;//println("Could not remove: <mergedImportLoc>"); // ignore possible exception
   }
   mainModule = compileIncremental(qualifiedModuleName, reuseConfig, pcfg, verbose=verbose, optimize=optimize); 
   merged = mergeImports(mainModule, pcfg, verbose=verbose, jvm=jvm);

   return merged;
}

str makeTestSummary(lrel[loc,int,str] test_results) = "<size(test_results)> tests executed; < size(test_results[_,0])> failed; < size(test_results[_,2])> ignored";

bool printTestReport(value results, list[value] exceptions){
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
      if(size(exceptions) > 0){
         println("\nEXCEPTIONS:");
         for(exc <- exceptions){
             println(exc);
         }
      }
      
      println("\nTEST SUMMARY: " + makeTestSummary(test_results));
      return size(failed) == 0;
  } else {
    throw "cannot create report for test results: <results>";
  }
}
