module experiments::Compiler::Examples::ExpIssues

extend  experiments::Compiler::Tests::TestUtils;

value main(list[value] args) = run("d1(3, \"a\")");

//test bool tst() = run("d1(3, \"a\") is d1")  == d1(3, "a") is d1;
//
//test bool tst() = run("{[\<1, \"x\", 2\>, \<10, \"x\", 20\>][_, \"x\"];}") == [<1, "x", 2>, <10, "x", 20>][_, "x"];
//
//test bool tst() = run("{x = [1, 2, 3]; int elem = 0; try { elem = x[5]; } catch IndexOutOfBounds(int index): { elem = 100 + index; } elem; }", ["Exception"]) 
//					== {x = [1, 2, 3]; int elem = 0; try { elem = x[5]; } catch IndexOutOfBounds(int index): { elem = 100 + index; } elem; };
//					
//test bool tst() = run("{x = d1(1, \"a\"); x [-1];}") ==  {x = d1(1, "a"); x [-1];};
//
//test bool tst() = run("{x = d1(1, \"a\"); x [1];}") ==  {x = d1(1, "a"); x [1];};
//test bool tst() = run("all(int M \<- {1,2}, M == 1, true)") == all(int M <- {1,2}, M == 1, true);
//
//test bool tst() = run("all([*x, *y] := [1,2,3], true)") == !all([*x, *y] := [1,2,3], true);
//test bool tst() = run("any(x \<- [1,2,13,3], x \> 3)") == any(x <- [1,2,13,3], x > 3);
//test bool tst() = run("{ res = []; for([*int x,*int y] \<- [ [1,2],[3,4] ]) { res = res + [x,y]; } res; }") == { res = []; for([*int x,*int y] <- [ [1,2],[3,4] ]) { res = res + [x,y]; } res; };
//test bool tst() = run("res = []; for([int x, 5] \<- [[1,6], [2,5], [3, 5]], x != 2) res = res +[x];", "res") == {res = []; for([int x, 5] <- [[1,6], [2,5], [3, 5]], x != 2) res = res +[x]; res;};
//					
//test bool tst() = run("res = []; for([int x, 5] \<- [[1,5], [2,5], [3, 5]], x != 2) res = res +[x];", "res") == {res = []; for([int x, 5] <- [[1,5], [2,5], [3, 5]], x != 2) res = res +[x]; res;};
//					