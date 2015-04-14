module experiments::Compiler::Examples::Tst2


//import lang::rascal::tests::types::StaticTestingUtils;
//
//// Sanity check on the testing utilities themselves
//
//value main(list[value] args) = checkOK("13;");


//syntax E = "a";
//syntax E = E "+" E;

import lang::rascal::\syntax::Rascal;
import ParseTree;

public bool isAmb(Tree t) = /amb(_) := t;
//
//public test bool literalAmb() = parse(#Exp,"\"a\"+ b;"));


value main(list[value] args) = isAmb(parse(#Expression,"a+a+a"));
//
////isAmb(parse(#E, "a+a+a"));
//
//
////import lang::rascal::\syntax::Rascal;
////import ParseTree;
////
////public bool isAmb(Tree t) = /amb(_) := t;
////
////public test bool literalAmb() = isAmb(parse(#Command,"\"a\"+ b;"));
