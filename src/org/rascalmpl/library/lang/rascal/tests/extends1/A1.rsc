module lang::rascal::tests::extends1::A1

extend lang::rascal::tests::extends1::Base;

import lang::rascal::tests::extends1::A2;

test bool a1base1() { S s = [S] "a";  return s == [S] "a";}

test bool a1base2() { Sstar s = [Sstar] "aaa";  return s == [Sstar] "aaa";}

test bool a1base3() { INTEGER n = 13; return n == 13;}

test bool a1base4() { D x = d1(); return x == d1();}

test bool a1base5() { return ident(13) == 13; }

test bool a1base6() { return f(0) == "zero"; }

test bool a1base7() { return f(9) == "value"; }

test bool a1extendedBase1() { S s = [S] "z";  return s == [S] "z";}

test bool a1extendedBase2() { Sstar s = [Sstar] "aza";  return s == [Sstar] "aza";}

test bool a1extendedBase3() { D x = d2(); return x == d2();}

test bool a1extendedBase4() { E x = e(); return x == e();}

test bool a1extendedBase5() { STRING s ="abc"; return s == "abc";}

test bool a1extendedBase6() { LIST_INTEGER lst = [1,2,3]; return lst == [1,2,3];}

test bool a1extendedBase7() { return ident("abc") == "abc"; }

test bool a1extendedBase8() { return f(1) == "one"; }


