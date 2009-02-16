module Graph

/*
 * TODO:
 * - replace rel[&T,&T] by graph[&T]
*/

import Set;
import Relation;

alias graph[&T] = rel[&T,&T];

public set[&T] top(rel[&T,&T] G)
@doc{top -- return the top nodes of a graph}
{
  return domain(G) - range(G);
}

public set[&T] bottom(rel[&T, &T] G)
@doc{bottom -- return the bottom nodes of a graph}
{
  return range(G) - domain(G);
}

alias graph[&T] = rel[&T,&T];

public set[&T] gtop(graph[&T] G)
@doc{Top of a Graph}
{
  return domain(G) - range(G);
}

public set[&T] gbottom(graph[&T] G)
  @doc{Bottom of a Graph}
{
  return range(G) - domain(G);
}

public set[&T] reach(rel[&T,&T] G, set[&T] Start)
@doc{Reachability from start set}
{
	with
     	set[&T] R = Start;
	solve
		R = R + G[R];
	return R;
}

public set[&T] reachR(rel[&T,&T] G, set[&T] Start, set[&T] Restr)
@doc{Reachability with restriction}
{
	return (carrierR(G, Restr)+)[Start];
}

public set[&T] reachX(rel[&T,&T] G, set[&T] Start, set[&T] Excl)
@doc{Reachability with exclusion}
{
   return (carrierX(G, Excl)+)[Start];
}
/*
TO DO
public list[&T] shortestPathPair(graph[&T] G, &T From, &T To)
  @doc{Shortest path between pair of nodes}
  @primitive{"Graph.shortestPathPair"}

public set[list[&T]] shortestPathFrom(graph[&T] G, &T From)
  @doc{Shortest path between one node and all others}
  @primitive{"Graph.shortestPathFrom"}

public set[list[&T]] shortestPathAll(graph[&T] G)
  @doc{Shortest path between all nodes}
  @primitive{"Graph.shortestPathAll"}



*/
