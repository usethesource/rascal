@bootstrapParser
module experiments::Compiler::Compile
 
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

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library

loc RVMModuleLocation(loc src, loc bindir) = getDerivedLocation(src, "rvm.gz", bindir = bindir, compressed=true); //(bindir + src.path)[extension="rvm"];

loc RVMExecutableLocation(loc src, loc bindir) = getDerivedLocation(src, "rvm.ser.gz", bindir = bindir, compressed=true); //(bindir + src.path)[extension="rvm.ser.xz"];
loc RVMExecutableCompressedLocation(loc src, loc bindir) = getDerivedLocation(src, "rvm.ser.gz", bindir = bindir, compressed=true); //(bindir + src.path)[extension="rvm.ser.xz"];


loc MuModuleLocation(loc src, loc bindir) = getDerivedLocation(src, "mu", bindir = bindir); //(bindir + src.path)[extension="mu"];

loc ConfigLocation(loc src, loc bindir) = getDerivedLocation(src, "tc", bindir = bindir);


bool validRVM(loc src, loc bindir = |home:///bin|){
	rvmLoc = RVMModuleLocation(src, bindir);
	//println("exists(<rvmLoc>): <exists(rvmLoc)>");
	//println("lastModified(<rvmLoc>) \>= lastModified(<src>): <lastModified(rvmLoc) >= lastModified(src)>");
	res = exists(rvmLoc) && lastModified(rvmLoc) >= lastModified(src);
	//println("validRVM(<src>) =\> <res>");
	return res;
}

tuple[Configuration, RVMModule] compile1(loc moduleLoc, bool verbose = true, loc bindir = |home:///bin|){

	Configuration config;
    lang::rascal::\syntax::Rascal::Module M;
    int check_time;
    int comp_time;
   	try {
   	    if(verbose) println("rascal2rvm: Parsing and checking <moduleLoc>");
   	    start_checking = cpuTime();
   		//M = parse(#start[Module], moduleLoc).top;
   		M = parseModuleGetTop(moduleLoc);
   	    config  = checkModule(M, newConfiguration()/*, verbose=verbose, bindir=bindir*/);
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
   	
   	rvmModuleLoc = RVMModuleLocation(moduleLoc, bindir);
   	
    if(verbose) println("rascal2rvm: Compiling <moduleLoc>");
    start_comp = cpuTime();
   	muMod = r2mu(M, config, verbose=verbose);
   	
    rvmMod = mu2rvm(muMod, verbose=verbose); 
    comp_time = (cpuTime() - start_comp)/1000000;
    println("Compiling <moduleLoc>: check: <check_time>, compile: <comp_time>, total: <check_time+comp_time> ms");
    if(verbose) println("compile: Writing RVMModule <rvmModuleLoc>");
    writeBinaryValueFile(rvmModuleLoc, rvmMod);
    for(msg <- rvmMod.messages){
        println(msg);
    }
    return <config, rvmMod>;  
}	

RVMModule compile(str moduleName, bool verbose = false, loc bindir = |home:///bin|){
    return compile(getModuleLocation(moduleName), verbose=verbose, bindir=bindir);
}

@doc{Compile a Rascal source module (given at a location) to RVM}

RVMModule compile(loc moduleLoc, bool verbose = false, loc bindir = |home:///bin|){

	<cfg, rvmMod> = compile1(moduleLoc, verbose=verbose, bindir=bindir);
   
   	errors = [ e | e:error(_,_) <- cfg.messages];
   	warnings = [ w | w:warning(_,_) <- cfg.messages ];
   
   	if(size(errors) > 0) {
   	    return rvmMod;
   	}
   	messages = {};
   	
   	dirtyModulesLoc = { getModuleLocation(prettyPrintName(dirty)) | dirty <- cfg.dirtyModules };
   
   	if(verbose){
   	   for(<m1, m2> <- cfg.importGraph){
   		   println("<prettyPrintName(m1)> imports <prettyPrintName(m2)>");
   	   }
   	}
   	
   	//importedByStar = invert(cfg.importGraph)*;
   	  	
   	allDependencies = { getModuleLocation(prettyPrintName(rname)) | rname <- carrier(cfg.importGraph) } - moduleLoc;
   	
    for(dependency <- allDependencies){
        if(dependency in dirtyModulesLoc || !validRVM(dependency)){
    	   <cfg1, rvmMod1> = compile1(dependency, bindir=bindir);
    	   messages += cfg1.messages;
        }
    }
   
   	clearDirtyModules(moduleLoc, bindir);
   	
   	errors = [ e | e:error(_,_) <- messages];
    warnings = [ w | w:warning(_,_) <- messages ];
    
    if(size(errors) > 0) {
        return errorRVMModule(rvmMod.name, messages, moduleLoc);
    }
    
   	return rvmMod ;
}

// Assumption: main declaration is the last one
lang::rascal::\syntax::Rascal::Declaration getMain(lang::rascal::\syntax::Rascal::Module m){
    if(appl(regular(Symbol def), list[Tree] args) := m.body.toplevels){
       if(Toplevel tl := args[-1]){
          return tl.declaration;
       }
    }
    throw "Cannot match toplevels";
}

Module removeMain((Module) `<Header h> <Toplevel* pre> <Toplevel _>`) = (Module) `<Header h> <Toplevel* pre>`;

Configuration previousConfig;

tuple[Configuration, RVMModule] compile1Incremental(loc moduleLoc, bool reuseConfig, bool verbose = true, loc bindir = |home:///bin|){

    Configuration config;
    lang::rascal::\syntax::Rascal::Module M;
    int check_time;
    int comp_time;
    try {
        if(verbose) println("rascal2rvm: Parsing and incremental checking <moduleLoc>");
        start_checking = cpuTime();
        //M = parse(#start[Module], moduleLoc).top;
        M = parseModuleGetTop(moduleLoc);
        if(!reuseConfig || !previousConfig?){
            M1 = removeMain(M);
            previousConfig = checkModule(M1, newConfiguration());
            previousConfig.stack = [0]; // make sure we are in the module scope
        }
        mainDecl = getMain(M);
        println("<mainDecl>");
        
        config  = checkDeclaration(mainDecl, true, previousConfig /*, verbose=verbose, bindir=bindir*/);
        println("checkDeclaration: done");
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
    
    rvmModuleLoc = RVMModuleLocation(moduleLoc, bindir);
    
    if(verbose) println("rascal2rvm: Compiling <moduleLoc>");
    start_comp = cpuTime();
    muMod = r2mu(M, config, verbose=verbose);
    
    rvmMod = mu2rvm(muMod, verbose=verbose); 
    comp_time = (cpuTime() - start_comp)/1000000;
    println("Compiling <moduleLoc>: check: <check_time>, compile: <comp_time>, total: <check_time+comp_time> ms");
    if(verbose) println("compile: Writing RVMModule <rvmModuleLoc>");
    writeBinaryValueFile(rvmModuleLoc, rvmMod);
    for(msg <- rvmMod.messages){
        println(msg);
    }
    return <config, rvmMod>;  
}  

RVMModule compileIncremental(loc moduleLoc, bool reuseConfig, bool verbose = false, loc bindir = |home:///bin|){

    <cfg, rvmMod> = compile1Incremental(moduleLoc, reuseConfig, verbose=verbose, bindir=bindir);
   
    return rvmMod;
}    