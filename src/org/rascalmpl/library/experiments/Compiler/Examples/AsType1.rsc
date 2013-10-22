module experiments::Compiler::Examples::AsType1

syntax A = "a";
syntax As = A+;

syntax B = "b";
syntax Bs = B+;

value main(list[value] args) = <[As] "aaaa", [Bs] "bbb" >;