@bootstrapParser
module experiments::Compiler::Tests::TestUtils

//import experiments::Compiler::RVM::AST;   // TODO: necessary to keep the typechecker happy (can't find RVMProgram in one of the overlaoded defs of "execute")
import  experiments::Compiler::Compile;
import  experiments::Compiler::Execute;
import util::SystemAPI;
import IO;
import util::Reflective;

//loc TMP = |tmp:///experiments/Compiler::Tests::TMP.rsc|;

PathConfig pcfg = pathConfig(srcPath=[|test-modules:///|, |std:///|], binDir=|home:///bin|, libPath=[|home:///bin|]);

value run(str exp, bool debug=false, bool debugRVM=false, bool recompile=true, bool profile=false, bool jvm=true) {
    TMP = makeTMP();
    msrc = "module TMP data D = d1(int n, str s) | d2(str s, bool b) | d3(list[int] l, list[int] r); value main() = <exp>;";
	writeFile(TMP, msrc);
	compileAndLink("TMP", pcfg, jvm=jvm); 
	return execute(TMP, pcfg, debug=debug, debugRVM=debugRVM, recompile=false, profile=profile, jvm=jvm);
}	
value run(str before, str exp, bool debug=false, bool debugRVM=false,  bool recompile=true, bool profile=false, bool jvm=true) {
   TMP = makeTMP();
   msrc = "module TMP data D = d1(int n, str s) | d2(str s, bool b); value main() {<before> ; return <exp>;}";
  
   writeFile(TMP, msrc);
   compileAndLink("TMP", pcfg, jvm=jvm); 
   return execute(TMP, pcfg, debug=debug, debugRVM=debugRVM, recompile=recompile, profile=profile, jvm=jvm);
}

value run(str exp, list[str] imports, bool debug=false, bool debugRVM=false, bool recompile=true, bool profile=false,  bool jvm=true) {
   TMP = makeTMP();
    msrc = "module TMP <for(im <- imports){>import <im>; <}> data D = d1(int n, str s) | d2(str s, bool b); value main() = 
           '<exp>;";
    
    writeFile(TMP, msrc);
    compileAndLink("TMP", pcfg, jvm=jvm); 
	return execute(TMP, pcfg, debug=debug, debugRVM=debugRVM, recompile=recompile, profile=profile, jvm=jvm);
}
	
data D = d1(int n, str s) | d2(str s, bool b) | d3(list[int] l, list[int] r);

loc makeTMP(){
	mloc = |test-modules:///TMP.rsc|;
    return mloc;
}