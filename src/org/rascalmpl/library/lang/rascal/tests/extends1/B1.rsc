module lang::rascal::tests::extends1::B1

import lang::rascal::tests::extends1::B2;
import lang::rascal::tests::extends1::B3;

import lang::rascal::tests::extends1::Base;

int add1(int n) = n + 1;

test bool b1base1() { S s = [S] "a";  return s == [S] "a";}

test bool b1base2() { Sstar s = [Sstar] "aaa";  return s == [Sstar] "aaa";}

test bool b1base3() { INTEGER n = 13; return n == 13;}

test bool b1base4() { D x = d1(); return x == d1();}

test bool b1base5() { return ident(13) == 13; }

test bool b1base6() { return f(0) == "zero"; }

test bool b1base7() { return f(9) == "value"; }

// Defined in B[1-3]

test bool b1tadd1() = add1(5) == 6;
test bool b1tadd2() = add2(5) == 7;
test bool b1tadd3() = add3(5) == 8;
