module lang::rascalcore::compile::Examples::Tst2
 
 
syntax IdType = idtype: Id id ":" Type t;

syntax Type 
  = natural:"natural" 
   ;
     
lexical Id  = [a-z][a-z0-9]* !>> [a-z0-9];

layout Layout = WhitespaceAndComment* !>> [\ \t\n\r%];

lexical WhitespaceAndComment 
   = [\ \t\n\r]
   ;
