module lang::dimacs::IO

import lang::logic::\syntax::Propositions;
import lang::dimacs::\syntax::Dimacs;
import ParseTree;

/*
c A sample .cnf file.
p cnf 3 2
1 -3 0
2 3 -1 0 
*/
public Formula readDimacs(loc l) = dimacsToFormula(parse(#start[Dimacs], l).top);

public Formula dimacsToFormula(Dimacs d) 
  = and({ or({(n is positive) ? id("<n>") : not(id("<n.number>")) | n <- l.disjunct.numbers}) | Line l <- d.lines, l is disjunct});
