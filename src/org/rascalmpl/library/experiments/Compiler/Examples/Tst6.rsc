module experiments::Compiler::Examples::Tst6
 import ParseTree;

lexical Id = [a-z] !<< [a-z]+ !>> [a-z];
lexical Num = [0-9]+;
layout W = [\ \t\n\r]*;

syntax Exp 
  = id: Id
  | number: Num
  | lst: "[" {Exp!com ","}*  "]"
  | ind: Exp!clop!clos "[" {Exp!com ","}+ "]"
  > clop: Exp "+"
  | clos: Exp "*"
  > left mul: Exp "*" Exp
  > left 
    ( add: Exp "+" Exp
    | sub: Exp "-" Exp
    ) 
  > bracket "(" Exp exp ")" 
  | com: Exp "," Exp
  ;

Exp noBrackets(Exp e) = visit (e) { case (Exp) `(<Exp a>)` => a };

value main() = /*noBrackets(parse(#Exp,"(a + b) + c")) == */parse(#Exp, "a + b + c");
