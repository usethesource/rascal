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

RVMProgram compile(str rascalSource, bool listing=false, bool recompile=false, loc bindir = |home:///bin|){
   muMod  = r2mu(parse(#start[Module], rascalSource).top);
   for(imp <- muMod.imports){
   	    println("Compiling import <imp>");
   	    compile(imp);
   	}
   rvmProgram = mu2rvm(muMod, listing=listing);
   return rvmProgram;
}

bool valid(loc moduleLoc, loc bindir){
    rvmLoc = RVMProgramLocation(moduleLc, bindir);
    return exists(rvmLoc) && lastModified(rvmLoc) > lastModified(moduleLoc);
}

@doc{Synopsis: Get the MuRascal version of a Rascal module}

private MuModule getMuModule(loc moduleLoc, bool recompile=false, loc bindir = |home:///bin|){
   muModuleLoc = MuModuleLocation(moduleLoc, bindir);
   if(!recompile && exists(muModuleLoc) && lastModified(muModuleLoc) > lastModified(moduleLoc)){
       try {
           muMod = readTextValueFile(#MuModule, muModuleLoc);
           if(all(imp <- muMod.imports, valid(imp, bindir))){
              println("compile: Using existing MuModule <muModuleLoc>");
              return muMod;
           } else {
              println("compile: recompiling <muModuleLoc> since imports are no longer valid");
           }
       } catch x: println("compile: Reading <muModuleLoc> did not succeed: <x>");
    }
    muMod = r2mu(parse(#start[Module], moduleLoc).top); // .top is needed to remove start! Ugly!
    println("compile: Writing MuModule <muModuleLoc>");
    writeTextValueFile(muModuleLoc, muMod);
    return muMod;
}

@doc{Compile a Rascal source module (given at a location) to RVM}

RVMProgram compile(loc moduleLoc,  bool listing=false, bool recompile=false, loc bindir = |home:///bin|){
    rvmProgram = compile(moduleLoc, {moduleLoc}, listing=listing, recompile=recompile,bindir=bindir);
    for(msg <- rvmProgram.messages){
        println(msg);
    }
    return rvmProgram;
}

private RVMProgram compile(loc moduleLoc,  set[loc] worklist, bool listing=false, bool recompile=false, loc bindir = |home:///bin|){
    println("compile: <moduleLoc>");
    rvmProgramLoc = RVMProgramLocation(moduleLoc, bindir);
    if(!recompile && exists(rvmProgramLoc) && lastModified(rvmProgramLoc) > lastModified(moduleLoc)){
       try {
  	       rvmProgram = readTextValueFile(#RVMProgram, rvmProgramLoc);
  	       
  	       // Temporary work around related to issue #343
  	       rvmProgram = visit(rvmProgram) { case type[value] t: { insert type(t.symbol,t.definitions); }}
  	       
  	       println("rascal2rvm: Using compiled version <rvmProgramLoc>");
  	       return rvmProgram;
  	   } catch x: println("compile: Reading <rvmProgramLoc> did not succeed: <x>");
  	}
  	
  	RVMProgram rvmProgram;
  	
  	try {
       	muMod = getMuModule(moduleLoc, recompile=recompile, bindir=bindir);
       	messages = muMod.messages;
       	
       	if(any(msg <- messages, error(_,_) := msg)){
       	    //println("compile: Errors in <muMod.name>");
       	    //for(e:error(_,_) <- messages){
       	    //    println(e);
       	    //}
       	    rvmProgram = errorRVMProgram(muMod.name, messages);
       	} else {
       	    imp_messages = {};
       	    imp_with_errors = {};
           	for(imp <- muMod.imports, imp notin worklist){
           	    println("Compiling import <imp>");
           	    worklist += imp;
           	    rvm_imp = compile(imp, worklist);
           	    if(any(msg <-rvm_imp.messages, error(_,_) := msg)){
           	        imp_with_errors += imp;
           	    }
           	    imp_messages += rvm_imp.messages;
           	}
       	
           	if(any(msg <-imp_messages, error(_,_) := msg)){
           	    println("compile: Errors in imports of <muMod.name>: <imp_with_errors>");
           	    for(e:error(_,_) <- imp_messages){
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
    return rvmProgram;
}