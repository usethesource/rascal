module LabeledGraph

import Graph;
import Set;
import Relation;
import IO;

alias lgraph[&T,&L] = rel[&T from, &L label, &T to];

public set[&T] top(lgraph[&T,&L] G)
@doc{top -- return the top nodes of a lgraph}
{
  return G.from - G.to;
}

public set[&T] bottom(lgraph[&T,&L] G)
@doc{bottom -- return the bottom nodes of a lgraph}
{
  return G.to - G.from;
}

public set[&T] reach(lgraph[&T,&L] G, set[&T] Start)
@doc{Reachability from start set}
{
	return reach(G<from,to>, Start);
}

public set[&T] reachR(lgraph[&T,&L] G, set[&T] Start, set[&T] Restr)
@doc{Reachability with restriction}
{
	return reachR(G<from,to>, Start, Restr);
}

public set[&T] reachX(lgraph[&T,&L] G, set[&T] Start, set[&T] Excl)
@doc{Reachability with exclusion}
{
   return reachX(G<from,to>, Start, Excl);
}

