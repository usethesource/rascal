module experiments::Compiler::Examples::Tst

syntax A = "a";
//syntax As = A+;

//syntax B = "b";
//syntax Bs = B+;

//bool f([A] "a") = true;

value main(list[value] args) = (A) `a` := [A] "a";