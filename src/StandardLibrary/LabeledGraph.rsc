module LabeledGraph

import Graph;
import Set;
import Relation;
import IO;

alias lgraph[&T,&L] = rel[&T from, &L label, &T to];

public set[&T] bottom(lgraph[&T,&L] G)
@doc{bottom -- return the bottom nodes of a lgraph}
{
  return G.to - G.from;
}

public set[&T] predecessors(lgraph[&T,&L] G, &T From)
@doc{predecessors -- the predecessors of a single node in a lgraph}
{
  //return G[_,From];
  return invert(G<from,to>)[From];
}

/*
public set[&T] predecessors(graph[&T] G, set[&T] From)
@doc{predecessors -- the predecessors of a set of nodes in a lgraph}
{
  //return G[_,From];
  return invert(G)[From];
}
*/

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

public set[&T] successors(lgraph[&T, &L] G, &T From)
@doc{successors -- the successor of a single node in a lgraph}
{
  return G<from,to>[From];
}

/*
public set[&T] successors(graph[&T] G, set[&T] From)
@doc{successors -- the successor of a set of nodes in a lgraph}
{
  return G[From];
}
*/

public set[&T] top(lgraph[&T,&L] G)
@doc{top -- return the top nodes of a lgraph}
{
  return G.from - G.to;
}
