module experiments::Compiler::Examples::Tst

import experiments::Compiler::Examples::ParseTreeDataType;

syntax AB = ("a" | "b")+ ;

syntax CD = ("c" | "d")+ ;

value main(list[value] args) = < [AB] "ababababab", [CD] "cdcdcdcdcdc" >;