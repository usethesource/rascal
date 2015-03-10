module experiments::Compiler::Examples::Tst1

import lang::rascal::tests::types::StaticTestingUtils;

value main(list[value] args) {
	//makeModule("MMM", "int x = 3;"); 
	//b1 = undeclaredVariable("x;", importedModules=["MMM"]);
	//makeModule("MMM", "data DATA = d();"); 
	//b2 = checkOK("DATA x;", importedModules=["MMM"]);
	b3 = checkOK("size([1,2,3]);", importedModules=["List"]);
	return b3;
}
