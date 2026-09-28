module lang::sexp::Parse

import lang::sexp::AST;
import lang::sexp::Syntax;
import ParseTree;

public data[SExp] parseSExp(str src, loc l) 
  = implode(#data[SExp], parse(#syntax[SExp], src, l));
