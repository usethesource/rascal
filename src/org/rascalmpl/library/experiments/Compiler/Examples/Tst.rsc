module experiments::Compiler::Examples::Tst

public value main(list[value] args) =  [ <s,r,L> | list[int] L:[*str s, *str r] <- [ [1,2], ["3","4"] ]];

