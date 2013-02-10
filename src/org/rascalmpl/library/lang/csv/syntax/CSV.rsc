module lang::csv::\syntax::CSV

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
  = ![\n\r\",] ![\n\r,]* !>> ![\n\r,] 
  | /* empty */
  ;
    
lexical QStr
  = [\"] QChar* [\"] !>> ![,\n\r]
  ;
  
lexical QChar
  = ![\"\n\r]
  | [\"][\"]
  ;
  
lexical EOL
  = [\n]
  | [\r][\n]
  | [\r] !>> [\n]
  ; 
  
