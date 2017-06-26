module experiments::Compiler::CompileMuLibrary

import IO;
import ValueIO;
import util::Reflective;
import String;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Load;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::muRascal2RVM::Relocate;

import experiments::Compiler::muRascal2RVM::mu2rvm;

private loc MuLibraryLoc(PathConfig pcfg) = getSearchPathLoc("experiments/Compiler/muRascal2RVM/MuLibrary.mu", pcfg);

private str MuLibrary() = "experiments::Compiler::muRascal2RVM::MuLibrary";

loc getMuLibraryCompiledWriteLoc(PathConfig pcfg) = getDerivedWriteLoc(MuLibrary(), "rvm", pcfg);

list[RVMDeclaration] compileMuLibrary(PathConfig pcfg, bool verbose = false, bool jvm=true, loc reloc=|std:///| /* loc reloc=|noreloc:///|*/ ){ // switch to |noreloc:///| as default after next boot release
    str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library
    println("compileMuLibrary: <MuLibraryLoc(pcfg)>.mu, <reloc>");
    if(verbose) println("execute: Recompiling library <basename(MuLibraryLoc(pcfg))>.mu");
    MuLibraryCompiled = getMuLibraryCompiledWriteLoc(pcfg);
    libModule = load(MuLibraryLoc(pcfg));
 
    libModule = relocMuModule(libModule, reloc, pcfg.srcs);
    functions =  mulib2rvm(libModule);
    writeBinaryValueFile(MuLibraryCompiled, functions);
    if(verbose) println("execute: Writing compiled version of library <MuLibraryCompiled>");
    
    return functions; 
}

void compileMuLibrary(list[loc] srcs, list[loc] libs, loc boot, loc bin, bool verbose = false, bool jvm=true, loc reloc=|noreloc:///|) {
  compileMuLibrary(pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), verbose=verbose, jvm=jvm, reloc=reloc);
}