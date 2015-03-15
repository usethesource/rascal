module experiments::Compiler::Examples::Tst1

import lang::rascal::tests::types::StaticTestingUtils;	

value main(list[value] args) {
	return
	checkOK("13;",
			importedModules = ["ParseTree"]);
}		