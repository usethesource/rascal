@bootstrapParser
module experiments::Compiler::Compile

import Prelude;
import lang::rascal::\syntax::Rascal;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::muRascal2RVM::mu2rvm;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library

RVMProgram compile(str rascalSource, bool listing=false, bool recompile=false){
   muMod  = r2mu(parse(#start[Module], rascalSource).top);
   for(imp <- muMod.imports){
   	    println("Compiling import <imp>");
   	    compile(imp);
   	}
   rvmProgram = mu2rvm(muMod, listing=listing);
   return rvmProgram;
}

@doc{Compile a Rascal source module (given at a location) to RVM}
RVMProgram compile(loc moduleLoc,  bool listing=false, bool recompile=false){
    println("compile: <moduleLoc>");
    rvmProgramLoc = moduleLoc.parent + (basename(moduleLoc) + ".rvm");
    if(!recompile && exists(rvmProgramLoc) && lastModified(rvmProgramLoc) > lastModified(moduleLoc)){
       try {
  	       rvmProgram = readTextValueFile(#RVMProgram, rvmProgramLoc);
  	       
  	       // Temporary work around related to issue #343
  	       rvmProgram = visit(rvmProgram) { case type[value] t => type(t.symbol,t.definitions) }
  	       
  	       println("rascal2rvm: Using compiled version <rvmProgramLoc>");
  	       return rvmProgram;
  	   } catch x: println("rascal2rvm: Reading <rvmProgramLoc> did not succeed: <x>");
  	}
    println("compile: parsing and compiling <moduleLoc>");
   	muMod = r2mu(parse(#start[Module], moduleLoc).top); // .top is needed to remove start! Ugly!
   	for(imp <- muMod.imports){
   	    println("Compiling import <imp>");
   	    compile(imp);
   	}
   	println("compile: generate rvm <moduleLoc>");
   	rvmProgram = mu2rvm(muMod, listing=listing);                          

   	println("rascal2rvm: Writing compiled version <rvmProgramLoc>");
   	writeTextValueFile(rvmProgramLoc, rvmProgram);
   	
   	return rvmProgram;
}