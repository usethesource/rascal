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
           >  left  EXP lhs "1" EXP rhs
           >  left  EXP lhs "2" EXP rhs
           >  left  EXP lhs "3" EXP rhs
           >  left  EXP lhs "4" EXP rhs
           >  left  EXP lhs "5" EXP rhs
           >  left  EXP lhs "6" EXP rhs
           >  left  EXP lhs "7" EXP rhs
           >  left  EXP lhs "8" EXP rhs
           >  left  EXP lhs "9" EXP rhs
           >  left  EXP lhs "10" EXP rhs
           >  left  EXP lhs "11" EXP rhs
           > left EXP lhs "12" EXP rhs > left EXP lhs "13" EXP rhs > left EXP lhs "14" EXP rhs > left EXP lhs "15" EXP rhs > left EXP lhs "16" EXP rhs > left EXP lhs "17" EXP rhs > left EXP lhs "18" EXP rhs > left EXP lhs "19" EXP rhs > left EXP lhs "20" EXP rhs
           ;
   
syntax EXP2 = Basic
            | EXP3;

syntax EXP3 = Op EXP3 | ; 
            
syntax Basic = id: PICOID name
           | strcon: STRCON string
           | natcon: NATCON natcon
           | bracket "(" EXP e ")"
           ;
           
syntax Op = "+" | "*" | "1" | "2"  | "3"  | "4"  | "5"  | "6"  | "7"  | "8"  | "9"  | "10"  | "11"  | "12"  | "13"  | "14"  | "15"  | "16"  | "17"  | "18"  | "19"  | "20" ;           

import ParseTree;

public EXP exp(str input) {
  return parseExperimental(#EXP, input);
}

public EXP2 exp2(str input) {
  return parseExperimental(#EXP2, input);
}

public PROGRAM progr(str input) {
  return parseExperimental(#PROGRAM, input);
}


  

