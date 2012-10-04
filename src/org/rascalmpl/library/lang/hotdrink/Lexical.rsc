module lang::hotdrink::Lexical

layout Layout 
  = WhitespaceAndComment* !>> [\ \t\n\r] !>> "/*" !>> "//";


lexical WhitespaceAndComment 
   = [\ \t\n\r]
   | @category="Comment" "/*" CommentChar* "*/"
   | @category="Comment" "//" ![\n]* $
   ;
   
lexical CommentChar
  = ![*]
  | [*] !>> [/]
  ;

lexical Identifier
  = ([a-zA-Z0-9_] !<< [a-zA-Z_] [a-zA-Z0-9_]* !>> [a-zA-Z0-9_]) \ Keywords
  ;

keyword Keywords = "empty" | "true" | "false";

lexical Number
  = [0-9]+ !>> [0-9]
  | [0-9] ("e" ("+"|"-")?) [0-9]+ !>> [0-9]
  ;

lexical String 
  = [\"] DQStrChar* [\"]
  | [\'] QStrChar* [\']
  ;
  
lexical DQStrChar
  = ![\"\\]
  | [\\][\\\"]
  ;
  
lexical QStrChar
  = ![\'\\]
  | [\\][\\\']
  ;