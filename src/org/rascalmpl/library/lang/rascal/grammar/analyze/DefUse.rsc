module lang::rascal::grammar::analyze::DefUse

import analysis::grammars::Grammar;
import ParseTree;

private bool definable(Symbol s) {
  return s is \sort || s is \lex || s is \keywords || s is \layouts || s is \parameterized-sort;
}

public tuple[set[Symbol] used, set[Symbol] defined] usedAndDefined(Grammar g) {
  used = { s | /prod(l, _, _) := g, /Symbol s <- l, definable(s)};
  defined = g.rules<0>;
  return visit(<used, defined>) { case \parameterized-sort(x,_) => \sort(x) } 
}  
 
public set[Symbol] usedNotDefined(Grammar g) {
  <used, defined> = usedAndDefined(g);
  return used - defined;
}

public set[Symbol] definedNotUsed(Grammar g) {
  <used, defined> = usedAndDefined(g);
  return used - {s | s <- defined, !(s is \start)}; 
}
