@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::grammar::definition::Names

import ParseTree;
import Grammar;

@synopsis{All uses of names are initially labeled 'sort', while declarations put
them in four classes: normal, lex, keywords and layout. This function will
mark all uses accordingly such that the proper interpretation can be done
by semantic processing of parse trees}
public Grammar resolve(Grammar d) {
  cd = {n | \sort(n) <- d.rules};
  pcd = {n | \parameterized-sort(n,_) <- d.rules};
  lx = {n | \lex(n) <- d.rules};
  plx = {n | \parameterized-lex(n,_) <- d.rules};
  ks = {n | \keywords(n) <- d.rules};
  ls = {n | \layouts(n) <- d.rules};
  
  return visit(d) {
    case sort(n) : {
      if (n in lx) insert \lex(n);
      if (n in ks) insert \keywords(n);
      if (n in ls) insert \layouts(n);
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
      fail;
    }
    case \parameterized-lex(n,ps) : {
      if (n in pcd) insert \parameterized-sort(n,ps);
      fail;
    }
  }
}

public str unescape(str name) {
  if (/\\<rest:.*>/ := name) return rest; 
  return name;
}
