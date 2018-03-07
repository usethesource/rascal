module lang::rascalcore::check::Test2

import ParseTree;

lexical Id = [a-z] !<< [a-z]+ !>> [a-z];
//lexical Num = [0-9]+;
layout W = [\ \t\n\r]*;

syntax Exp 
  = id: Id
  //| number: Num
  //| lst: "[" {Exp!com ","}*  "]"
  //| ind: Exp!clop!clos "[" {Exp!com ","}+ "]"
  //> clop: Exp "+"
  //| clos: Exp "*"
  //> left mul: Exp "*" Exp
  > left 
    ( add: Exp "+" Exp
    | sub: Exp "-" Exp
    ) 
  //> bracket "(" Exp exp ")" 
  //| com: Exp "," Exp
  ;

//syntax Exp = left( add: Exp "+" Exp | minmin: Exp "--" Exp);

// "modular" extensions in the priority relation
//syntax Exp = :mul > left Exp "/" Exp > :add;
syntax Exp = :add > Exp "." Exp;