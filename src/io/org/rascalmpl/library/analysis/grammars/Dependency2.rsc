@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module analysis::grammars::Dependency2

import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::check::AType;
import analysis::graphs::Graph;

@doc{
  Compute the symbol dependency graph. This graph does not report intermediate nodes
  for regular expressions.
}
@experimental
public Graph[AType] symbolDependencies(AGrammar g) {}
//  { <from,to> | /prod(AType s,[_*,AType elem,_*],_) := g, /AType to := elem, (label(_,AType from) := s || AType from := s), to is sort || to is lex || to is \parameterized-sort, from is sort || from is lex || from is \parameterized-sort};

public Graph[AType] symbolDependencies(AGrammarDefinition d) {}
//  { *symbolDependencies(d.modules[m].grammar) | m <- d.modules };
