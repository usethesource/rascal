module lang::rascal::tests::extends1::UseImportBase

import lang::rascal::tests::extends1::Base;

test bool base1() { S s = [S] "a";  return s == [S] "a";}

test bool base2() { Sstar s = [Sstar] "aaa";  return s == [Sstar] "aaa";}

test bool base3() { INTEGER n = 13; return n == 13;}

test bool base4() { D x = d1(); return x == d1();}

test bool base5() { return ident(13) == 13; }

test bool base6() { return f(0) == "zero"; }

test bool base7() { return f(9) == "value"; }


