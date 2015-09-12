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

//import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Load;

extend experiments::Compiler::RVM::AST;   // Strange: using import here, gives RVMModule not defined errors later on
import experiments::Compiler::RVM::ExecuteProgram;
import experiments::Compiler::Compile;

//import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes; // redundant!

import experiments::Compiler::muRascal2RVM::mu2rvm;
import experiments::Compiler::muRascal2RVM::StackValidator;
import experiments::Compiler::muRascal2RVM::PeepHole;
import util::Reflective;

public loc MuLibrary = getSearchPathLocation("experiments/Compiler/muRascal2RVM/Library.mu");

loc getMuLibraryCompiled(loc bindir = |home:///bin|) = getDerivedLocation(MuLibrary, "rvm.gz", compressed=true, bindir = bindir);

loc getMergedImportsLocation(loc mainSourceLoc, loc bindir = |home:///bin|){
    merged_imports_src = mainSourceLoc[path = replaceLast(mainSourceLoc.path, ".rsc", "-imports.rsc")];
    return getDerivedLocation(merged_imports_src, "rvm.gz", compressed=true, bindir = bindir);
}

public list[loc] defaultImports = [];  //[|std:///Exception.rsc|, |std:///ParseTree.rsc| ];

alias Resolved = tuple[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors];

map[loc,tuple[datetime modified, RVMModule program]] importCache = ();

RVMModule getImport(loc importedLoc){
    lastMod = lastModified(importedLoc);
    if(importCache[importedLoc]?){
       <lastModifiedAtImport, program> = importCache[importedLoc];
       if(lastMod <= lastModifiedAtImport){
          //println("getImport: use cached version for <importedLoc>");
          return program;
       }
    }
    program = readBinaryValueFile(#RVMModule, importedLoc);
    importCache[importedLoc] = <lastMod, program>;
    //println("getImport: import new version for <importedLoc>");
    return program;
}
 
list[experiments::Compiler::RVM::AST::Declaration] parseMuLibrary(bool verbose = false, loc bindir = |home:///bin|){
    if(verbose) println("execute: Recompiling library <basename(MuLibrary)>.mu");
    MuLibraryCompiled = getMuLibraryCompiled(bindir = bindir);
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
  
    writeBinaryValueFile(MuLibraryCompiled, functions);
    if(verbose) println("execute: Writing compiled version of library <MuLibraryCompiled>");
    
    return functions; 
}

bool valid(loc mergedImportsLoc, RVMModule mergedProgram, RVMModule program, loc bindir = |home:///bin|){

    if(mergedProgram.imports == program.imports && mergedProgram.extends == program.extends){
        mm = lastModified(mergedImportsLoc);
        for(imp <- mergedProgram.imports){
            if(lastModified(RVMModuleLocation(getModuleLocation(imp), bindir)) > mm){
                return false;
            }
        }
        return true;
    }
    return false;
}

alias MergedImports = tuple[RVMModule mainProgram,
                            map[str,map[str,str]] imported_module_tags,
                            map[str,Symbol] imported_types,
                            list[experiments::Compiler::RVM::AST::Declaration] imported_declarations,
                            lrel[str name, Symbol funType, str scope, list[str] ofunctions,list[str] oconstructors] imported_overloaded_functions,
                            map[str,int] imported_overloading_resolvers];

MergedImports mergeImports0(RVMModule mainProgram, bool useJVM = false, bool serialize=false, bool verbose = false, loc bindir = |home:///bin|){
                        
   map[str,map[str,str]] imported_moduleTags = ();
   map[str,Symbol] imported_types = ();
   list[experiments::Compiler::RVM::AST::Declaration] imported_declarations = [];
   lrel[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors] imported_overloaded_functions = [];
   map[str,int] imported_overloading_resolvers = ();
   set[Message] messages = mainProgram.messages;
   
   if(any(msg <- messages, error(_,_) := msg)){
        for(msg <- messages){
            println(msg);
        }
        throw messages;
   }
   
   mergedImportsLoc = getMergedImportsLocation(mainProgram.src, bindir=bindir);
   if(exists(mergedImportsLoc)){
      startTime = cpuTime();
      rvmMergedImports = readBinaryValueFile(#RVMModule, mergedImportsLoc);
      println("Reading: <mergedImportsLoc>: <(cpuTime() - startTime)/1000000>ms");
      if(valid(mergedImportsLoc, rvmMergedImports, mainProgram)){
         return <mainProgram,
                 rvmMergedImports.module_tags,
                 rvmMergedImports.types,
                 rvmMergedImports.declarations,
                 rvmMergedImports.overloaded_functions, 
                 rvmMergedImports.resolver>;
           }
   }
   
   // Read the muLibrary, recompile if necessary
   //println("MuLibrary: <MuLibrary>");
   MuLibraryCompiled = getMuLibraryCompiled(bindir = bindir);
   //println("MuLibraryCompiled: <MuLibraryCompiled>");
   if(exists(MuLibraryCompiled) && lastModified(MuLibraryCompiled) > lastModified(MuLibrary)){
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
   
   void processImports(RVMModule rvmProgram) {
       list[str] orderedImports = reverse(order(rvmProgram.importGraph))/* - rvmProgram.name*/;
       //println("Ordered import graph <rvmProgram.name>: <orderedImports>");
       
       for(str impName <- orderedImports) {
          // println("execute: IMPORT <impName>");
           
           imp = getModuleLocation(impName);
           importedLoc = RVMModuleLocation(imp, bindir);
           try {
               RVMModule importedRvmProgram = getImport(importedLoc);
               
               extensions = {};
                
               for(ext <- importedRvmProgram.extends){
                  // println("execute: <importedRvmProgram.name> EXTENDS <ext>");
                   extensions += {<importedRvmProgram.name, ext>};
               }
           
               messages += importedRvmProgram.messages;
               //imported_moduleTags[importedRvmProgram.name] = importedRvmProgram.tags;
               imported_moduleTags += importedRvmProgram.module_tags;
               
               // Temporary work around related to issue #343
               importedRvmProgram = visit(importedRvmProgram) { case type[value] t : { insert type(t.symbol,t.definitions); }}
              
               imported_types = imported_types + importedRvmProgram.types;
               new_declarations = importedRvmProgram.declarations;
               
               if(!isEmpty(extensions)){
                    extending_modules += extensions;
                    //println("extending_modules = <extending_modules>");
                    resolve_module_extensions(importedRvmProgram.name, imported_declarations, new_declarations);
               }    
               
               imported_declarations += new_declarations;
               
               // Merge overloading functions and resolvers: all indices in the current resolver have to be incremented by the number of imported overloaded functions
               pos_delta = size(imported_overloaded_functions); 
               imported_overloaded_functions = imported_overloaded_functions + importedRvmProgram.overloaded_functions;
               imported_overloading_resolvers = imported_overloading_resolvers + ( ofname : (importedRvmProgram.resolver[ofname] + pos_delta) | str ofname <- importedRvmProgram.resolver );
           
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
   
   processImports(mainProgram);
  
   if(any(msg <- messages, error(_,_) := msg)){
        for(e: error(_,_) <- messages){
            println(e);
        }
        throw "Cannot execute due to compilation errors";
   }
   
   rvmMergedImports =
   rvm(mainProgram.name,
            imported_moduleTags,
            messages,
            mainProgram.imports,
            mainProgram.extends,
            imported_types, 
            mainProgram.symbol_definitions,
            imported_declarations,
            mainProgram.initialization, 
            imported_overloading_resolvers, 
            imported_overloaded_functions,
            mainProgram.importGraph,
            mainProgram.src,
            linked(now())
            );
 
   //if(serialize){        
      writeBinaryValueFile(mergedImportsLoc, rvmMergedImports);
   //}
   
  return <mainProgram,
           rvmMergedImports.module_tags,
           rvmMergedImports.types,
           rvmMergedImports.declarations,
           rvmMergedImports.overloaded_functions, 
           rvmMergedImports.resolver>;
}

MergedImports mergeImports(RVMModule mainProgram, bool useJVM = false, bool serialize=false, bool verbose = false, loc bindir = |home:///bin|){

    merged = mergeImports0(mainProgram, verbose=verbose, useJVM=useJVM, bindir=bindir);
    pos_delta = size(merged.imported_overloaded_functions);
    mainProgram.resolver = ( ofname : mainProgram.resolver[ofname] + pos_delta | str ofname <- mainProgram.resolver );
   
    return <mainProgram,
           merged.imported_module_tags,
           merged.imported_types,
           merged.imported_declarations,
           merged.imported_overloaded_functions, 
           merged.imported_overloading_resolvers>;
}

tuple[value, num] execute_and_time(RVMModule mainProgram, list[value] arguments, bool debug=false, 
                                    bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, 
                                    bool coverage = false, bool useJVM = false, bool serialize=false, bool verbose = false, loc bindir = |home:///bin|){
    start_loading = cpuTime();   
    merged = mergeImports(mainProgram, verbose=verbose, useJVM=useJVM, bindir=bindir);
    load_time = cpuTime() - start_loading;
    println("mergeImports: <load_time/1000000> msec");
    <v, t> = executeProgram(RVMExecutableLocation(mainProgram.src, bindir),
                           merged.mainProgram, 
                           merged.imported_module_tags,
                           merged.imported_types,
                           merged.imported_declarations, 
                           merged.imported_overloaded_functions, 
                           merged.imported_overloading_resolvers, 
                           arguments, 
                           debug, 
                           testsuite, 
                           profile, 
                           trackCalls, 
                           coverage,
                           useJVM,
                           serialize);
   //if(!testsuite && verbose){
        println("Result = <v>, [load: <load_time/1000000> msec, execute: <t> msec]");
  // }    
   return <v, t>;                            
}

void linkProgram(loc rascalSource, bool verbose = false, bool useJVM = false, loc bindir = |home:///bin|){
    rvmLoc = RVMModuleLocation(rascalSource, bindir);
        
    RVMModule mainProgram = readBinaryValueFile(#RVMModule, rvmLoc);
    merged = mergeImports(mainProgram, verbose=verbose, useJVM=useJVM, bindir=bindir);
    linkProgram(RVMExecutableLocation(mainProgram.src, bindir),
                mainProgram, 
                merged.imported_module_tags,
                merged.imported_types,
                merged.imported_declarations, 
                merged.imported_overloaded_functions, 
                merged.imported_overloading_resolvers,
                useJVM);
}

value execute(RVMModule mainProgram, list[value] arguments, bool debug=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false, bool coverage=false, bool useJVM=false, bool serialize=false, bool verbose=true, loc bindir = |home:///bin|){
    <v, t> = execute_and_time(mainProgram, arguments, debug=debug, testsuite=testsuite,recompile=recompile, profile=profile, trackCalls=trackCalls, coverage=coverage, useJVM=useJVM, serialize=serialize, verbose=verbose, bindir=bindir);
    //if(testsuite){
 //        return printTestReport(v);
 //   }
   return v;
}

value execute(loc rascalSource, list[value] arguments, bool debug=false, bool testsuite=false, bool recompile=true, bool profile=false, bool trackCalls= false,  bool coverage=false, bool useJVM=false, bool serialize=true, bool verbose = false, loc bindir = |home:///bin|){
   if(!recompile){
      executable = RVMExecutableLocation(rascalSource, bindir);
      compressed = RVMExecutableCompressedLocation(rascalSource, bindir);
      if(exists(compressed)){
         if(verbose) println("Using <compressed>");
         <v, t> = executeProgram(compressed, arguments, debug, testsuite, profile, trackCalls, coverage, useJVM);
         println("Executing (not recompiled): <t> ms");
         if(!testsuite && verbose){
            println("Result = <v>, [execute: <t> msec]");
         }  
         return v;
      }
   }
   startTime = cpuTime();
   mainProgram = compile(rascalSource, verbose=verbose, bindir=bindir);
   println("Compiling: <(cpuTime() - startTime)/1000000> ms");
   //<cfg, mainProgram> = compile(rascalSource, bindir=bindir);
   startTime = cpuTime();
   v = execute(mainProgram, arguments, debug=debug, testsuite=testsuite, profile=profile, verbose=verbose, bindir=bindir, trackCalls=trackCalls, coverage=coverage, useJVM=useJVM, serialize=serialize);
   println("Executing: <(cpuTime() - startTime)/1000000> ms");
   return v;
}

value executeBinary(loc executable, list[value] arguments, bool debug=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls= false,  bool coverage=false, bool useJVM = false, bool verbose = true, loc bindir = |home:///bin|){
  if(exists(executable)){
     if(verbose) println("Using <executable>");
     <v, t> = executeProgram(executable, arguments, debug, testsuite, profile, trackCalls, coverage, useJVM);
    
     if(!testsuite && verbose){
        println("Result = <v>, [execute: <t> msec]");
     }  
     return v;
  }
}

//value execute(str rascalSource, list[value] arguments, bool debug=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls=false,  bool coverage=false, loc bindir = |home:///bin|){
//   mainProgram = compile(rascalSource, recompile=recompile, bindir=bindir);
//   return execute(mainProgram, arguments, debug=debug, testsuite=testsuite,profile=profile, bindir = bindir, trackCalls=trackCalls, coverage=coverage);
//}

tuple[value, num] execute_and_time(loc rascalSource, list[value] arguments, bool debug=false, bool testsuite=false, bool recompile=false, bool profile=false, bool trackCalls=false,  bool coverage=false, bool useJVM=false, bool serialize=false, bool verbose = true, loc bindir = |home:///bin|){
   mainProgram = compile(rascalSource, bindir=bindir);
   return execute_and_time(mainProgram, arguments, debug=debug, testsuite=testsuite, profile=profile, verbose = verbose, bindir = bindir, trackCalls=trackCalls, coverage=coverage, useJVM=useJVM, serialize=serialize);
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

value main(list[value] args) {
   println("Execute.main: <args>");
   if(loc src := args[0]){
      return execute(src, []);
   }
   throw "Cannot execute <args[0]>";
}
