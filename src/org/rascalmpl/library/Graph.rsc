module Graph

import Set;
import Relation;
import IO;      
   
alias Graph[&T] = rel[&T from, &T to];

@doc{ return the bottom nodes of a Graph.}
public set[&T] bottom(Graph[&T] G)
{
  return range(G) - domain(G);
}

@doc{ the predecessors of a single node in a Graph}
public set[&T] predecessors(Graph[&T] G, &T From)
{
  //return G[_,From];
  return invert(G)[From];
}

@doc{ Reachability from set of start nodes.}
public set[&T] reach(Graph[&T] G, set[&T] Start)
{
    set[&T] R = Start;
	
	solve (R) {
		R = R + G[R];
    }
    
	return R;
}

@doc{ Reachability from set of start nodes with restriction to certain nodes.}
public set[&T] reachR(Graph[&T] G, set[&T] Start, set[&T] Restr)
{
	return (carrierR(G, Restr)+)[Start];
}

@doc{ Reachability from set of start nodes with exclusion of certain nodes.}
public set[&T] reachX(Graph[&T] G, set[&T] Start, set[&T] Excl)
{
   return (carrierX(G, Excl)+)[Start];
}

@doc{ Shortest path between pair of nodes.}
@javaClass{org.rascalmpl.library.Graph}
public list[&T] java shortestPathPair(Graph[&T] G, &T From, &T To);

/* TODO: replace upper builtin by lower code, but refactor it to not
   use global variables.
private Graph[&T] Graph ={};
private map[int, int] distance =();
private map[int, int] pred = ();
private set[int] settled = {};
private set[int] Q = {};
private int MAXDISTANCE = 10000;

@doc{Shortest path between pair of nodes}
public list[int] shortestPathPair1(rel[int,int] G, int From, int To)
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

@doc{The successor of a single node in a Graph}
public set[&T] successors(Graph[&T] G, &T From)
{
  return G[From];
}

@doc{Return the top nodes of a Graph.}
public set[&T] top(Graph[&T] G)
{
  return domain(G) - range(G);
}
