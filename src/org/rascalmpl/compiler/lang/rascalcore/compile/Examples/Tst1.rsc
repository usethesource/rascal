module lang::rascalcore::compile::Examples::Tst1

//lexical Id  = [a-z];
//
//syntax Exp = exp: Exp lhs "+" Exp rhs;
//
//value main() = ([Exp] "a+b").lhs;

//import lang::rascal::\syntax::Rascal;
//
//value main() = [Expression] "{x=1;}";

//test bool cntStats1() = cntStats(((Expression) `{x=1;}`).statements) == 1;
//test bool cntStats2() = cntStats(((Expression) `{x=1;x=2;}`).statements) == 2;
 

lexical Id  = [a-z];

value main() = (Id) `<Id _>` := (Id) `x`;