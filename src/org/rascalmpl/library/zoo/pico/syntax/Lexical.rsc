module zoo::pico::syntax::Lexical

syntax PICOID = lex [a-z][a-z0-9]*;
syntax NATCON = lex [0-9]+ ;
syntax STRCON = lex "\"" ![\"]*  "\"";

