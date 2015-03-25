@bootstrapParser
module experiments::Compiler::Examples::Tst4

import lang::rascal::tests::types::StaticTestingUtils;

value main(list[value] args) =
checkOK("Program program := t1;",
			initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
			importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);
			