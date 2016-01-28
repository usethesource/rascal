module experiments::Compiler::Examples::Tst1

value main() {M = (1:10); M[1] ? 0 += 100; M;}




//test bool tst() = run("{M = (1:10); M[2] ? 0 += 100; M;}") == {M = (1:10); M[2] ? 0 += 100; M;};
//
//test bool tst() = run("{M = (1:10); M[1] ?= 100; M;}") == {M = (1:10); M[1] ?= 100; M;};
//
//test bool tst() = run("{M = (1:10); M[2] ?= 100; M;}") == {M = (1:10); M[2] ?= 100; M;};
//

//value main()
//= [int x] := [1];

// {res = []; for([int x, 5] <- [[1,5], [2,5], [3, 5]], x != 2) res = res +[x]; res;}