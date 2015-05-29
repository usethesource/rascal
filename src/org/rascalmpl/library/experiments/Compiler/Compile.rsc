@bootstrapParser
module experiments::Compiler::Compile
 
import IO;
import ValueIO;
import String;
import Message;
import ParseTree;
import util::Reflective;
import Map;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::muRascal2RVM::mu2rvm;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;

//import util::ValueUI;

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library

loc RVMProgramLocation(loc src, loc bindir) = getDerivedLocation(src, "rvm.gz", bindir = bindir, compressed=true); //(bindir + src.path)[extension="rvm"];

loc RVMExecutableLocation(loc src, loc bindir) = getDerivedLocation(src, "rvm.ser.gz", bindir = bindir, compressed=true); //(bindir + src.path)[extension="rvm.ser.xz"];
loc RVMExecutableCompressedLocation(loc src, loc bindir) = getDerivedLocation(src, "rvm.ser.gz", bindir = bindir, compressed=true); //(bindir + src.path)[extension="rvm.ser.xz"];


loc MuModuleLocation(loc src, loc bindir) = getDerivedLocation(src, "mu", bindir = bindir); //(bindir + src.path)[extension="mu"];

loc ConfigLocation(loc src, loc bindir) = getDerivedLocation(src, "tc", bindir = bindir);

bool valid(loc moduleLoc, loc bindir){
    configLoc = ConfigLocation(moduleLoc, bindir);
    return exists(configLoc) ==> lastModified(configLoc) > lastModified(moduleLoc);
}

bool validDerived(loc orgLoc, loc derivedLoc, loc bindir){
   //println("exists(<derivedLoc>): <exists(derivedLoc)>");
   
   //println("lastModified(<orgLoc>) = <lastModified(orgLoc)>");
   // println("lastModified(<derivedLoc>) = <lastModified(derivedLoc)>");
   //println("lastModified(<derivedLoc>) \>= lastModified(<orgLoc>): <lastModified(derivedLoc) >= lastModified(orgLoc)>");
    return exists(derivedLoc) ==> lastModified(derivedLoc) >= lastModified(orgLoc);
}

bool needsRecompilation(loc src, loc bindir = |home:///bin|){
	configLoc = ConfigLocation(src, bindir);
	rvmLoc = RVMProgramLocation(src, bindir);
	//println("exists(<rvmLoc>): <exists(rvmLoc)>");
	//println("validDerived(<src>, <configLoc>, bindir): <validDerived(src, configLoc, bindir)>");
	//println("validDerived(<configLoc>, <rvmLoc>, bindir): <validDerived(configLoc, rvmLoc, bindir)>");
	
	return !(validDerived(src, configLoc, bindir) && validDerived(configLoc, rvmLoc, bindir) && validDerived(src, rvmLoc, bindir));
}

RVMProgram getRVMProgram(loc moduleLoc, bool recompile=false, loc bindir = |home:///bin|){
	rvmProgramLoc = RVMProgramLocation(moduleLoc, bindir);
	
    if(!recompile && !needsRecompilation(moduleLoc)){
        try {
	  	       rvmProgram = readBinaryValueFile(#RVMProgram, rvmProgramLoc);
	  	       
	  	       // Temporary work around related to issue #343
	  	       //rvmProgram = visit(rvmProgram) { case type[value] t: { insert type(t.symbol,t.definitions); }}
	  	       
	  	       println("rascal2rvm: Using compiled version <rvmProgramLoc>");
	  	       rvmModules[moduleLoc] = rvmProgram;
	  	       return rvmProgram;
	  	} 
	  	catch x: println("compile: Reading <rvmProgramLoc> did not succeed: <x>");
  	}
    
    Configuration config;
    lang::rascal::\syntax::Rascal::Module M;
   	try {
   	    println("rascal2rvm: Parsing and checking <moduleLoc>");
   		M = parse(#start[Module], moduleLoc).top;
   	    config  = checkModule(M, newConfiguration(), bindir=bindir);
   	} catch e: {
   	    throw e;
   	}
   	// Uncomment to dump the type checker configuration:
   	// text(config);
   	errors = [ e | e:error(_,_) <- config.messages];
   	warnings = [ w | w:warning(_,_) <- config.messages ];
   
   	if(size(errors) > 0) {
   		rvmProgram = errorRVMProgram("<M.header.name>", config.messages, moduleLoc);
   	    rvmModules[moduleLoc] = rvmProgram;
   	    return rvmProgram;
   	} else {
   	
   		if(!recompile && !needsRecompilation(moduleLoc) && exists(rvmProgramLoc)){
   			try {
    			rvmProgram = readBinaryValueFile(#RVMProgram, rvmProgramLoc);
	  	       
	  	       // Temporary work around related to issue #343
	  	       //rvmProgram = visit(rvmProgram) { case type[value] t: { insert type(t.symbol,t.definitions); }}
	  	       
	  	       println("rascal2rvm: Using compiled version <rvmProgramLoc>");
	  	       rvmModules[moduleLoc] = rvmProgram;
	  	       return rvmProgram;
	  		} 
	  		catch x: println("rascal2rvm: Reading <rvmProgramLoc> failed: <x>");		
   		}
   	    println("rascal2rvm: Recompiling <moduleLoc>");
	   	muMod = r2mu(M, config);
	   	//muModuleLoc = MuModuleLocation(moduleLoc, bindir);
	   	//println("compile: Writing MuModule <muModuleLoc>");
     //   writeTextValueFile(muModuleLoc, muMod);
	   	
	    rvmProgram = mu2rvm(muMod); 
	    println("compile: Writing RVMProgram <rvmProgramLoc>");
	    writeBinaryValueFile(rvmProgramLoc, rvmProgram);
	    for(msg <- rvmProgram.messages){
	        println(msg);
	    }
	    rvmModules[moduleLoc] = rvmProgram;
	    recompiled += moduleLoc;
	    return rvmProgram;
    }
}	

map[loc, RVMProgram] rvmModules = ();
rel[loc, loc] imports = {};

set[loc] recompiled = {};

void collectDependencies(loc moduleLoc, bool recompile=false, loc bindir = |home:///bin|){
	rvmModules = ();
	imports = {};
	recompiled = {};
	collectDependencies1(moduleLoc, recompile=recompile, bindir=bindir);
}

void collectDependencies1(loc moduleLoc, bool recompile=false,loc bindir = |home:///bin|){
	prog = getRVMProgram(moduleLoc, recompile=recompile, bindir=bindir);
	for(imp <- prog.imports){
		if(!rvmModules[imp]?){
		  collectDependencies1(imp, bindir=bindir);
		}
		imports += <moduleLoc, imp>;
	}
}

@doc{Compile a Rascal source module (given at a location) to RVM}

RVMProgram compile(loc moduleLoc, bool listing=false, bool recompile=false, loc bindir = |home:///bin|){
	collectDependencies(moduleLoc, recompile=recompile, bindir=bindir);
	imports1 = imports*;
	for(mloc <- rvmModules){
		if(imports1[mloc] & recompiled != {} && mloc notin recompiled){
			getRVMProgram(mloc, recompile=true, bindir=bindir);
		}
	}
	messages = {};
	for(mloc <- rvmModules){
		messages += rvmModules[mloc].messages;
	}
	
	if(any(msg <- messages, error(_,_) := msg)){
		rvmProgram = errorRVMProgram(rvmModules[moduleLoc].name, messages, moduleLoc);
   	    rvmModules[moduleLoc] = rvmProgram;
   	    return rvmProgram;
	}
	
    return rvmModules[moduleLoc];
} 