module zoo::pico::syntax::Pico

public syntax PICOID = lex id: [a-z] [a-z0-9]+
public syntax NATCON = lex [0-9]+ ;
public syntax STRCON = lex "\"" ~[\"]*  "\"";