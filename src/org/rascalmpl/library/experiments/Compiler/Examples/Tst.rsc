module experiments::Compiler::Examples::Tst

syntax AB = ("a" | "b")+ ;

syntax CD = ("c" | "d")+ ;

value main(list[value] args) = <[AB] "ababababab", [CD] "cddcdcdcdcdc">;