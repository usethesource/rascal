module experiments::Compiler::Examples::Tst6

syntax A = "a";

test bool concreteMatch01() = (A) `<A a>` := [A] "a";