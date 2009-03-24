module Graph

import Set;
import Relation;
import IO;

alias graph[&T] = rel[&T from, &T to];

public set[&T] top(graph[&T] G)
@doc{top -- return the top nodes of a graph}
{
  return domain(G) - range(G);
}

public set[&T] bottom(graph[&T] G)
@doc{bottom -- return the bottom nodes of a graph}
{
  return range(G) - domain(G);
}

public set[&T] reach(graph[&T] G, set[&T] Start)
@doc{Reachability from start set}
{
	with
     	set[&T] R = Start;
	solve
		R = R + G[R];
	return R;
}

public set[&T] reachR(graph[&T] G, set[&T] Start, set[&T] Restr)
@doc{Reachability with restriction}
{
	return (carrierR(G, Restr)+)[Start];
}

public set[&T] reachX(graph[&T] G, set[&T] Start, set[&T] Excl)
@doc{Reachability with exclusion}
{
   return (carrierX(G, Excl)+)[Start];
}

// Shortest Path functions

public list[&T] java shortestPathPair(graph[&T] G, &T From, &T To)
@javaClass{org.meta_environment.rascal.std.Graph};

/*

private graph[&T] Graph ={};
private map[int, int] distance =();
private map[int, int] pred = ();
private set[int] settled = {};
private set[int] Q = {};
private int MAXDISTANCE = 10000;

public list[int] shortestPathPair1(rel[int,int] G, int From, int To)
@doc{Shortest path between pair of nodes}
{
    Graph = G;
    for(int edge <- carrier(G)){
       distance[edge] = MAXDISTANCE;
    }
    distance[From] = 0;
    pred = ();
    settled = {};
    Q = {From};
    
    while (Q != {}){
        u = extractMinimum();
        if(u == To)
        	return extractPath(From, u);
        settled = settled + u;
        relaxNeighbours(u);
    }  
    return [];
}
  
private void relaxNeighbours(int u)
{  
    for(int v <- Graph[u], v notin settled){
        if(distance[v] > distance[u] + 1){  // 1 is default weight of each edge
           distance[v] = distance[u] + 1;
           pred[v] = u;
           Q = Q + v;
        }
     }
  }
  
private int extractMinimum()
{
     minVal = MAXDISTANCE;
     int min = -1;
     for(int q <- Q){
     	 d = distance[q];
     
        if(distance[q] <= minVal){
           minVal = distance[q];
           min = q;
        }
     }
     Q = Q - min;
     return min;
}
  
private list[int] extractPath(int start, int u)
{
    list[int] path = [u];
    while(pred[u] != start){
          u = pred[u];
          path = u + path;
    }
    return start + path;
}
  
public rel[int,int] examp = {<1,2>,<2,3>,<3,4>,<2,4>};
*/

/* TODO
public set[list[&T]] shortestPathFrom(graph[&T] G, &T From)
  @doc{Shortest path between one node and all others}
  @primitive{"Graph.shortestPathFrom"}

public set[list[&T]] shortestPathAll(graph[&T] G)
  @doc{Shortest path between all nodes}
  @primitive{"Graph.shortestPathAll"}

*/

/* TODO:
public set[&T] successors(graph[&T] G, &T From)
@doc{successors -- the successor of a single node in a graph}
{
  return G[From];
}

public set[&T] successors(graph[&T] G, set[&T] From)
@doc{successors -- the successor of a set of nodes in a graph}
{
  return G[From];
}

/* TODO:
public set[&T] predecessors(graph[&T] G, &T From)
@doc{predecessors -- the predecessors of a single node in a graph}
{
  return G[_,From];
}

public set[&T] predecessors(graph[&T] G, &T From)
@doc{predecessors -- the predecessors of a set of nodes in a graph}
{
  return G[_,From];
}
*/
