module experiments::Compiler::Examples::Tst6

import List;

syntax A = a: "a";

syntax X = "plus" {A ","}+ l1 | "star" {A ","}* l2;

layout W = [\ ]*;

int f({A ","}* l) = size([y | A y <- l]);

test bool plusToStar() = f(([X] "plus a,a").l1) == 2;

data IG = ig(int z = 1);

test bool ignoreKeywordParameter2() = ig(z=1) := ig();