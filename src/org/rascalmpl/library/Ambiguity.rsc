@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module Ambiguity

import ParseTree;
import IO;
import ValueIO;
import lang::rascal::syntax::Grammar2Rascal;

public void report(str amb) {
  report(readTextValueString(#Tree, amb));
}

public void report(Tree amb) {
  for (x <- uniqueProductions(amb.alternatives)) {
    println("alternative: <for (e <- x) {>
            '  syntax <symbol2rascal(e.rhs)> = <prod2rascal(e)><}>");
  }
}

@doc{
  Summarize trees as sets of productions.
}
public set[set[Production]] productions(set[Tree] alts) {
  return { {{ p | /Production p := a }} | Tree a <- alts }; 
}

@doc{
  Reduce a set of alternative trees to sets of productions, 
  where each set contains only productions that are unique to one of the original trees
}
public set[set[Production]] uniqueProductions(set[Tree] alts) {
  prods = productions(alts);
  solve (prods) 
    prods = { {p - q}, {q - p} | p <- prods, q <- prods, p != q};
  return prods;
}

@doc{
 Reduce a set of alternative trees to sets of productions,
 where each set contains only productions that are unique to one of the original trees,
 and no production is included in such set that is above another production in the same tree
 
 TODO: make this function faster
}
public set[set[Production]] uniqueTopProductions(set[Tree] alts) {
  prods = uniqueProductions(alts);
  return { {{ p1 | p1 <- class, p2 <- class, !(/appl(p2,/appl(p1,_)) <- alts)}} | class <- prods};
}
