module experiments::Compiler::Examples::Tst

syntax AB = ("a" | "b")+ ;

value main(list[value] args) = [AB] "ababababab";