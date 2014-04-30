@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
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
  pcd = {n | m <- d.modules, \parameterized-sort(n,_) <- d.modules[m].grammar.rules};
  lx = {n | m <- d.modules, \lex(n) <- d.modules[m].grammar.rules};
  tk = {n | m <- d.modules, \token(n) <- d.modules[m].grammar.rules};
  plx = {n | m <- d.modules, \parameterized-lex(n,_) <- d.modules[m].grammar.rules};
  ks = {n | m <- d.modules, \keywords(n) <- d.modules[m].grammar.rules};
  ls = {n | m <- d.modules, \layouts(n) <- d.modules[m].grammar.rules};
  
  return visit(d) {
    case sort(n) : {
      if (n in lx) insert \lex(n);
      if (n in ks) insert \keywords(n);
      if (n in ls) insert \layouts(n);
      if (n in tk) insert \token(n);
      fail;
    }
    case \parameterized-sort(n,ps) : {
      if (n in plx) insert \parameterized-lex(n,ps);
      fail;
    }
    case lex(n) : {
      if (n in cd) insert \sort(n);
      if (n in ks) insert \keywords(n);
      if (n in ls) insert \layouts(n);
      if (n in tk) insert \token(n);
      fail;
    }
    case \parameterized-lex(n,ps) : {
      if (n in pcd) insert \parameterized-sort(n,ps);
      fail;
    }
    case \token(n) : {
      if (n in cd) insert \sort(n);
      if (n in ks) insert \keywords(n);
      if (n in ls) insert \layouts(n);
      if (n in lx) insert \lex(n);
      fail;
    }
  }
}

public str unescape(str name) {
  if (/\\<rest:.*>/ := name) return rest; 
  return name;
}
