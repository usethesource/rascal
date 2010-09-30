module Ambiguity

import ParseTree;
import IO;
import ValueIO;
import rascal::syntax::Grammar2Rascal;

public void report(str amb) {
  report(readTextValueString(#Tree, amb));
}

public void report(Tree amb) {
  for (x <- uniqueProductions(amb.alternatives)) {
    println("alternative: <for (e <- x) {>\n\tsyntax <symbol2rascal(e.rhs)> = <prod2rascal(e)><}>");
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