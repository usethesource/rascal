@doc{
.Synopsis
Syntax definition for S-Expressions, based on http://people.csail.mit.edu/rivest/Sexp.txt
}
@contributor{Tijs van der Storm - storm@cwi.nl (CWI)}

module lang::sexp::\syntax::SExp

import String;
import IO;

start syntax SExp
  = string: String
  | \list: "(" SExp* ")"
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
  = bracket "[" SimpleString "]"
  ;

syntax Raw
  = raw: Decimal >> [:] ":" !>> [\ \t\n\r] Bytes
  ;
  
lexical Decimal
  = [1-9][0-9]* !>> [0-9]
  | [0]
  ; 

lexical Bytes
  = ![]*;  
  
lexical Token
  = TokenChar+ !>> [a-zA-Z0-9\-./_:*+=]
  ;

syntax Base64
  = bracket "|" Base64Char* "|" // nb: whitespace allowed
  ;

syntax HexaDecimal
  = bracket "#" HexDigit* "#"; // nb: whitespace allowed
  
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
  
  
public Raw raw(Decimal d, Bytes bs) {
  int l = toInt(unparse(d));
  str s = unparse(bs);
  println("L = <l>");
  println("s = \"<s>\"");
  if (l != size(s)) {
    filter;
  }
  else {
    fail;
  }
}

str unparse(Bytes _){
    throw "unparse Bytes not implemented";
}

str unparse(Decimal _){
    throw "unparse Decimal not implemented";
}
