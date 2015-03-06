module experiments::Compiler::Examples::Tst3

import lang::rascal::tests::types::StaticTestingUtils;

value main(list[value] args) {
	makeModule("M", "public int x = 3;"); 
	return checkOK("x;", importedModules=["M"]);
}	