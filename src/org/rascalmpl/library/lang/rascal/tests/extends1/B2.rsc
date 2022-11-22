module lang::rascal::tests::extends1::B2

import lang::rascal::tests::extends1::B1;
import lang::rascal::tests::extends1::B3;

import lang::rascal::tests::extends1::BaseExtended;

int add2(int n) = n + 2;

test bool base1() { S s = [S] "a";  return s == [S] "a";}

test bool base2() { Sstar s = [Sstar] "aaa";  return s == [Sstar] "aaa";}

test bool base3() { INTEGER n = 13; return n == 13;}

test bool base4() { D x = d1(); return x == d1();}

test bool base5() { return ident(13) == 13; }

test bool base6() { return f(0) == "zero"; }

test bool base7() { return f(9) == "value"; }

test bool extendedBase1() { S s = [S] "z";  return s == [S] "z";}

test bool extendedBase2() { Sstar s = [Sstar] "aza";  return s == [Sstar] "aza";}

test bool extendedBase3() { D x = d2(); return x == d2();}

test bool extendedBase4() { E x = e(); return x == e();}

test bool extendedBase5() { STRING s ="abc"; return s == "abc";}

test bool extendedBase6() { LIST_INTEGER lst = [1,2,3]; return lst == [1,2,3];}

test bool extendedBase7() { return ident("abc") == "abc"; }

test bool extendedBase8() { return f(1) == "one"; }

// Defined in B[1-3]

test bool tadd1() = add1(5) == 6;
test bool tadd2() = add2(5) == 7;
test bool tadd3() = add3(5) == 8;
