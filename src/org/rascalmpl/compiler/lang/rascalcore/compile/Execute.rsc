module lang::rascalcore::compile::Execute

import IO;
import ValueIO;
import String;
import Message;
import List;
import Map;
import Set;
import ParseTree;
import util::Benchmark;
import util::FileSystem;
import analysis::graphs::Graph;
import DateTime;

import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal::Load;

import lang::rascalcore::compile::RVM::AST;
import lang::rascalcore::compile::RVM::Interpreter::ExecuteProgram;
import lang::rascalcore::compile::Compile;

import lang::rascalcore::compile::muRascal2RVM::mu2rvm;
import util::Reflective;

@doc{Use classRenamings to map old names of class files used in generated JVM bytecode to new names, in case of refactoring/renaming/moving them}
// NOTE: Be aware that old dotted names are mapped to new, slash-separated, names, e.g., ("org.rascalmpl.value":"io/usethesource/vallang")
private map[str,str] classRenamings = ();

alias Resolved = tuple[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors];

RVMModule getRVMImport(loc importedRVMLoc){
    return getRVMImport1(importedRVMLoc, lastModified(importedRVMLoc));
}

@memo
RVMModule getRVMImport1(loc importedRVMLoc, datetime lastModified){
    return readBinaryValueFile(#RVMModule, importedRVMLoc);
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
           // Only needed for reified types in RVM instructions
           if(!jvm){
              imported_declarations = visit(imported_declarations) { case type[value] t : { insert type(t.symbol,t.definitions); }}
           }
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
           
           //imp = getModuleLocation(impName, pcfg);
           <existsImportedLoc, importedRVMLoc> = RVMModuleReadLoc(impName, pcfg);
           try {
               RVMModule importedRvmModule = getRVMImport(importedRVMLoc);
               
               extensions = {};
                
               for(ext <- importedRvmModule.extends){
                  // println("execute: <importedRvmModule.name> EXTENDS <ext>");
                   extensions += {<importedRvmModule.name, ext>};
               }
           
               messages += importedRvmModule.messages;
               //imported_moduleTags[importedRvmModule.name] = importedRvmModule.tags;
               imported_moduleTags += importedRvmModule.module_tags;
               
               // Temporary work around related to issue #343
               // Only needed for reified types in RVM instructions
               if(!jvm){
                  importedRvmModule = visit(importedRvmModule) { case type[value] t : { insert type(t.symbol,t.definitions); }}
               }
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
           
           } catch x: throw "execute: Reading <importedRVMLoc> did not succeed: <x>";      
       }
   }
   
   bool does_extend(str moduleName, list[str] functions) {
        res = any(str fname <- functions, str fnameModule := fname[0 .. findFirst(fname, "/")], <moduleName, fnameModule> in extending_modules);
        //println("does_extend =\> <res> for <moduleName>, <functions>");
        return res;
   }
   
   void resolve_module_extensions(str importName, list[RVMDeclaration] imported_declarations, list[RVMDeclaration] new_declarations){
        
       for(decl <- new_declarations){
          // println("resolve_module_extensions: <decl>");
          if(decl has ftype){
             overloads = [<ofunctions, oconstructors> | <Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors> <- imported_overloaded_functions[decl.uqname],
                                                         scope == decl.scopeIn, comparable(funType, decl.ftype)];
                          
             if(overloads != []){
                //println("decl = <decl>");
                imported_overloaded_functions =
                    for(Resolved tup: <str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors> <- imported_overloaded_functions){
                        //println("tup = <tup>");
                        if(name == decl.uqname && scope == decl.scopeIn && comparable(funType, decl.ftype) && decl.qname notin tup.ofunctions && does_extend(importName, tup.ofunctions)){
                            
                            if(verbose) println("execute: *** added as extension: <decl.uqname>, it overlaps with: <overloads> ***");
                            append <name, 
                                    funType, 
                                    decl.scopeIn, 
                                    decl.isDefault ? tup.ofunctions + decl.qname : decl.qname + tup.ofunctions,
                                    tup.oconstructors>;
                        } else {
                            if(verbose && name == decl.uqname){
                                println("not added as extension <decl.uqname>: <tup>");
                                println("scope == decl.scopeIn: <scope == decl.scopeIn>");
                                println("comparable(funType, decl.ftype): <comparable(funType, decl.ftype)>");
                                println("decl.qname notin tup.ofunctions: <decl.qname notin tup.ofunctions>");
                                println("does_extend(importName, tup.ofunctions): <does_extend(importName, tup.ofunctions)>");
                            }
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

value execute(RVMProgram program, PathConfig pcfg, 
                map[str,value] keywordArguments = (), 
                bool debug=false, 
                bool debugRVM=false,
                bool testsuite=false, 
                bool recompile=false, 
                bool profile=false, 
                bool trace= false, 
                bool enableAsserts=false,
                bool coverage = false, 
                bool jvm = true, 
                bool verbose = false){
   startTime = cpuTime();
   return executeProgram(  program,
                           keywordArguments,
                           debug, 
                           debugRVM, 
                           testsuite, 
                           profile, 
                           trace, 
                           coverage,
                           jvm, 
                           verbose);
 
   //if(verbose) println("Executing: <(cpuTime() - startTime)/1000000> ms");
   //return v;                            
}

value execute(RVMModule mainModule, PathConfig pcfg, 
                map[str,value] keywordArguments = (), 
                bool debug=false, 
                bool debugRVM=false,
                bool testsuite=false, 
                bool recompile=false, 
                bool profile=false,
                bool trace= false, 
                bool enableAsserts=false,
                bool coverage = false, 
                bool jvm = true, bool 
                verbose = false){  
   merged = mergeImports(mainModule, pcfg, verbose=verbose, jvm=jvm);
   return execute(merged, pcfg, 
                keywordArguments=keywordArguments, 
                debug=debug, 
                debugRVM=debugRVM, 
                testsuite=testsuite,
                recompile=recompile,
                profile=profile,
                trace=trace, 
                enableAsserts=enableAsserts, 
                coverage=coverage,
                jvm=jvm,
                verbose=verbose);             
}

value execute(loc moduleLoc, PathConfig pcfg, map[str,value] 
              keywordArguments = (), 
              bool debug=false, 
              bool debugRVM=false, 
              bool testsuite=false, 
              bool recompile=false, 
              bool profile=false, 
              bool trace= false, 
              bool enableAsserts=false, 
              bool coverage=false, 
              bool jvm=true, 
              bool verbose = false) {
   return execute(getModuleName(moduleLoc, pcfg), pcfg, 
              keywordArguments = keywordArguments, 
              debug=debug, 
              debugRVM=debugRVM, 
              testsuite=testsuite, 
              profile=profile, 
              trace=trace, 
              enableAsserts=enableAsserts, 
              coverage=coverage,
              jvm=jvm,
              verbose=verbose);    
}

value execute(str qualifiedModuleName, PathConfig pcfg, 
              map[str,value] keywordArguments = (), 
              bool debug=false, 
              bool debugRVM=false, 
              bool testsuite=false, 
              bool recompile=false, 
              bool profile=false, 
              bool trace=false, 
              bool enableAsserts=false, 
              bool coverage=false, 
              bool jvm=true, 
              bool verbose=false){
   if(!recompile){
      if(<true, compressed> := RVMExecutableReadLoc(qualifiedModuleName, pcfg)){
         if(verbose) println("Using <compressed>");
         startTime = cpuTime();
         v = executeProgram(compressed, keywordArguments, debug, debugRVM, testsuite, profile, trace, coverage, jvm, verbose);
         if(verbose) println("Executing: <(cpuTime() - startTime)/1000000> ms");
         if(!testsuite && verbose){
            println("Result = <v>");
         }  
         return v;
      }
      throw "Executable not found, compile first or use recompile=true";
   }
   mainModule = compile(qualifiedModuleName, pcfg, verbose=verbose, enableAsserts=enableAsserts);
   return execute(mainModule, pcfg, keywordArguments=keywordArguments, debug=debug, debugRVM=debugRVM, testsuite=testsuite, profile=profile, verbose=verbose, trace=trace, coverage=coverage, jvm=jvm);
}
 
@deprecated 
value rascalTests(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, 
                  map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false, bool profile=false, 
                  bool trace= false,  bool coverage=false, bool jvm=true, bool verbose = false)
  = rascalTests(qualifiedModuleNames, srcs, libs, boot, bin, true, keywordArguments=keywordArguments, debug=debug, debugRVM=debugRVM, profile=profile,
                trace=trace, coverage=coverage, jvm=jvm, verbose=verbose);
                 

@deprecated
value rascalTests(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool recompile,
                  map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false, bool profile=false, 
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

data TestResults = testResults(lrel[loc,int,str] results, list[value] exceptions);

@deprecated
TestResults rascalTestsRaw(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool recompile,
                  map[str,value] keywordArguments = (), bool debug=false, bool debugRVM=false, bool profile=false, 
                  bool trace= false,  bool coverage=false, bool jvm=true, bool verbose = false){
            
    return rascalTestsRaw(qualifiedModuleNames, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin),
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
                  
TestResults rascalTestsRaw(list[str] qualifiedModuleNames, PathConfig pcfg, 
                  map[str,value] keywordArguments = (), 
                  bool debug=false, 
                  bool debugRVM=false, 
                  bool recompile=false, 
                  bool profile=false, 
                  bool trace=false,  
                  bool coverage=false, 
                  bool jvm=true, 
                  bool verbose=false){
   lrel[loc,int,str] all_test_results = [];
   
   exceptions = [];
   value v;

   expandedModuleNames = [];    // replace any directories by the (nested) Rascal modules found there
   for(qualifiedModuleName <- qualifiedModuleNames){
        try {
            loc moduleLoc = getModuleLocation(qualifiedModuleName, pcfg, extension="");
            expandedModuleNames += [ getModuleName(subModuleLoc, pcfg) | subModuleLoc <- find(moduleLoc, "rsc") ];
        } catch e: {
            expandedModuleNames += qualifiedModuleName;
        }
   }
   
   for(str qualifiedModuleName <- expandedModuleNames){
       try {
       if(!recompile && <true, compressed> := RVMExecutableReadLoc(qualifiedModuleName, pcfg)){
           if(verbose) println("Using <compressed>");
           v = executeProgram(compressed, keywordArguments, debug, debugRVM, true, profile, trace, coverage, jvm, verbose);
       } else {
           if(!recompile){
              throw "No executable found for <qualifiedModuleName>";
              //executables_available = false;
           }
           try {
              RVMModule mainModule = compile(qualifiedModuleName, pcfg, verbose=verbose,enableAsserts=true);
              errors = [ e | e:error(_,_) <- mainModule.messages];
              if(size(errors) > 0){
                 v = [ <at, 0, msg> | error(msg, at) <- errors ];
              } else {
                 v = execute(mainModule, pcfg, keywordArguments=keywordArguments, debug=debug, debugRVM=debugRVM, testsuite=true, profile=profile, verbose=verbose, trace=trace, coverage=coverage, jvm=jvm);
              }
           } catch e: {
             exceptions += "<qualifiedModuleName>: <e>";
             v = [];
           }
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
   return testResults(all_test_results, exceptions); // && executables_available;
}

value rascalTests(list[str] qualifiedModuleNames, PathConfig pcfg, 
                        map[str,value] keywordArguments = (), 
                        bool debug=false, 
                        bool debugRVM=false, 
                        bool recompile=false, 
                        bool profile=false, 
                        bool trace= false,  
                        bool coverage=false, 
                        bool jvm=true, bool 
                        verbose = false){
   trs = rascalTestsRaw(qualifiedModuleNames, pcfg, 
                        keywordArguments=keywordArguments, 
                        debug=debug, 
                        debugRVM=debugRVM, 
                        recompile=recompile, 
                        profile=profile, 
                        trace=trace,
                        coverage=coverage,
                        jvm=jvm,
                        verbose=verbose
                       );
   return makeTestReport(trs);
}

@deprecated
RVMProgram compileAndLink(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin,
                          bool enableAsserts=false, bool jvm=true, bool verbose = false){
    return compileAndLink(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), jvm=jvm, verbose=verbose, enableAsserts=enableAsserts);
}

@deprecated
RVMProgram compileAndLink(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc,
                          bool enableAsserts=false, bool jvm=true, bool verbose = false){
    return compileAndLink(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), reloc=reloc, jvm=jvm, verbose=verbose, enableAsserts=enableAsserts);
}

@deprecated
list[RVMProgram] compileAndLink(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin,
                          bool enableAsserts=false, bool jvm=true, bool verbose = false){
    PathConfig pcfg = pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin); // TODO: type was added for new (experimental) type checker
    return [ compileAndLink(qualifiedModuleName, pcfg, jvm=jvm, verbose=verbose, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];        
} 

@deprecated
list[RVMProgram] compileAndLink(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc,
                          bool enableAsserts=false, bool jvm=true, bool verbose = false){
    return [ compileAndLink(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), reloc=reloc, jvm=jvm, verbose=verbose, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];        
} 

list[RVMProgram] compileAndLink(list[str] qualifiedModuleNames, PathConfig pcfg, 
            loc reloc=|noreloc:///|,
            bool enableAsserts=false, 
            bool jvm=true, 
            bool verbose = false){
    return [ compileAndLink(qualifiedModuleName, pcfg, reloc=reloc, jvm=jvm, verbose=verbose, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];        
}                           

RVMProgram compileAndLink(str qualifiedModuleName, PathConfig pcfg, 
            loc reloc=|noreloc:///|, 
            bool jvm=true,  
            bool enableAsserts=false, 
            bool verbose=false, 
            bool optimize=true){
   startTime = cpuTime();
   mainModule = compile(qualifiedModuleName, pcfg, reloc=reloc, verbose=verbose, enableAsserts=enableAsserts, optimize=optimize);
   if(verbose) println("Compiling: <(cpuTime() - startTime)/1000000> ms");
   start_linking = cpuTime();   
   merged = mergeImports(mainModule, pcfg, verbose=verbose, jvm=jvm);
   link_time = cpuTime() - start_linking;
   if(verbose) println("linking: <link_time/1000000> msec");  
   mergedLoc =  RVMExecutableWriteLoc(mainModule.name, pcfg);   
   linkAndSerializeProgram(mergedLoc, merged, jvm, classRenamings);
   return merged;
}

@deprecated
RVMProgram compileAndMergeIncremental       (str qualifiedModuleName, bool reuseConfig, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool jvm=true, bool verbose = false, bool optimize = true){
    return compileAndMergeProgramIncremental(qualifiedModuleName, reuseConfig, srcs, libs, boot, bin, jvm=jvm, verbose=verbose, optimize=optimize);
}

@deprecated
RVMProgram compileAndMergeProgramIncremental(str qualifiedModuleName, bool reuseConfig, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool jvm=true, bool verbose = false, bool optimize = true){
    return compileAndMergeProgramIncremental(qualifiedModuleName, reuseConfig,  pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), jvm=jvm, verbose=verbose, optimize=optimize);
}

RVMProgram compileAndMergeProgramIncremental(str qualifiedModuleName, bool reuseConfig, PathConfig pcfg, 
            bool jvm=true, 
            bool verbose=false, 
            bool optimize=true){
   //pcfg = pathConfig(srcs=[|std:///|, |memory://test-modules/|], bin=|home:///bin-console|, libs=[|home:///bin-console|]);
   //pcfg = pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin);
   if(!reuseConfig){
      mergedImportLoc = getMergedImportsWriteLoc(qualifiedModuleName, pcfg);
      try {
         remove(mergedImportLoc);
         //println("Removed: <mergedImportLoc>");
      } catch e:
          ;//println("Could not remove: <mergedImportLoc>"); // ignore possible exception
   }
   mainModule = compileIncremental(qualifiedModuleName, reuseConfig, pcfg, verbose=verbose, enableAsserts=true, optimize=optimize); 
   merged = mergeImports(mainModule, pcfg, verbose=verbose, jvm=jvm);

   return merged;
}

str makeTestSummary(lrel[loc,int,str] test_results) = "<size(test_results)> tests executed; < size(test_results[_,0])> failed; < size(test_results[_,2])> ignored";

tuple[bool,str] makeTestReport(TestResults trs){
  test_results = trs.results;
  exceptions = trs.exceptions;
 
  failed = test_results[_,0];
  failed_messages = size(failed) == 0 ? "" :
      "\nFAILED TESTS:
      '<for(<l, 0, msg> <- test_results){>
      '<l>: FALSE <msg>
      <}>";
      
  ignored = test_results[_,2];
  ignored_messages = size(ignored) == 0 ? "" :
      "\nIGNORED TESTS:
      '<for(<l, 2, msg> <- test_results){>
      '<l>: IGNORED
      <}>";
 
  exception_messages = size(exceptions) == 0 ? "" :
     "\nEXCEPTIONS:
     '<for(exc <- exceptions){>
     '<exc>
     '<}>";
  
  messages = "<failed_messages><ignored_messages><exception_messages>
             'TEST SUMMARY: <makeTestSummary(test_results)>";
  return <size(failed) == 0 && size(exceptions) == 0, messages>;
}
