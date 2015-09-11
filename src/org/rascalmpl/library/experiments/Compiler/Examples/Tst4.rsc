
module experiments::Compiler::Examples::Tst4

import ParseTree;
syntax A = "a";
syntax B = "b";

syntax M[&T] = "[[" &T "]]";

syntax MA = M[A];
syntax MB = M[B];

syntax N[&T,&U] = "\<\<" &T "," &U "\>\>";

syntax NAB = N[A,B];

test bool parameterized1() = /A a := (M[A]) `[[a]]`;
test bool parameterized2() = /A a !:= (M[B]) `[[b]]`;

test bool parameterized3() = /B b := (M[B]) `[[b]]`;
test bool parameterized4() = /B b !:= (M[A]) `[[a]]`;

test bool parameterized5() = /A a := (N[A,B]) `\<\<a,b\>\>`;
test bool parameterized6() = /B b := (N[A,B]) `\<\<a,b\>\>`;