@bootstrapParser
module lang::rascalcore::check::tests::PatternUsingStdLibTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool PicoQuoted1() = 
	checkOK("Program program := t1;",
			initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
			importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);

test bool PicoQuoted2() = 
	checkOK("(Program) `\<Program program\>` := t1;",
			initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
			importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);
  
test bool PicoQuoted3() = 
	checkOK(" (Program) `begin \<Declarations decls\> \<{Statement \";\"}* stats\> end` := t1; ",
			initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
			importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);
			
test bool PicoQuoted4() = 
    checkOK("if ((Program) `begin \<Declarations decls\> \<{Statement \";\"}* stats\> end` := t1) {
            '    x = (Program) `begin \<Declarations decls\> \<{Statement \";\"}* stats\> end`;
            '}",
            initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
            importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);