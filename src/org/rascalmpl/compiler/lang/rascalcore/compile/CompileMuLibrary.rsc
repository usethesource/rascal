module lang::rascalcore::compile::CompileMuLibrary

import IO;
import ValueIO;
import util::Reflective;
import String;
import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal::Load;
import lang::rascalcore::compile::RVM::AST;

import lang::rascalcore::compile::muRascal2RVM::Relocate;

import lang::rascalcore::compile::muRascal2RVM::mu2rvm;

public loc MuLibraryLoc(PathConfig pcfg) = getSearchPathLoc("experiments/Compiler/muRascal2RVM/MuLibrary.mu", pcfg);

public str MuLibrary() = "lang::rascalcore::compile::muRascal2RVM::MuLibrary";

loc getMuLibraryCompiledWriteLoc(PathConfig pcfg) = getDerivedWriteLoc(MuLibrary(), "rvm", pcfg);

list[RVMDeclaration] compileMuLibrary(PathConfig pcfg, bool verbose = false, bool jvm=true, loc reloc=|std:///| /* loc reloc=|noreloc:///|*/ ){ // switch to |noreloc:///| as default after next boot release
    str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library
    println("compileMuLibrary: <MuLibraryLoc(pcfg)>.mu, <reloc>");
    if(verbose) println("execute: Recompiling library <basename(MuLibraryLoc(pcfg))>.mu");
    
    libModule = getMuLibrary(pcfg, verbose=verbose,jvm=jvm,reloc=reloc);
    functions =  mulib2rvm(libModule);
    MuLibraryCompiled = getMuLibraryCompiledWriteLoc(pcfg);
    writeBinaryValueFile(MuLibraryCompiled, functions);
    if(verbose) println("execute: Writing compiled version of library <MuLibraryCompiled>");
    
    return functions; 
}

MuModule getMuLibrary(PathConfig pcfg, bool verbose = false, bool jvm=true, loc reloc=|std:///|){
    MuLibraryCompiled = getMuLibraryCompiledWriteLoc(pcfg);
    libModule = load(MuLibraryLoc(pcfg));
    return libModule = relocMuModule(libModule, reloc, pcfg.srcs);
}

void compileMuLibrary(list[loc] srcs, list[loc] libs, loc boot, loc bin, bool verbose = false, bool jvm=true, loc reloc=|noreloc:///|) {
  compileMuLibrary(pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), verbose=verbose, jvm=jvm, reloc=reloc);
}