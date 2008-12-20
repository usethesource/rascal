module Graph

/*
 * TODO:
 * - remove qualifiers
 * - replace rel[&T,&T] by graph[&T]
*/
import Set;
import Relation;

public set[&T] top(rel[&T,&T] G)
@doc{Top of a Graph}
{
  return Relation::domain(G) - Relation::range(G); // TODO: now needed to resolve names
}

public set[&T] bottom(rel[&T, &T] G)
@doc{Bottom of a Graph}
{
  return Relation::range(G) - Relation::domain(G);
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
	return (Relation::rangeR(G, Restr)+)[Start];
}

public set[&T] reachX(set[&T] Start, set[&T] Excl, rel[&T,&T] G)
@doc{Reachability with exclusion}
{
  return (Relation::rangeX(G, Excl)+)[Start];
}
/*
public list[&T] shortestPathPair(&T From, &T To, graph[&T] G)
  @doc{Shortest path between pair of nodes}
  @primitive{"Graph.shortestPathPair"}

public set[list[&T]] shortestPathFrom(&T From, graph[&T] G)
  @doc{Shortest path between one node and all others}
  @primitive{"Graph.shortestPathFrom"}

public set[list[&T]] shortestPathAll(graph[&T] G)
  @doc{Shortest path between all nodes}
  @primitive{"Graph.shortestPathAll"}

%% TO DO

public rel[&T, &T] closure(rel[&T, &T])
  @primitive{"Rel.closure"}
*/
