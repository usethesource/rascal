module experiments::Compiler::Tests::TestUtils

import experiments::Compiler::RVM::AST;   // TODO: necessary to keep the typechecker happy (can't find RVMProgram in one of the overlaoded defs of "execute")
import  experiments::Compiler::Execute;
import util::SystemAPI;
import IO;

//loc TMP = |tmp:///experiments/Compiler::Tests::TMP.rsc|;

value run(str exp, bool listing=false, bool debug=false, bool recompile=true, bool profile=false) {
    msrc = "module experiments::Compiler::Tests::TMP data D = d1(int n, str s) | d2(str s, bool b) | d3(list[int] l, list[int] r); value main(list[value] args) = <exp>;";
    TMP = makeTMP();
	writeFile(TMP, msrc);
	return execute(TMP, [], listing=listing, debug=debug, recompile=recompile, profile=profile);
}	
value run(str before, str exp, bool listing=false, bool debug=false, bool recompile=true, bool profile=false) {
   msrc = "module experiments::Compiler::Tests::TMP data D = d1(int n, str s) | d2(str s, bool b); value main(list[value] args) {<before> ; return <exp>;}";
   TMP = makeTMP();
   writeFile(TMP, msrc);
   return execute(msrc, [], listing=listing, debug=debug, recompile=recompile, profile=profile);
}

value run(str exp, list[str] imports, bool listing=false, bool debug=false, bool recompile=true, bool profile=false) {
    msrc = "module experiments::Compiler::Tests::TMP <for(im <- imports){>import <im>; <}> data D = d1(int n, str s) | d2(str s, bool b); value main(list[value] args) = <exp>;";
    TMP = makeTMP();
    writeFile(TMP, msrc);
	return execute(msrc, [], listing=listing, debug=debug, recompile=recompile, profile=profile);
}
	
data D = d1(int n, str s) | d2(str s, bool b) | d3(list[int] l, list[int] r);

loc makeTMP(){
	tmpdir = getSystemProperty("java.io.tmpdir");
	test_modules = |file:///| + tmpdir + "/test-modules";
	if(!exists(test_modules)){
		mkDirectory(test_modules);
	}
   
	mloc = |test-modules:///TMP.rsc|;
   return mloc;
}