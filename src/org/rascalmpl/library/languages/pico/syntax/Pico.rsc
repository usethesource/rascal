module languages::pico::syntax::Pico

import languages::pico::syntax::Layout;
import languages::pico::syntax::Lexical;

syntax start PROGRAM = program: "begin" <DECLS hoi> <{STATEMENT  ";"}* body> "end" ;

syntax DECLS = "declare" <{IDTYPE ","}* decls> ";" ;

syntax STATEMENT = assign : <PICOID var> ":="  <EXP val>
                 | cond:   "if" <EXP cond> "then" <{STATEMENT ";"}*  thenPart> "else" <{STATEMENT ";"}* elsePart>
                 | cond:   "if" <EXP cond> "then" <{STATEMENT ";"}*  thenPart>
                 | loop:   "while" <EXP cond> "do" <{STATEMENT ";"}* body> "od"
                 ;

syntax TYPE = natural:"natural" | string:"string" | nil:"nil-type";

syntax EXP = id: PICOID name
           | strcon: STRCON string
           | natcon: NATCON natcon
           | left ( cons: <EXP lhs> "||" <EXP rhs>
                  | plus: <EXP lhs> "-" <EXP rhs>
                  | minus: <EXP lhs> "+" <EXP rhs>
                  )
           | bracket "(" <EXP e> ")"
           ;
               
// added because we have not implemented imports yet
public syntax PICOID = lex id: [a-z] [a-z0-9]+
public syntax NATCON = lex [0-9]+ ;
public syntax STRCON = lex "\"" ~[\"]*  "\"";

public layout Layout = [\ \t\n\r]
         | "%" ~[%]* "%"
         | "%%" ~[\n]* "\n"
         ;