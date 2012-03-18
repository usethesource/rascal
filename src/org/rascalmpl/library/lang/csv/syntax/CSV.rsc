module lang::csv::syntax::CSV

start syntax Table 
  = table: {Record EOL}*
  ;

syntax Record // todo: $ does match single \r 
  = record: {Field ","}+ 
  ;

syntax Field
  = unquoted: UQStr
  | quoted: QStr
  ;
  
lexical UQStr
  = ![\n\r\",]* !>> ![\n\r\",]
  ;
    
lexical QStr
  = [\"] QChar* [\"]
  ;
  
lexical QChar
  = ![\"\n\r]
  | QStr
  ;
  
lexical EOL
  = [\n]
  | [\r][\n]
  | [\r] !>> [\n]
  ; 
  
