module experiments::Compiler::Examples::Tst1

import ParseTree;

//lexical MyName = ([A-Z a-z _] !<< [A-Z _ a-z] [0-9 A-Z _ a-z]* !>> [0-9 A-Z _ a-z]) ;
lexical A = [A-Z a-z _] [0-9 A-Z _ a-z]*;
value main(list[value] args) = (A) `a` := (A) `a`;


//(MyName) `location` := (MyName) `location`;