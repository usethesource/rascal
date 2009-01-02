module Graph

/*
 * TODO:
 * - replace rel[&T,&T] by graph[&T]
*/

import Set;
import Relation;

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

type rel[&T,&T] graph[&T];

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

public set[&T] reachR(set[&T] Start, set[&T] Restr, rel[&T,&T] G)
@doc{Reachability with restriction}
{
	return (rangeR(G, Restr)+)[Start];
}

public set[&T] reachX(set[&T] Start, set[&T] Excl, rel[&T,&T] G)
@doc{Reachability with exclusion}
{
  return (rangeX(G, Excl)+)[Start];
}
/*
TO DO
public list[&T] shortestPathPair(&T From, &T To, graph[&T] G)
  @doc{Shortest path between pair of nodes}
  @primitive{"Graph.shortestPathPair"}

public set[list[&T]] shortestPathFrom(&T From, graph[&T] G)
  @doc{Shortest path between one node and all others}
  @primitive{"Graph.shortestPathFrom"}

public set[list[&T]] shortestPathAll(graph[&T] G)
  @doc{Shortest path between all nodes}
  @primitive{"Graph.shortestPathAll"}



*/
