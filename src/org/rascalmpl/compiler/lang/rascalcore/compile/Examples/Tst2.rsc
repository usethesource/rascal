@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2

extend lang::rascalcore::compile::Examples::Tst1;

test bool base2() { Sstar s = [Sstar] "aaa";  return s == [Sstar] "aaa";}
//test bool extendedBase2() { Sstar s = [Sstar] "aza";  return s == [Sstar] "aza";}

