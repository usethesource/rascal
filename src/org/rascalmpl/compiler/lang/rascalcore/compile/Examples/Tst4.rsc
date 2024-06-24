module lang::rascalcore::compile::Examples::Tst4

import util::Maybe;

//set[int] z1 = {y | x <- [1..5], just(y)     := just(x)};
set[int] z2 = {y | x <- [1..5], just(y)     := just(x)} + {6, 7, 8}; // Error: Initialization of `z2` should be subtype of `set[int]`, found `set[value]`
//set[int] z3 = {y | x <- [1..5], just(int y) := just(x)} + {6, 7, 8};

set[int] z4 = {y | x <- [1..5], {y} := {y}};
//set[int] z5 = {y | x <- [1..5], {y} := {y}} + {6, 7, 8};
//set[int] z6 = {y | x <- [1..5], [y] := [y]};
//set[int] z7 = {y | x <- [1..5], [y] := [y]} + {6, 7, 8};

value main() = z4;
//
//alias A = int;
//int A() = 0;

//data F = f(str s, int n) | f(int n, str s);
//int getN(f(s, n)) = s;
        