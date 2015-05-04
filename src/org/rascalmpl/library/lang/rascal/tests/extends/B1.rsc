module lang::rascal::tests::extends::B1

import lang::rascal::tests::extends::B2;
import lang::rascal::tests::extends::B3;

import lang::rascal::tests::extends::Base;

int add1(int n) = n + 1;

test bool base1() { S s = [S] "a";  return s == [S] "a";}

test bool base2() { Sstar s = [Sstar] "aaa";  return s == [Sstar] "aaa";}

test bool base3() { INTEGER n = 13; return n == 13;}

test bool base4() { D x = d1(); return x == d1();}

test bool base5() { return ident(13) == 13; }

test bool base6() { return f(0) == "zero"; }

test bool base7() { return f(9) == "value"; }

// Defined in B[1-3]

test bool tadd1() = add1(5) == 6;
test bool tadd2() = add2(5) == 7;
test bool tadd3() = add3(5) == 8;