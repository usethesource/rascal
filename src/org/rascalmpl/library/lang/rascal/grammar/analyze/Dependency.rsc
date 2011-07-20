module lang::rascal::grammar::analyze::Dependency

import Grammar;
import ParseTree;
import Graph;

@doc{
  Compute the symbol dependency graph. This graph does not report intermediate nodes
  for regular expressions.
}
@experimental
public Graph[Symbol] symbolDependencies(Grammar g) {
  return { <from,to> | /prod(Symbol from,[_*,/Symbol to,_*],_) := g, to is sort || to is lex};
}
