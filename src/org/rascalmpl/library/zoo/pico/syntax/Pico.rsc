module zoo::pico::syntax::Pico

import zoo::pico::syntax::Layout;
import zoo::pico::syntax::Lexical;
    
start syntax PROGRAM = program: "begin" DECLS decls {STATEMENT  ";"}* body "end" ;
  
syntax DECLS = "declare" {IDTYPE ","}* decls ";" ;
 
syntax STATEMENT = assign: PICOID var ":="  EXP val
                 | cond:   "if" EXP cond "then" {STATEMENT ";"}*  thenPart "else" {STATEMENT ";"}* elsePart
                 | cond:   "if" EXP cond "then" {STATEMENT ";"}*  thenPart
                 | loop:   "while" EXP cond "do" {STATEMENT ";"}* body "od"
                 ;

syntax IDTYPE = PICOID id ":" TYPE typ;
   
syntax TYPE = natural:"natural" | string:"string" | nil:"nil-type";

syntax EXP = id: PICOID name
           | strcon: STRCON string
           | natcon: NATCON natcon
           | bracket "(" EXP e ")"
           ;
           
syntax EXP =  left mult: EXP lhs "*" EXP rhs
           >  left add: EXP lhs "+" EXP rhs
           ;
   
syntax PICOID = ...
              - "begin" 
              - "end" 
              - "declare" 
              - "natural" 
              - "string" 
              - "nil-type" 
              - "if" 
              - "while" 
              - "do" 
              - "then"
              - "else"
              - "od"
              ;
              
import ParseTree;

public EXP exp(str input) {
  return parseExperimental(#EXP, input);
}

public PROGRAM progr(str input) {
  return parseExperimental(#PROGRAM, input);
}


  

