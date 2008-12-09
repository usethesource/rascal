module Graph

type rel[&T,&T] graph[&T];

public set[&T] top(graph[&T] G)
 @doc{Top of a Graph}
{
  return domain(G) - range(G);
}

public set[&T] bottom(graph[&T] G)
  @doc{Bottom of a Graph}
{
  return range(G) - domain(G);
}

public set[&T] reachR(set[&T] Start, set[&T] Restr,
                          graph[&T] G)
  @doc{Reachability with restriction}
{
  return range(compose(domainR(G, Start), 
                       carrierR(G, Restr)+));
}

public set[&T] reachX(set[&T] Start, set[&T] Excl, 
                          graph[&T] G)
  @doc{Reachability with exclusion}
{
  return range(compose(domainR(G, Start), 
                       carrierX(G, Excl)+));
}

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

%% --- Annotations -------------------------------------------
