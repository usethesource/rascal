module lang::rascalcore::compile::Examples::Tst1

import ParseTree;

lexical Id = [a-z] !<< [a-z]+ !>> [a-z];
lexical Num = [0-9]+;
layout W = [\ \t\n\r]*;

syntax Exp 
  = 
    number: Num
  | lst: "[" {Exp!com ","}*  "]"
  > clop: Exp "+"
  | clos: Exp "*"
  > left mul: Exp "*" Exp
  > left 
    ( add: Exp "+" Exp
    | sub: Exp "-" Exp
    ) 
  > bracket "(" Exp exp ")" 
  ;

syntax Exp = left( add: Exp "+" Exp | minmin: Exp "--" Exp);

// "modular" extensions in the priority relation
syntax Exp = :mul > left Exp "/" Exp > :add;
syntax Exp = :add > Exp "." Exp;