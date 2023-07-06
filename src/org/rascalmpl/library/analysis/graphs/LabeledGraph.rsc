@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module analysis::graphs::LabeledGraph

import analysis::graphs::Graph;
import Relation;

alias LGraph[&T,&L] = rel[&T from, &L label, &T to];


@synopsis{

Return the bottom nodes of a LGraph.
}
public set[&T] bottom(LGraph[&T,&L] G)
{
  return G.to - G.from;
}


@synopsis{

The predecessors of a single node in a LGraph.
}
public set[&T] predecessors(LGraph[&T,&L] G, &T From)
{
  return invert(G<from,to>)[From];
}


@synopsis{

Reachability from a given start set of nodes.
}
public set[&T] reach(LGraph[&T,&L] G, set[&T] Start)
{
	return reach(G<from,to>, Start);
}


@synopsis{

Reachability from given start set of nodes with restrictions.
}
public set[&T] reachR(LGraph[&T,&L] G, set[&T] Start, set[&T] Restr)
{
	return reachR(G<from,to>, Start, Restr);
}


@synopsis{

Reachability from given start set of nodes with exclusions.
}
public set[&T] reachX(LGraph[&T,&L] G, set[&T] Start, set[&T] Excl)
{
   return reachX(G<from,to>, Start, Excl);
}


@synopsis{

The successors of a single node in a LGraph.
}
public set[&T] successors(LGraph[&T, &L] G, &T From)
{
  return G<from,to>[From];
}


@synopsis{

Return the top nodes of a LGraph.
}
public set[&T] top(LGraph[&T,&L] G)
{
  return G.from - G.to;
}
