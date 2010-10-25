module zoo::pico::syntax::Main

import IO;

start syntax Program = program: "begin" Decls decls {Statement  ";"}* body "end" ;

syntax Decls = "declare" {IdType ","}* decls ";" ;  
 
syntax Statement = assign: Id var ":="  Exp val 
                 | cond:   "if" Exp cond "then" {Statement ";"}*  thenPart "else" {Statement ";"}* elsePart
                 | cond:   "if" Exp cond "then" {Statement ";"}*  thenPart
                 | loop:   "while" Exp cond "do" {Statement ";"}* body "od"
                 ;  

syntax IdType = Id id ":" Type type;
     
syntax Type = natural:"natural" 
            | string:"string" 
            | nil:"nil-type"
            ;

syntax Exp = id: Id name
           | strcon: Str string
           | natcon: Nat natcon
           | bracket "(" Exp e ")"
           > concat: Exp lhs "||" Exp rhs
           > left (add: Exp lhs "+" Exp rhs
                  |min: Exp lhs "-" Exp rhs
                  )
           ;

           
syntax Id  = lex [a-z][a-z0-9]* # [a-z0-9];
syntax Nat = lex [0-9]+ ;
syntax Str = lex "\"" ![\"]*  "\"";

layout Pico = WhitespaceAndComment*  
            # [\ \t\n\r]
            # "%"
            ;

syntax WhitespaceAndComment 
   = lex [\ \t\n\r]
   | lex "%" ![%]* "%"
   | lex "%%" ![\n]* "\n"
   ;

public list[tuple[Id,Exp]] assignments = [];

