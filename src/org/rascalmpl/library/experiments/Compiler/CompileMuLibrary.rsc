module experiments::Compiler::CompileMuLibrary

import IO;
import ValueIO;
import util::Reflective;
import String;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Load;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::muRascal2RVM::mu2rvm;

private loc MuLibraryLoc(PathConfig pcfg) = getSearchPathLoc("experiments/Compiler/muRascal2RVM/MuLibrary.mu", pcfg);

private str MuLibrary() = "experiments::Compiler::muRascal2RVM::MuLibrary";

loc getMuLibraryCompiledWriteLoc(PathConfig pcfg) = getDerivedWriteLoc(MuLibrary(), "rvm.gz", pcfg);

list[RVMDeclaration] compileMuLibrary(PathConfig pcfg, bool verbose = false, bool jvm=false){
    str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library
    if(verbose) println("execute: Recompiling library <basename(MuLibraryLoc(pcfg))>.mu");
    MuLibraryCompiled = getMuLibraryCompiledWriteLoc(pcfg);
    libModule = load(MuLibraryLoc(pcfg));
 
    functions =  mulib2rvm(libModule);
    writeBinaryValueFile(MuLibraryCompiled, functions);
    if(verbose) println("execute: Writing compiled version of library <MuLibraryCompiled>");
    
    return functions; 
}