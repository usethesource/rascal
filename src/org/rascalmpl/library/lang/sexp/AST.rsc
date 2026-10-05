
@synopsis{AST model for S-Expressions.}
@contributor{Tijs van der Storm - storm@cwi.nl (CWI)}
module lang::sexp::AST

data SExp
  = string(String \str)
  | \list(list[SExp] elts)
  ;

data String
  = simple(SimpleString simpleStr)
  | display(str display, SimpleString simpleStr)
  ;

data SimpleString
  = raw(Raw raw)
  | token(str \value)
  | base64(list[str] chars)
  | hex(list[str] digits)
  | quoted(str \value)
  ;

data Raw
  = raw(int size, str bytes)
  ;
