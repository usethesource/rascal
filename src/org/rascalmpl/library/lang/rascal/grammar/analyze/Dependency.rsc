@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascal::grammar::analyze::Dependency

import Grammar;
import ParseTree;
import analysis::graphs::Graph;

@synopsis{Compute the symbol dependency graph. This graph does not report intermediate nodes
  for regular expressions.}
@experimental
public Graph[Symbol] symbolDependencies(Grammar g) =
  { <from,to> | /prod(Symbol s,[*_,Symbol elem,*_],_) := g, /Symbol to := elem, Symbol from := ((label(_,Symbol f) := s) ? f : s), to is sort || to is lex || to is \parameterized-sort, from is sort || from is lex || from is \parameterized-sort};

public Graph[Symbol] symbolDependencies(GrammarDefinition d) =
  { *symbolDependencies(d.modules[m].grammar) | m <- d.modules };
    
