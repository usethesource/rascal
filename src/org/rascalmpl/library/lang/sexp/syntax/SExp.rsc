module lang::sexp::syntax::SExp

// http://people.csail.mit.edu/rivest/Sexp.txt

start syntax SExp
  = string: String
  | \list: List
  ;

syntax String
  = simple: SimpleString
  | display: Display SimpleString
  ;

syntax SimpleString
  = raw: Raw
  | token: Token
  | base64: Base64
  | hex: HexaDecimal
  | quoted: QuotedString
  ;

syntax Display
  = "[" SimpleString "]"
  ;

lexical Raw
  = Decimal ":" Bytes
  ;
  
lexical Decimal
  = [1-9][0-9]* !>> [0-9]
  | [0]
  ; 

lexical Bytes
  = ![]*;  // any string of bytes, of the indicated length in Raw;-- does not work
  
lexical Token
  = TokenChar+
  ;

syntax Base64
  = "|" Base64Char* "|" // nb: whitespace allowed
  ;

syntax HexaDecimal
  = "#" HexDigit* "#"; // nb: whitespace allowed
  
lexical QuotedString
  = [\"] QSChar* [\"] 
  ;
  
lexical QSChar
  = ![\"\'\\\n\r]
  | [\\][btvnfr\"\'\\]
  | [\\][0-7][0-7][0-7]
  | [\\][x] HexDigit HexDigit
  | [\\][\n\r]
  | [\\][\r][\n]
  | [\\][\n][\r]
  ;

syntax List
  = "(" SExp* ")"
  ;

layout Whitespace
  = WS* !>> [\ \t\n\r]
  ;
  
lexical WS
  = [\ \t\n\r]
  ;

lexical TokenChar
  = Alpha
  | DecimalDigit
  | SimplePunc
  ;

lexical Alpha
  = [a-zA-Z]
  ;

lexical DecimalDigit
  = [0-9]
  ;

lexical HexDigit
  = [0-9A-Fa-f]
  ;
  
lexical SimplePunc
  = [\-./_:*+=]
  ;

lexical Base64Char
  = Alpha
  | DecimalDigit
  | [+/=]
  ;