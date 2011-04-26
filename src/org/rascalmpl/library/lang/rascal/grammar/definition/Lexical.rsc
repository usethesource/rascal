@bootstrapParser
module lang::rascal::grammar::definition::Lexical

import lang::rascal::syntax::RascalRascal;
import ParseTree;
import Grammar;

public set[Production] lexical(set[Production] prods) {
  return visit(prods) {
    case p:prod(_,_,_) => attribute(p, \lex())
  } 
}