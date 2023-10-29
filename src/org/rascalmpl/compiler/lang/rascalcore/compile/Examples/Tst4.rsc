@bootstrapParser
module lang::rascalcore::compile::Examples::Tst4

import List;

import lang::rascal::tests::functionality::CallAux;

// backtracking tests, also uses an alternative from CallTestsAux

C c(int i) {
  if (i == 0 || i mod 3 != 0) 
    fail c;
  else
    return c(i / 3);
}

C c(int i) {
  if (i == 0 || i mod 2 != 0) 
    fail c;
  else
    return c(i / 2);
}

C c(int i) = c(i / 7) when i mod 7 == 0, i != 0;

value main() //test bool bt1() 
    = c(7 * 5 * 3 * 2);// == c(1);
    
//test bool bt2() = c(5 * 3 * 2) == c(1);
