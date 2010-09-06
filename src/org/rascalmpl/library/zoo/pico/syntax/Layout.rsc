module zoo::pico::syntax::Layout

layout PicoLayout = WhitespaceAndComment*
                  # [\ \t\n\r]
                  # "%"
                  ;

syntax WhitespaceAndComment 
   = lex [\ \t\n\r]
   | lex "%" ![%]* "%"
   | lex "%%" ![\n]* "\n"
   ;