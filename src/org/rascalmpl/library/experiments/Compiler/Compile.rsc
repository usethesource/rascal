@bootstrapParser
module experiments::Compiler::Compile
 
import IO;
import ValueIO;
import Message;
import String;
import ParseTree;
import util::Reflective;
import util::Benchmark;
import util::FileSystem;
import Map;
import Relation;
import Exception;

import lang::rascal::\syntax::Rascal;
//import experiments::Compiler::Rascal2muRascal::ParseModule;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::muRascal2RVM::mu2rvm;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckerConfig;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::CheckModule;
import lang::rascal::types::AbstractName;

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library

tuple[bool,loc] RVMModuleReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "rvm", pcfg);

loc RVMModuleWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "rvm", pcfg);

tuple[bool,loc] RVMExecutableReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "rvmx", pcfg); 

loc RVMExecutableWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "rvmx", pcfg);

tuple[bool,loc] MuModuleReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "mu", pcfg); 
loc MuModuleWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "mu", pcfg);

tuple[bool,loc] ConfigReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "tc", pcfg);
loc ConfigWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "tc", pcfg);

tuple[bool,loc] getMergedImportsReadLoc(str mainQualifiedName, PathConfig pcfg){
    merged_imports_qname = mainQualifiedName + "_imports";
    return getDerivedReadLoc(merged_imports_qname, "rvm", pcfg);
}

loc getMergedImportsWriteLoc(str mainQualifiedName, PathConfig pcfg){
    merged_imports_qname = mainQualifiedName + "_imports";
    return getDerivedWriteLoc(merged_imports_qname, "rvm", pcfg);
}

loc sourceOrImportsLoc(str qualifiedModuleName, PathConfig pcfg){
    try {
        return getModuleLocation(qualifiedModuleName, pcfg);
    } catch ex: {
        if(<true, importsLoc> := cachedImportsReadLoc(qualifiedModuleName, pcfg)){
           return importsLoc;
        }
        throw "No source or imports loc found for <qualifiedModuleName>";
    }
}

bool validRVM(str qualifiedModuleName, PathConfig pcfg){
	<existsRvmLoc, rvmLoc> = RVMModuleReadLoc(qualifiedModuleName, pcfg);
	//println("exists(<rvmLoc>): <exists(rvmLoc)>");
	//println("lastModified(<rvmLoc>) \>= lastModified(<src>): <lastModified(rvmLoc) >= lastModified(src)>");
	res = existsRvmLoc && lastModified(rvmLoc) >= lastModified(sourceOrImportsLoc(qualifiedModuleName, pcfg));
	//println("validRVM(<src>) =\> <res>");
	return res;
}

tuple[Configuration, RVMModule] compile1(str qualifiedModuleName, PathConfig pcfg, loc reloc = |noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){

	Configuration config;
    lang::rascal::\syntax::Rascal::Module M;
    int check_time;
    int comp_time;
    loc moduleLoc;
    rvmModuleLoc = RVMModuleWriteLoc(qualifiedModuleName, pcfg);
    try {
        moduleLoc = getModuleLocation(qualifiedModuleName, pcfg);
    } catch e: {
        <existsConfig, configLoc> = ConfigReadLoc(qualifiedModuleName, pcfg);
        if(exists(rvmModuleLoc) && existsConfig){
            config = readBinaryValueFile(#Configuration, configLoc);
            rvmMod = readBinaryValueFile(#RVMModule, rvmModuleLoc);
            if(verbose) println("No source found for <qualifiedModuleName>, reusing existing binary");
            return <config, rvmMod>;
        }
        rvmMod = errorRVMModule(qualifiedModuleName, {error("Module not found: <qualifiedModuleName>", |unknown:///|)}, |unknown:///|);
        writeBinaryValueFile(rvmModuleLoc, rvmMod);
        return <newConfiguration(pcfg), rvmMod>;
    }
   	try {
   	    if(verbose) println("rascal2rvm: <moduleLoc>");
   	    start_checking = cpuTime();
   		//M = parse(#start[Module], moduleLoc).top;
   		M = parseModule(moduleLoc);
   	    config  = checkModule(M, newConfiguration(pcfg), verbose=verbose);
   	    if(reloc != |noreloc:///|){
   	        configWriteLoc = ConfigWriteLoc(qualifiedModuleName, pcfg);
   	        writeBinaryValueFile(configWriteLoc, relocConfig(config, reloc, pcfg.srcs));
   	    }
   	    check_time = (cpuTime() - start_checking)/1000000;
   	} catch e: {
   	    throw e;
   	}
   	errors = [ e | e:error(_,_) <- config.messages];
   	warnings = [ w | w:warning(_,_) <- config.messages ];
   
   
    
   	if(size(errors) > 0) {
   		rvmMod = errorRVMModule("<M.header.name>", config.messages, moduleLoc);
   		writeBinaryValueFile(rvmModuleLoc, rvmMod);
   	    return <config, rvmMod>;
   	}
   	
    //if(verbose) println("rascal2rvm: Compiling <moduleLoc>");
    start_comp = cpuTime();
   	muMod = r2mu(M, config, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);

   	
    rvmMod = mu2rvm(muMod, verbose=verbose, optimize=optimize); 
    comp_time = (cpuTime() - start_comp)/1000000;
    if(verbose) println("Compiling <moduleLoc>: check: <check_time>, compile: <comp_time>, total: <check_time+comp_time> ms");
    //if(verbose) println("compile: Writing RVMModule <rvmModuleLoc>");
    writeBinaryValueFile(rvmModuleLoc, rvmMod);
    return <config, rvmMod>;  
}	

@doc{Compile a Rascal source module (given at a location) to RVM}
RVMModule compile(loc moduleLoc, PathConfig pcfg, loc reloc = |noreloc:///|, bool verbose=false, bool optimize=true, bool enableAsserts=false) =
    compile(getModuleName(moduleLoc, pcfg), pcfg, reloc=reloc, verbose = verbose, optimize=optimize, enableAsserts=enableAsserts);


@doc{Compile a Rascal source module (given at a location) to RVM}
RVMModule compile(str qualifiedModuleName, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
	<cfg, rvmMod> = compile1(qualifiedModuleName, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
	// TODO: JV worrying side-effect noticed here:
	rvmMod1 = recompileDependencies(qualifiedModuleName, rvmMod, cfg, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
    return rvmMod;
}

list[RVMModule] compileAll(loc moduleRoot, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return [ compile(getModuleName(moduleLoc, pcfg), pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts) | moduleLoc <- find(moduleRoot, "rsc")];
}


list[RVMModule] compile(list[loc] moduleLocs, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return [ compile(getModuleName(moduleLoc, pcfg), pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts) | moduleLoc <- moduleLocs ];
}

list[RVMModule] compile(list[str] qualifiedModuleNames, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return [ compile(qualifiedModuleName, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];
}

@deprecated
RVMModule compile(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return compile(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
}

@deprecated
RVMModule compile(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return compile(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
}
@deprecated
list[RVMModule] compile(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    pcfg =  pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin);
    return [ compile(qualifiedModuleName, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];
}

@deprecated
list[RVMModule] compile(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    pcfg =  pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin);
    return [ compile(qualifiedModuleName, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];
}

RVMModule recompileDependencies(str qualifiedModuleName, RVMModule rvmMod, Configuration cfg, PathConfig pcfg, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    errors = [ e | e:error(_,_) <- cfg.messages];
    warnings = [ w | w:warning(_,_) <- cfg.messages ];
   
    if(size(errors) > 0) {
        return rvmMod;
    }
    messages = {};
    
    dirtyModules = { prettyPrintName(dirty) | dirty <- cfg.dirtyModules };
   
    if(verbose){
       println("dirtyModules:");
       for(m1 <- dirtyModules) println("\t<m1>");
       
       println("importGraph:");
       for(<m1, m2> <- cfg.importGraph){
           println("\t<prettyPrintName(m1)> imports <prettyPrintName(m2)>");
       }
    }
        
    allDependencies = { prettyPrintName(rname) | rname <- carrier(cfg.importGraph) } - qualifiedModuleName;
    
    bool atLeastOneRecompiled = false;
    for(dependency <- allDependencies){
        if(dependency in dirtyModules || !validRVM(dependency, pcfg)){
           <cfg1, rvmMod1> = compile1(dependency, pcfg, optimize=optimize, enableAsserts=enableAsserts);
           atLeastOneRecompiled = true;
           messages += cfg1.messages;
        }
    }
    
    clearDirtyModules(qualifiedModuleName, pcfg);
    
    errors = [ e | e:error(_,_) <- messages];
    warnings = [ w | w:warning(_,_) <- messages ];
    
    if(size(errors) > 0) {
        return errorRVMModule(rvmMod.name, messages, getModuleLocation(qualifiedModuleName, pcfg));
    }
    if(atLeastOneRecompiled){
       mergedLoc = getMergedImportsWriteLoc(qualifiedModuleName, pcfg);
       try {
           if(verbose) println("Removing <mergedLoc>");
           remove(mergedLoc);
       } catch e: {
           println("Could not remove <mergedLoc>: <e>");
        }
    }
   
    return rvmMod ;
}

// Assumption: main declaration is the last one
lang::rascal::\syntax::Rascal::Declaration getMain(lang::rascal::\syntax::Rascal::Module m){
    if(m2: (Module) `<Header h> <Toplevel* pre> <Toplevel tl_main>` := m){
       return tl_main.declaration;
    }
    throw "getMain: cannot match toplevels";
}

Module removeMain(lang::rascal::\syntax::Rascal::Module m) {
    if(m2: (Module) `<Header h> <Toplevel* pre> <Toplevel mn>` := m){
       return (Module) `<Header h> <Toplevel* pre>`;
    }
    throw "removeMain: no main found";
    return m;
}

Configuration noPreviousConfig = newConfiguration(pathConfig());
Configuration previousConfig = noPreviousConfig;

tuple[Configuration, RVMModule] compile1Incremental(str qualifiedModuleName, bool reuseConfig, PathConfig pcfg, bool verbose = false, bool optimize=true, bool enableAsserts=false){

    Configuration config;
    lang::rascal::\syntax::Rascal::Module M;
    int check_time;
    int comp_time;
    
    moduleLoc = getModuleLocation(qualifiedModuleName, pcfg);
    try {
        if(verbose) println("rascal2rvm: Parsing and incremental checking <moduleLoc>");
        start_checking = cpuTime();
        
        M = parseModule(moduleLoc);
        
        if(!reuseConfig || previousConfig == noPreviousConfig){
            lang::rascal::\syntax::Rascal::Module M1 = removeMain(M);
            previousConfig = checkModule(M1, newConfiguration(pcfg), verbose=verbose);
            previousConfig.stack = [0]; // make sure we are in the module scope
        } else {
          previousConfig.dirtyModules = {};
        }
        mainDecl = getMain(M);
        config  = checkDeclaration(mainDecl, true, previousConfig);
       
        check_time = (cpuTime() - start_checking)/1000000;
    } catch e: {
        throw e;
    }
    errors = [ e | e:error(_,_) <- config.messages];
    warnings = [ w | w:warning(_,_) <- config.messages ];
   
    if(size(errors) > 0) {
        rvmMod = errorRVMModule("<M.header.name>", config.messages, moduleLoc);
        return <config, rvmMod>;
    }
    
    rvmModuleLoc = RVMModuleWriteLoc(qualifiedModuleName, pcfg);
    
    if(verbose) println("rascal2rvm: Compiling <moduleLoc>");
    start_comp = cpuTime();
    muMod = r2mu(M, config, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);  // never a reloc
    
    rvmMod = mu2rvm(muMod, verbose=verbose, optimize=optimize); 
    comp_time = (cpuTime() - start_comp)/1000000;
    if(verbose) println("Compiling <moduleLoc>: check: <check_time>, compile: <comp_time>, total: <check_time+comp_time> ms");
    if(verbose) println("compile: Writing RVMModule <rvmModuleLoc>");
    writeBinaryValueFile(rvmModuleLoc, rvmMod);
   
    return <config, rvmMod>;  
}  

RVMModule compileIncremental(str qualifiedModuleName, bool reuseConfig, PathConfig pcfg, bool verbose = false, bool optimize = true, bool enableAsserts=false){
    <cfg, rvmMod> = compile1Incremental(qualifiedModuleName, reuseConfig, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
    return recompileDependencies(qualifiedModuleName, rvmMod, cfg, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
}

set[str] getIncrementalVocabulary(){
    if(previousConfig == noPreviousConfig){
        return {};
    }
    return {name | /RSimpleName(str name) := previousConfig};
}  
