module zoo::pico::syntax::Pico

import zoo::pico::syntax::Layout;
import zoo::pico::syntax::Lexical;
    
start syntax Program = program: "begin" DECLS decls {Statement  ";"}* body "end" ;

syntax Decls = "declare" {IdType ","}* decls ";" ;
 
syntax Statement = assign: Id var ":="  Exp val
                 | cond:   "if" Exp cond "then" {Statement ";"}*  thenPart "else" {Statement ";"}* elsePart
                 | cond:   "if" Exp cond "then" {Statement ";"}*  thenPart
                 | loop:   "while" Exp cond "do" {Statement ";"}* body "od"
                 ;

syntax IDTYPE = Id id ":" Type typ;
   
syntax TYPE = natural:"natural" 
            | string:"string" 
            | nil:"nil-type"
            ;

syntax Exp = id: Id name
           | strcon: Str string
           | natcon: Nat natcon
           | bracket "(" Exp e ")"
           ;
           
syntax Exp =  left  mult: Exp lhs "||" Exp rhs
           >  left ( sub: Exp lhs "-" Exp rhs
                   | add: Exp lhs "-" Exp rhs
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