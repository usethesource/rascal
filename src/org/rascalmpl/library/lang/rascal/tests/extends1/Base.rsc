module lang::rascal::tests::extends::Base

syntax S = "a";

syntax Sstar = S*;

alias INTEGER = int;

data D = d1();

int ident(int n) = n;

str f(0) = "zero";

default str f(value v) = "value";