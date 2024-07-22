module lang::rascal::tests::extends1::UseImportBaseExtended

import lang::rascal::tests::extends1::BaseExtended;

test bool base1() { S s = [S] "a";  return s == [S] "a";}

test bool base2() { Sstar s = [Sstar] "aaa";  return s == [Sstar] "aaa";}

test bool base3() { INTEGER n = 13; return n == 13;}

test bool base4() { D x = d1(); return x == d1();}

test bool base5() { return ident(13) == 13; }

test bool base6() { return f(0) == "zero"; }

test bool base7() { return f(9) == "value"; }

test bool extendedBase1() { S s = [S] "z";  return s == [S] "z";}

test bool extendedBase2() { Sstar s = [Sstar] "azaz";  return s == [Sstar] "azaz";}

test bool extendedBase3() { D x = d2(); return x == d2();}

test bool extendedBase4() { E x = e(); return x == e();}

test bool extendedBase5() { STRING s ="abc"; return s == "abc";}

test bool extendedBase6() { LIST_INTEGER lst = [1,2,3]; return lst == [1,2,3];}

test bool extendedBase7() { return ident("abc") == "abc"; }

test bool extendedBase8() { return f(1) == "one"; }
