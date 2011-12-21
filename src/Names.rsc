module lang::rascal::grammar::definition::Names

import ParseTree;
import Grammar;

@doc{All uses of names are initially labeled 'sort', while declarations put
them in four classes: normal, lex, keywords and layout. This function will
mark all uses accordingly such that the proper interpretation can be done
by semantic processing of parse trees
}
public GrammarDefinition resolve(GrammarDefinition d) {
  cd = {n | m <- d.modules, \sort(n) <- d.modules[m].grammar.rules};
  lx = {n | m <- d.modules, \lex(n) <- d.modules[m].grammar.rules};
  ks = {n | m <- d.modules, \keywords(n) <- d.modules[m].grammar.rules};
  ls = {n | m <- d.modules, \layouts(n) <- d.modules[m].grammar.rules};
  
  return visit(d) {
    case sort(n) : {
      if (n in lx) insert \lex(n);
      if (n in ks) insert \keywords(n);
      if (n in ls) insert \layouts(n);
      fail;
    }
    case lex(n) : {
      if (n in cd) insert \sort(n);
      if (n in ks) insert \keywords(n);
      if (n in ls) insert \layouts(n);
      fail;
    }
  }
}

public str unescape(str name) {
  if (/\\<rest:.*>/ := name) return rest; 
  return name;
}
