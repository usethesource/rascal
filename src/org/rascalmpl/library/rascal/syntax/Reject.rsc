module rascal::syntax::Reject

import rascal::syntax::Grammar;

@doc{Produces all symbols that appear on the right-hand side of a reject (-)}
public set[Symbol] rejectedSymbols(Grammar gr) {
  return { s | /diff(_,_,s) := gr};
}