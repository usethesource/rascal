module experiments::Compiler::CompileMuLibrary

import IO;
import ValueIO;
import util::Reflective;
import String;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Load;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::muRascal2RVM::mu2rvm;
import experiments::Compiler::muRascal2RVM::StackValidator; // TODO: hide these two
import experiments::Compiler::muRascal2RVM::PeepHole;

private loc MuLibraryLoc(PathConfig pcfg) = getSearchPathLoc("experiments/Compiler/muRascal2RVM/MuLibrary.mu", pcfg);

private str MuLibrary() = "experiments::Compiler::muRascal2RVM::MuLibrary";

loc getMuLibraryCompiledWriteLoc(PathConfig pcfg) = getDerivedWriteLoc(MuLibrary(), "rvm.gz", pcfg);

list[RVMDeclaration] compileMuLibrary(PathConfig pcfg, bool verbose = false, bool jvm=false){
    str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library
    if(verbose) println("execute: Recompiling library <basename(MuLibraryLoc(pcfg))>.mu");
    MuLibraryCompiled = getMuLibraryCompiledWriteLoc(pcfg);
    libModule = load(MuLibraryLoc(pcfg));
    list[RVMDeclaration] functions = [];
 
    for(fun <- libModule.functions) {
        setFunctionScope(fun.qname);
        set_nlocals(fun.nlocals);
        body = peephole(tr(fun.body, stack(), returnDest()));
        <maxSP, exceptions> = validate(fun.src, body, []);
        required_frame_size = get_nlocals() + maxSP;
        functions += (fun is muCoroutine) ? COROUTINE(fun.qname, fun. uqname, fun.scopeIn, fun.nformals, get_nlocals(), (), fun.refs, fun.src, required_frame_size, body, [])
                                          : FUNCTION(fun.qname, fun.uqname, fun.ftype, fun.scopeIn, fun.nformals, get_nlocals(), (), false, false, false, fun.src, required_frame_size, 
                                                     false, 0, 0, body, []);
    }
  
    writeBinaryValueFile(MuLibraryCompiled, functions);
    if(verbose) println("execute: Writing compiled version of library <MuLibraryCompiled>");
    
    return functions; 
}