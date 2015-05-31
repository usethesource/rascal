module experiments::Compiler::Examples::Tst5

import lang::rascal::tests::types::StaticTestingUtils;

import ParseTree;


value main(list[value] args) = 
	checkOK("Program p;", //"(Program) `begin declare x: natural; x := 10 end`;", 
			importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);