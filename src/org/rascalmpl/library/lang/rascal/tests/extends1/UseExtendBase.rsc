module lang::rascal::tests::extends::UseExtendBase

extend lang::rascal::tests::extends::Base;

syntax S = "b";

test bool base1() { S s = [S] "a";  return s == [S] "a";}

test bool base2() { Sstar s = [Sstar] "aaa";  return s == [Sstar] "aaa";}

test bool base3() { INTEGER n = 13; return n == 13;}

test bool base4() { D x = d1(); return x == d1();}

test bool base5() { return ident(13) == 13; }

test bool base6() { return f(0) == "zero"; }

test bool base7() { return f(9) == "value"; }

// Extensions

test bool extend1() { S s = [S] "b";  return s == [S] "b";}

test bool extend2() { Sstar s = [Sstar] "abab";  return s == [Sstar] "abab";}

str f(2) = "two";

test bool extend3() { return f(2) == "two"; }



