module languages::pico::syntax::Pico
 
import languages::pico::syntax::Layout;
import languages::pico::syntax::Lexical;
  
start syntax PROGRAM = program: "begin" <DECLS hoi> <{STATEMENT  ";"}* body> "end" ;
  
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
               

