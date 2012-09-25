module lang::rascal::grammar::analyze::Dependency

import Grammar;
import ParseTree;
import analysis::graphs::Graph;

@doc{
  Compute the symbol dependency graph. This graph does not report intermediate nodes
  for regular expressions.
}
@experimental
public Graph[Symbol] symbolDependencies(Grammar g) =
  { <from,to> | /prod(Symbol s,[_*,/Symbol to,_*],_) := g, (label(_,Symbol from) := s || Symbol from := s), to is sort || to is lex || to is \parameterized-sort, from is sort || from is lex || from is \parameterized-sort};

public Graph[Symbol] symbolDependencies(GrammarDefinition d) =
  { symbolDependencies(d.modules[m].grammar) | m <- d.modules };
    
