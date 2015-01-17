module experiments::Compiler::Examples::Tst3

import ParseTree;

syntax S 
  = "s"
  | "star" S* list "."
  ;
     
     
layout WS = [\t\n\ ]*;
 
// testing regular expressions

public S s = (S) `s`;

public S star_two = (S) `star <S s> <S s>.`;

value main(list[value] args) { if ((S*) x := star_two.\list) { return (S) `star <S* x>.` /* ==  (S) `star s s.` */; }}



public test bool splicestar1() = (S*) x := star_two.\list && (S) `star <S* x> <S* x>.` == (S) `star s s s s.`; 
