module zoo::pico::syntax::Pico

       
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
           | cons: EXP lhs "||" EXP rhs
                  | plus: EXP lhs "-" EXP rhs
                  | minus: EXP lhs "+" EXP rhs
           |
           | bracket "(" EXP e ")"
           ;
               
syntax PICOID = lex [a-z][a-z0-9]*;
syntax NATCON = lex [0-9]+ ;
syntax STRCON = lex "\"" ![\"]*  "\"";
 
layout Layout = [\ \t\n\r]
          | "%" ![%]* "%"
          | "%%" ![\n]* "\n"
          ;

public int main() {
  parseExperimental(#PROGRAM, "begin declare a : natural; a := 10 end");
}

  

