module experiments::Compiler::Examples::Tst1

import ParseTree;
//layout Whitespace = [\t-\n\r\ ]*; /*1*/
//    
//lexical IntegerLiteral = [0-9]+;           
//
//start syntax Exp 
//  = IntegerLiteral          
//  | bracket "(" Exp ")"     
//  > left Exp "*" Exp         
//  > left Exp "+" Exp        
//  ;
//
//value main(list[value] args){
//
//  P = parse(#Exp, "1 + 2");
//  return (Exp) `1 + 2` := P;
//}