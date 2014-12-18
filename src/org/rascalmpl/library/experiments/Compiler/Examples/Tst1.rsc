module experiments::Compiler::Examples::Tst1

//import demo::lang::Exp::Concrete::WithLayout::Syntax;
layout Whitespace = w: [\t-\n\r\ ]*; /*1*/
    
lexical IntegerLiteral = i: [0-9]+;           

start syntax Exp 
  = e1: IntegerLiteral          
  | bracket "(" Exp ")"     
  > left e2: Exp "*" Exp        
  > left e3: Exp "+" Exp        
  ;

import ParseTree; 
import IO;   
                                                             
public value main(list[value] args) {
  //return #start[Exp].definitions[\start(sort("Exp"))];
  return [start[Exp]] " 7+  2*3";
}