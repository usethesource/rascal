module lang::rascal::tests::extends1::BaseExtended

extend lang::rascal::tests::extends1::Base;

syntax S = "z";

alias STRING = str;

alias LIST_INTEGER = list[INTEGER];

data D = d2();

data E = e();

str ident(str s) = s;

str f(1) = "one";