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
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::muRascal2RVM::mu2rvm;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library

loc RVMProgramLocation(loc src, loc bindir) = getDerivedLocation(src, "rvm.gz", bindir = bindir, compressed=true); //(bindir + src.path)[extension="rvm"];

loc RVMExecutableLocation(loc src, loc bindir) = getDerivedLocation(src, "rvm.ser.gz", bindir = bindir, compressed=true); //(bindir + src.path)[extension="rvm.ser.xz"];
loc RVMExecutableCompressedLocation(loc src, loc bindir) = getDerivedLocation(src, "rvm.ser.gz", bindir = bindir, compressed=true); //(bindir + src.path)[extension="rvm.ser.xz"];


loc MuModuleLocation(loc src, loc bindir) = getDerivedLocation(src, "mu", bindir = bindir); //(bindir + src.path)[extension="mu"];

loc ConfigLocation(loc src, loc bindir) = getDerivedLocation(src, "tc", bindir = bindir);


bool validRVM(loc src, loc bindir = |home:///bin|){
	rvmLoc = RVMProgramLocation(src, bindir);
	//println("exists(<rvmLoc>): <exists(rvmLoc)>");
	//println("lastModified(<rvmLoc>) \>= lastModified(<src>): <lastModified(rvmLoc) >= lastModified(src)>");
	res = exists(rvmLoc) && lastModified(rvmLoc) >= lastModified(src);
	//println("validRVM(<src>) =\> <res>");
	return res;
}

tuple[Configuration, RVMProgram] compile1(loc moduleLoc, bool verbose = true, loc bindir = |home:///bin|){

	Configuration config;
    lang::rascal::\syntax::Rascal::Module M;
   	try {
   	    if(verbose) println("rascal2rvm: Parsing and checking <moduleLoc>");
   	    start_checking = cpuTime();
   		M = parse(#start[Module], moduleLoc).top;
   	    config  = checkModule(M, newConfiguration(), bindir=bindir);
   	    println("Checking <moduleLoc>: <(cpuTime() - start_checking)/1000000> ms");
   	} catch e: {
   	    throw e;
   	}
   	errors = [ e | e:error(_,_) <- config.messages];
   	warnings = [ w | w:warning(_,_) <- config.messages ];
   
   	if(size(errors) > 0) {
   		rvmProgram = errorRVMProgram("<M.header.name>", config.messages, moduleLoc);
   	    return <config, rvmProgram>;
   	}
   	
   	rvmProgramLoc = RVMProgramLocation(moduleLoc, bindir);
   	
    if(verbose) println("rascal2rvm: Compiling <moduleLoc>");
   	muMod = r2mu(M, config, verbose=verbose);
   	
    rvmProgram = mu2rvm(muMod, verbose=verbose); 
    if(verbose) println("compile: Writing RVMProgram <rvmProgramLoc>");
    writeBinaryValueFile(rvmProgramLoc, rvmProgram);
    for(msg <- rvmProgram.messages){
        println(msg);
    }
    return <config, rvmProgram>;  
}	

@doc{Compile a Rascal source module (given at a location) to RVM}

RVMProgram compile(loc moduleLoc, bool verbose = true, loc bindir = |home:///bin|){

   // moduleLoc = getSearchPathLocation(moduleLoc.path);
    if(verbose) println("moduleLoc = <moduleLoc>");
    //moduleLoc = normalize(moduleLoc);
	<cfg, rvmProgram> = compile1(moduleLoc, verbose=verbose, bindir=bindir);
   
   	errors = [ e | e:error(_,_) <- cfg.messages];
   	warnings = [ w | w:warning(_,_) <- cfg.messages ];
   
   	if(size(errors) > 0) {
   	    return rvmProgram;
   	}
   	
   	dirtyModulesLoc = { getModuleLocation(prettyPrintName(dirty)) | dirty <- cfg.dirtyModules };
   	for(dirtyLoc <- dirtyModulesLoc){
   		println("\tdirty: <dirtyLoc>");
   	}
   	if(verbose){
   	   for(<m1, m2> <- cfg.importGraph){
   		   println("<prettyPrintName(m1)> imports <prettyPrintName(m2)>");
   	   }
   	}
   	
   	importedByStar = invert(cfg.importGraph)*;
   	
   	//affectedByDirty = importedByStar[cfg.dirtyModules];
    //println("affectedByDirty: <affectedByDirty>"); 
   	//
   	//println("Affected but not in dirty: <affectedByDirty - cfg.dirtyModules>");
   	
   	allDependencies = { getModuleLocation(prettyPrintName(rname)) | rname <- carrier(cfg.importGraph) } - moduleLoc;
   	
   	//println("allDependencies: <allDependencies>");
   	
    for(dependency <- allDependencies){
        if(dependency in dirtyModulesLoc || !validRVM(dependency)){
    	   compile1(dependency, bindir=bindir);
        }
    }
   
   	clearDirtyModules(moduleLoc, bindir);
   	return rvmProgram ;
}	