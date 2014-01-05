module experiments::Compiler::Examples::ConcreteTerms

import ParseTree;
layout Whitespace = [\ ]*;
lexical IntegerLiteral = [0-9]+; 
lexical Identifier = [a-z]+;

syntax Exp 
  = IntegerLiteral  
  | Identifier        
  | bracket "(" Exp ")"     
  > left Exp "*" Exp        
  > left Exp "+" Exp  
  | Exp "==" Exp      
  ;

syntax Stat 
   = Identifier ":=" Exp
   | "if" Exp cond "then" {Stat ";"}* thenPart "else" {Stat ";"}* elsePart "fi"
   ;
   
value main(list[value] args){
  e = [Exp] "x + 1";
  s1 = [Stat] "a := 2 * 3";
  s2 = [Stat] "b := 4 + 5";
  
  s3 = (Stat) `if <Exp e> then <Stat s1>;<Stat s2> else <Stat s1> fi`;
  
  return s3.thenPart[0];
}