module experiments::Compiler::Examples::Tst1

import IO;

value main() = <123> := <124> ? 10 : 20;



//{ for(int x <- [1, 2, 3], x >= 2, int y <- {10,20,30}) println(x+y); return 12345;}
// { x = true; for(x <- [-1,2,-3], x > 0) println(x); return 12345;}
// 1 := 2 ? 10 : 20;

//<123,1,2> := <123,2,2> ? 10 : 20;
//<123> := <123> ? 10 : 20;

// <1,2> := <1,2> ? 10 : 20;

//"f"() := "f"() ? 10 : 20;
//"f"(1) := "f"(1) ? 10 : 20;


//a/ := "a" ? 10 : 20;


//[1] := [1] ? 10 : 20;

//{ int n = 0; for(x <- [1,2,3]) { println(n); n += x; } return n;}
//{ int n = 0; for(x <- {1,2,3}, y <- [10,20,30]) n += x+y; return n;}