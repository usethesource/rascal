@bootstrapParser
module experiments::Compiler::Examples::Tst5
 

import IO;
import ValueIO;
import String;
import Message;
import ParseTree;
import util::Reflective;
import util::Benchmark;
import Map;
import Relation;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::Rascal2muRascal::ParseModule;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::muRascal2RVM::mu2rvm;

import lang::rascal::types::CheckerConfig;

import lang::rascal::types::TestChecker;
//import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library

tuple[bool,loc] RVMModuleReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "rvm.gz", pcfg);

loc RVMModuleWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "rvm.gz", pcfg);

//tuple[bool,loc] RVMExecutableReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "rvm.ser.gz", pcfg); 

//tuple[bool,loc] RVMExecutableCompressedReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "rvm.ser.gz", pcfg); 

//loc RVMExecutableCompressedWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "rvm.ser.gz", pcfg);

//tuple[bool,loc] MuModuleReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "mu", pcfg); 
//loc MuModuleWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "mu", pcfg);

//tuple[bool,loc] ConfigReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "tc", pcfg);

//tuple[bool,loc] getMergedImportsReadLoc(str mainQualifiedName, PathConfig pcfg){
//    merged_imports_qname = mainQualifiedName + "_imports";
//    return getDerivedReadLoc(merged_imports_qname, "rvm.gz", pcfg);
//}

//loc getMergedImportsWriteLoc(str mainQualifiedName, PathConfig pcfg){
//    merged_imports_qname = mainQualifiedName + "_imports";
//    return getDerivedWriteLoc(merged_imports_qname, "rvm.gz", pcfg);
//}


//bool validRVM(str qualifiedModuleName, PathConfig pcfg){
//    <existsRvmLoc, rvmLoc> = RVMModuleReadLoc(qualifiedModuleName, pcfg);
//    //println("exists(<rvmLoc>): <exists(rvmLoc)>");
//    //println("lastModified(<rvmLoc>) \>= lastModified(<src>): <lastModified(rvmLoc) >= lastModified(src)>");
//    res = existsRvmLoc && lastModified(rvmLoc) >= lastModified(getModuleLocation(qualifiedModuleName, pcfg));
//    //println("validRVM(<src>) =\> <res>");
//    return res;
//}

tuple[Configuration, RVMModule] compile1(str qualifiedModuleName, PathConfig pcfg, bool verbose = true){

    Configuration config;
    lang::rascal::\syntax::Rascal::Module M;
    int check_time;
    int comp_time;
    loc moduleLoc = getModuleLocation(qualifiedModuleName, pcfg);
    try {
        if(verbose) println("rascal2rvm: Parsing and checking <moduleLoc>");
        start_checking = cpuTime();
        //M = parse(#start[Module], moduleLoc).top;
        M = parseModuleGetTop(moduleLoc);
        //config  = checkModule(M, newConfiguration(pcfg));
        check_time = (cpuTime() - start_checking)/1000000;
    } catch e: {
        throw e;
    }
    errors = [ e | e:error(_,_) <- config.messages];
    warnings = [ w | w:warning(_,_) <- config.messages ];
   
    rvmModuleLoc = RVMModuleWriteLoc(qualifiedModuleName, pcfg);
    
    if(size(errors) > 0) {
        rvmMod = errorRVMModule("<M.header.name>", config.messages, moduleLoc);
        writeBinaryValueFile(rvmModuleLoc, rvmMod);
        return <config, rvmMod>;
    }
    
    if(verbose) println("rascal2rvm: Compiling <moduleLoc>");
    start_comp = cpuTime();
    muMod = r2mu(M, config, verbose=verbose);
    
    rvmMod = mu2rvm(muMod, verbose=verbose); 
    comp_time = (cpuTime() - start_comp)/1000000;
    println("Compiling <moduleLoc>: check: <check_time>, compile: <comp_time>, total: <check_time+comp_time> ms");
    if(verbose) println("compile: Writing RVMModule <rvmModuleLoc>");
    writeBinaryValueFile(rvmModuleLoc, rvmMod);
    return <config, rvmMod>;  
}   

//RVMModule compile(str qualifiedModuleName, PathConfig pcfg, bool verbose = false){
//    return compile(qualifiedModuleName, pcfg, verbose=verbose);
//}

//@doc{Compile a Rascal source module (given at a location) to RVM}
//RVMModule compile(loc moduleLoc, PathConfig pcfg, bool verbose = false) =
//    compile(getModuleName(moduleLoc, pcfg), pcfg, verbose = verbose);
//
//@doc{Compile a Rascal source module (given at a location) to RVM}
//RVMModule compile(str qualifiedModuleName, PathConfig pcfg, bool verbose = false){
//    <cfg, rvmMod> = compile1(qualifiedModuleName, pcfg, verbose=verbose);
//    //rvmMod1 = recompileDependencies(qualifiedModuleName, rvmMod, cfg, pcfg, verbose=verbose);
//    errors = [ e | e:error(_,_) <- rvmMod.messages];
//    warnings = [ w | w:warning(_,_) <- rvmMod.messages ];
//    for(msg <- rvmMod.messages){
//        if(error(txt, src) := msg) println("[error] <txt> at <src>");
//        if(warning(txt, src) := msg) println("[warning] <txt> at <src>");
//        if(info(txt, src) := msg) println("[info] <txt> at <src>");
//    }
//    return rvmMod;
//}

//RVMModule compile(str qualifiedModuleName, list[loc] srcPath, list[loc] libPath, loc bootDir, loc binDir, bool verbose = false){
//    return compile(qualifiedModuleName, pathConfig(srcPath=srcPath, libPath=libPath, bootDir=bootDir, binDir=binDir), verbose=verbose);
//}

//RVMModule recompileDependencies(str qualifiedModuleName, RVMModule rvmMod, Configuration cfg, PathConfig pcfg, bool verbose = false){
//    errors = [ e | e:error(_,_) <- cfg.messages];
//    warnings = [ w | w:warning(_,_) <- cfg.messages ];
//   
//    if(size(errors) > 0) {
//        return rvmMod;
//    }
//    messages = {};
//    
//    dirtyModules = { prettyPrintName(dirty) | dirty <- cfg.dirtyModules };
//   
//    if(verbose){
//       println("dirtyModules:");
//       for(m1 <- dirtyModules) println("\t<m1>");
//       
//       println("importGraph:");
//       for(<m1, m2> <- cfg.importGraph){
//           println("\t<prettyPrintName(m1)> imports <prettyPrintName(m2)>");
//       }
//    }
//        
//    allDependencies = { prettyPrintName(rname) | rname <- carrier(cfg.importGraph) } - qualifiedModuleName;
//    
//    bool atLeastOneRecompiled = false;
//    for(dependency <- allDependencies){
//        if(dependency in dirtyModules || !validRVM(dependency, pcfg)){
//           <cfg1, rvmMod1> = compile1(dependency, pcfg);
//           atLeastOneRecompiled = true;
//           messages += cfg1.messages;
//        }
//    }
//    
//    clearDirtyModules(qualifiedModuleName, pcfg);
//    
//    errors = [ e | e:error(_,_) <- messages];
//    warnings = [ w | w:warning(_,_) <- messages ];
//    
//    if(size(errors) > 0) {
//        return errorRVMModule(rvmMod.name, messages, getModuleLocation(qualifiedModuleName, pcfg));
//    }
//    if(atLeastOneRecompiled){
//       mergedLoc = getMergedImportsWriteLoc(qualifiedModuleName, pcfg);
//       try {
//           println("Removing <mergedLoc>");
//           remove(mergedLoc);
//       } catch e: {
//           println("Could not remove <mergedLoc>: <e>");
//        }
//    }
//   
//    return rvmMod ;
//}
