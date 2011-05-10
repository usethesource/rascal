@bootstrapParser
module lang::rascal::grammar::definition::Lexical

import lang::rascal::syntax::RascalRascal;
import lang::rascal::grammar::definition::Attributes;
import ParseTree;
import Grammar;

public Production lexical(Production prod) {
  return visit(prod) {
    case p:prod(_,_,_) => attribute(p, \lex())
  } 
}