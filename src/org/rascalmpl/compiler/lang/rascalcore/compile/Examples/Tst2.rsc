module lang::rascalcore::compile::Examples::Tst2
  
 test bool tst_in(int A, set[int] B) = A in (A + B) && A in (B + A);
 
//test bool escape3() {  list[tuple[int \n,str \type]] L = [<1, "a">, <2, "b">]; return L[0].\n == 1; }

//test bool reflexEq(value x) = x := x;