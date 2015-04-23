
module experiments::Compiler::Examples::Tst3

import ParseTree;



syntax A = "a";
syntax As = A+;


value main(list[value] args) { return (A) `<A a>` := [A] "a"; }