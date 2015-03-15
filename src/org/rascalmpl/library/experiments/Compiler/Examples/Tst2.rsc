
module experiments::Compiler::Examples::Tst2


import lang::rascal::tests::types::StaticTestingUtils;

import ParseTree;

value main(list[value] args) = 
	cannotMatch("[[1]] := [];");


//test bool matchNestedList() = cannotMatch("[[1]] := [];");   // NoSuchAnnotation("at")

 
//test bool antiPatternDoesNotDeclare1() = undeclaredVariable("![1,int X,3] := [1,2,4] && (X ? 10) == 10;" ); // TODO //NoSuchAnnotation("rtype")

//test bool doubleTypedVariableBecomes() = redeclaredVariable("[int N : 3, int N : 4] := [3,4] && N == 3;");  // Loops

//test bool PicoQuoted1() = 																					// Loops
//	checkOK("Program program := t1;",
//			initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
//			importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);	
  	
  	
  		