module experiments::Compiler::Examples::Tst1

import lang::rascal::tests::types::StaticTestingUtils;
import lang::rascal::\syntax::Rascal;
import ParseTree;

syntax A = "a";

value main(list[value] args) = checkOK("x;", initialDecls=["int x = 5;"]);

//#start[A];

//parse(#start[Module],"module M\nimport List;");


//checkOK("x;", initialDecls=["int x = 5;"]);
