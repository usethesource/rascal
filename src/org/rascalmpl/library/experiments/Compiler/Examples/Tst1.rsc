module experiments::Compiler::Examples::Tst1


value main() = <1> := <1> ? 10 : 20;


//"f"(1) := "f"(1) ? 10 : 20;


//a/ := "a" ? 10 : 20;


//[1] := [1] ? 10 : 20;


//{ int n = 0; for(x <- {1,2,3}, y <- [10,20,30]) n += x+y; return n;}