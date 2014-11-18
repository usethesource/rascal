@bootstrapParser
module experiments::Compiler::Compile

import Prelude;
import Message;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::muRascal2RVM::mu2rvm;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library

loc RVMProgramLocation(loc src, loc bindir) = (bindir + src.path)[extension="rvm"];

loc MuModuleLocation(loc src, loc bindir) = (bindir + src.path)[extension="mu"];

RVMProgram compile(str rascalSource, bool listing=false, bool recompile=true, loc bindir = |home:///bin|){
   muMod  = r2mu(parse(#start[Module], rascalSource).top);
   for(imp <- muMod.imports){
   	    println("Compiling import <imp>");
   	    compile(imp);
   	}
   rvmProgram = mu2rvm(muMod, listing=listing);
   return rvmProgram;
}

bool valid(loc moduleLoc, loc bindir){
    rvmLoc = RVMProgramLocation(moduleLoc, bindir);
    return exists(rvmLoc) ==> lastModified(rvmLoc) > lastModified(moduleLoc);
}

@doc{Synopsis: Get the MuRascal version of a Rascal module}

private MuModule getMuModule(loc moduleLoc, bool recompile=false, loc bindir = |home:///bin|){
   muModuleLoc = MuModuleLocation(moduleLoc, bindir);
   //println("exists(<muModuleLoc>): <exists(muModuleLoc)>");
   //println("lastModified(<muModuleLoc>) \> lastModified(<moduleLoc>): <lastModified(muModuleLoc) > lastModified(moduleLoc)>");
   if(!recompile && exists(muModuleLoc) && lastModified(muModuleLoc) > lastModified(moduleLoc)){
       try {
           muMod = readTextValueFile(#MuModule, muModuleLoc);
           if(all(imp <- muMod.imports, valid(imp, bindir))){
              println("compile: Using existing MuModule <muModuleLoc>");
              return muMod;
           } else {
              println("compile: recompiling <muModuleLoc> since some imports are no longer valid");
           }
       } catch x: println("compile: Reading <muModuleLoc> did not succeed: <x>");
    }
    println("compile: recompiling <moduleLoc>");
    muMod = r2mu(parse(#start[Module], moduleLoc).top); // .top is needed to remove start! Ugly!
    println("compile: Writing MuModule <muModuleLoc>");
    writeTextValueFile(muModuleLoc, muMod);
    return muMod;
}

map[loc, RVMProgram] completed = ();
set[loc] busy = {};

@doc{Compile a Rascal source module (given at a location) to RVM}

RVMProgram compile(loc moduleLoc, bool listing=false, bool recompile=true, loc bindir = |home:///bin|){
    completed = ();
    busy = {};
    rvmProgram = compile1(moduleLoc, listing=listing, recompile=recompile,bindir=bindir);
    for(msg <- rvmProgram.messages){
        println(msg);
    }
    completed = ();
    busy = {};
    return rvmProgram;
}

private RVMProgram compile1(loc moduleLoc, bool listing=false, bool recompile=false, loc bindir = |home:///bin|){
    if(completed[moduleLoc]?){
        println("compile: <moduleLoc>, retrieved from completed");
        return completed[moduleLoc];
    }
    println("compile: <moduleLoc>, busy: <busy>");
    busy += moduleLoc;
    rvmProgramLoc = RVMProgramLocation(moduleLoc, bindir);
    if(!recompile && exists(rvmProgramLoc) && lastModified(rvmProgramLoc) > lastModified(moduleLoc)){
       try {
  	       rvmProgram = readTextValueFile(#RVMProgram, rvmProgramLoc);
  	       
  	       // Temporary work around related to issue #343
  	       rvmProgram = visit(rvmProgram) { case type[value] t: { insert type(t.symbol,t.definitions); }}
  	       
  	       println("rascal2rvm: Using compiled version <rvmProgramLoc>");
  	       completed[moduleLoc] = rvmProgram;
  	       busy -= moduleLoc;
  	       return rvmProgram;
  	   } catch x: println("compile: Reading <rvmProgramLoc> did not succeed: <x>");
  	}
  	
  	RVMProgram rvmProgram;
  	
  	try {
       	muMod = getMuModule(moduleLoc, recompile=recompile, bindir=bindir);
       	messages = muMod.messages;
       	
       	if(any(Message msg <- messages, error(_,_) := msg)){
       	    println("compile: Errors in <muMod.name>");
       	    for(e:error(_,_) <- messages){
       	        println(e);
       	    }
       	    rvmProgram = errorRVMProgram(muMod.name, messages);
       	} else {
       	    imp_messages = {};
       	    imp_with_errors = {};
           	for(imp <- muMod.imports, imp notin busy){
           	    println("Compiling import <imp>");
           	    rvm_imp = compile1(imp);
           	    if(any(msg <-rvm_imp.messages, error(_,_) := msg)){
           	        imp_with_errors += imp;
           	    }
           	    imp_messages += rvm_imp.messages;
           	}
       	
           	if(any(Message msg <-imp_messages, error(_,_) := msg)){
           	    println("compile: Errors in imports of <muMod.name>: <imp_with_errors>");
           	    for(Message e:error(_,_) <- imp_messages){
                    println(e);
                }
           	    rvmProgram = errorRVMProgram(muMod.name, messages + imp_messages);
           	} else {
           	    println("compile: Generating rvm for <moduleLoc>");
           	    rvmProgram = mu2rvm(muMod, listing=listing); 
           	}                         
        }
   	} catch x : {
   	    rvmProgram = errorRVMProgram(basename(moduleLoc), {error("Fatal compilation error: <x>", moduleLoc)});
   	}
   	
   	println("compile: Writing compiled version <rvmProgramLoc>");
    writeTextValueFile(rvmProgramLoc, rvmProgram);
    completed[moduleLoc] = rvmProgram;
    busy -= moduleLoc;
    return rvmProgram;
}