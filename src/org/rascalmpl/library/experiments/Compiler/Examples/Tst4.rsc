@bootstrapParser
module experiments::Compiler::Examples::Tst4

import lang::rascal::tests::types::StaticTestingUtils;
import util::Benchmark;
import IO;

value main(list[value] args) {
	t1 = cpuTime();
	res = checkOK("size([1,2,3]);",
			importedModules = ["List"]);
	//res = checkOK("Program program := t1;",
	//		initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
	//		importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);
	t2 = cpuTime();		
	println("Time for checking: <(t2 - t1)/1000000>");
	return res;
}			
			