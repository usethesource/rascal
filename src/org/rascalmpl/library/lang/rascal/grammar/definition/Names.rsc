module lang::rascal::grammar::definition::Names

import ParseTree;
import Grammar;

@doc{All uses of names are initially labeled 'sort', while declarations put
them in four classes: normal, lex, keywords and layout. This function will
mark all uses accordingly such that the proper interpretation can be done
by semantic processing of parse trees
}
public Grammar resolve(Grammar g) {
  lx = {n | \lex(n) <- g.rules};
  ks = {n | \keywords(n) <- g.rules};
  ls = {n | \layouts(n) <- g.rules};
  
  return visit(g) {
    case sort(n) : {
      if (n in lx) insert \lex(n);
      if (n in ks) insert \keywords(n);
      if (n in ls) insert \layouts(n);
      fail;
    }
  }
}