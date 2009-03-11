module Graph

/*
 * TODO:
 * - replace rel[&T,&T] by graph[&T]
*/

import Set;
import Relation;
import IO;

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

private rel[int,int] Graph ={};
private map[int, int] distance =();
private map[int, int] pred = ();
private set[int] settled = {};
private set[int] Q = {};
private int MAXDISTANCE = 10000;

public list[int] shortestPathPair(rel[int,int] G, int From, int To)
  @doc{Shortest path between pair of nodes}
  {
    Graph = G;
    for(int edge <- carrier(G)){
       distance[edge] = MAXDISTANCE;
    }
    distance[From] = 0;
    pred = ();;
    settled = {};
    Q = {From};
    
    while (Q != {}){
        u = extractMinimum();
        if(u == To)
        	return extractPath(From, u);
        settles = settled + u;
        relaxNeighbours(u);
    }  
    return [];
  }
  
  private void relaxNeighbours(int u){
  
    for(int v <- Graph[u], v notin settled){
        if(distance[v] > distance[u] + 1){
           distance[v] = distance[u] + 1;
           pred[v] = u;
           Q = Q + v;
        }
     }
  }
  
  private int extractMinimum(){
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
  
  private list[int] extractPath(int start, int u){
    list[int] path = [u];
    while(pred[u] != start){
          u = pred[u];
          path = u + path;
    }
    return start + path;
  }
  
public rel[int,int] examp = {<1,2>,<2,3>,<3,4>,<2,4>};

/* TODO
public set[list[&T]] shortestPathFrom(graph[&T] G, &T From)
  @doc{Shortest path between one node and all others}
  @primitive{"Graph.shortestPathFrom"}

public set[list[&T]] shortestPathAll(graph[&T] G)
  @doc{Shortest path between all nodes}
  @primitive{"Graph.shortestPathAll"}



*/
