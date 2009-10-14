module LabeledGraph

/*
 * Functions for manipulating labeled graphs:
 * - bottom
 * - predecessors
 * - reach
 * - reachR
 * - reachX
 * - successors
 * - top
 */

import Graph;
import Set;
import Relation;
import IO;

alias lgraph[&T,&L] = rel[&T from, &L label, &T to];

@doc{Return the bottom nodes of a lgraph}
public set[&T] bottom(lgraph[&T,&L] G)
{
  return G.to - G.from;
}

@doc{The predecessors of a single node in a lgraph}
public set[&T] predecessors(lgraph[&T,&L] G, &T From)
{
  return invert(G<from,to>)[From];
}

@doc{Reachability from start set}
public set[&T] reach(lgraph[&T,&L] G, set[&T] Start)
{
	return reach(G<from,to>, Start);
}

@doc{Reachability with restriction}
public set[&T] reachR(lgraph[&T,&L] G, set[&T] Start, set[&T] Restr)
{
	return reachR(G<from,to>, Start, Restr);
}

@doc{Reachability with exclusion}
public set[&T] reachX(lgraph[&T,&L] G, set[&T] Start, set[&T] Excl)
{
   return reachX(G<from,to>, Start, Excl);
}

@doc{The successor of a single node in a lgraph}
public set[&T] successors(lgraph[&T, &L] G, &T From)
{
  return G<from,to>[From];
}

@doc{Return the top nodes of a lgraph}
public set[&T] top(lgraph[&T,&L] G)
{
  return G.from - G.to;
}
