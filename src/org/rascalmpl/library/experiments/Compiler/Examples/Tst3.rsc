module experiments::Compiler::Examples::Tst3


import ParseTree;

lexical MyName = ([A-Z a-z _] !<< [A-Z _ a-z] [0-9 A-Z _ a-z]* !>> [0-9 A-Z _ a-z]) ;


value main(list[value] args) = (MyName) `location` := (MyName) `location`;
 