@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl - CWI}
@contributor{Bas Basten - Bas.Basten@cwi.nl - CWI}
@doc{
#### Synopsis

A `Graph` datatype with associated functions.

#### Types

`alias Graph[&T] = rel[&T from, &T to];`

#### Description

The Graph data type is a binary relation and all operators and functions defined
on [relations]((Rascal:Values-Relation)) are also defined on Graphs.

The `Graph` library provides the following functions:
(((TOC)))
}
module analysis::graphs::Graph

import Set;
import List;
import Relation;
import util::Math;    
   
alias Graph[&T] = rel[&T from, &T to];

@doc{
#### Synopsis

Compute topological order of the nodes in a graph.

#### Examples

```rascal-shell
import  analysis::graphs::Graph;
order({<3,4>, <1,2>, <2,4>, <1,3>});
```
}
list[&T] order(Graph[&T] g){
    <components, topsort> = stronglyConnectedComponentsAndTopSort(g);
    return topsort;
}

@doc{
#### Synopsis

Compute strongly connected components in a graph.

#### Examples

```rascal-shell
import  analysis::graphs::Graph;
stronglyConnectedComponents({<1, 2>, <2, 3>, <3, 2>, <2, 4>, <4, 2>, <3, 5>, <5, 3>, <4, 5>, <5, 3>});
```
}
set[set[&T]] stronglyConnectedComponents(Graph[&T] g){
    <components, topsort> = stronglyConnectedComponentsAndTopSort(g);
    return components;
}

@synopsis{Compute the strongly connected components in a graph and return also the topologically sorted elements}
@description{
Tarjan's algorithm for computing strongly connected components in a graph
Returns :
* a set of strongly connected components (sets of vertices)
* the topological sort of vertices even for cyclic graphs)
* See <https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm>
}
tuple[set[set[&T]], list[&T]]  stronglyConnectedComponentsAndTopSort(Graph[&T] g){
    int index = 0;              // depth-first search node number counter
    map[&T, int] low = ();      // smallest index of any node known to be reachable from v
    map[&T, int] indexOf = ();  // maps nodes to their index
    set[&T] onStack = {};       // set of nodes on current stack
    list[&T] stack = [];        // node stack contains nodes of SCC under construction
    
    components = {};            // set of SCCs to be constructed
    topsort = [];
    
    void strongConnect(&T v){
        // Set the depth index for v to the smallest unused index
        indexOf[v] = index;
        low[v] = index;
        index += 1;
        stack = push(v, stack);
        onStack += {v};
        
        // Consider successors of v
        for(&T w <- successors(g, v)){
            if(!indexOf[w]?){
              // Successor w has not yet been visited; recurse on it
              strongConnect(w);
              low[v] = min(low[v], low[w]);
            } else if(w in onStack){
                // Successor w is in stack S and hence in the current SCC
                // If w is not on stack, then (v, w) is a cross-edge in the DFS tree and must be ignored
               low[v] = min(low[v], indexOf[w]);
            }
         }
         
        // If v is a root node, pop the stack and generate an SCC
        if(low[v] == indexOf[v]){
            // Start a new strongly connected component
            scc = {};
            &T w = v;
            do {
                <w, stack> = pop(stack);
                onStack -= {w};
                scc += {w};
                topsort = [w] + topsort;
            } while (w != v);
            components += {scc};
        }
    }
    
    for(v <- carrier(g)){
        if(!indexOf[v]?){
            strongConnect(v);
        }
    }
    
    return <components, topsort>;
}

@doc{
#### Synopsis

Determine the bottom nodes (leaves) of a graph.

#### Description

Returns the bottom nodes of Graph `G`, i.e., the leaf nodes that don't have any descendants.

#### Examples

```rascal-shell
import analysis::graphs::Graph;
bottom({<1,2>, <1,3>, <2,4>, <3,4>});
```
}
public set[&T] bottom(Graph[&T] G)
{
  return range(G) - domain(G);
}

@doc{
#### Synopsis

Determine the direct predecessors of a graph node.

#### Description

Returns the direct predecessors of node `From` in Graph `G`.

#### Examples

```rascal-shell
import analysis::graphs::Graph;
predecessors({<1,2>, <1,3>, <2,4>, <3,4>}, 4);
```
}
public set[&T] predecessors(Graph[&T] G, &T From)
{
  //return G[_,From];
  return invert(G)[From];
}

@doc{
#### Synopsis

Determine the graph nodes reachable from a set of nodes.

#### Description

Returns the set of nodes in Graph `G` that are reachable from any of the nodes
in the set `Start`.
}
public set[&T] reach(Graph[&T] G, set[&T] Start)
{
    set[&T] R = Start;
    set[&T] new = R;
	
	while (new != {}) {
		new = G[new] - R;
		R += new;
	}
    
	return R;
}

@doc{
#### Synopsis

Determine the graph nodes reachable from a set of nodes using a restricted set of intermediate nodes.

#### Description

Returns the set of nodes in Graph `G` that are reachable from any of the nodes
in set `Start` using path that only use nodes in the set `Restr`.

#### Examples

```rascal-shell
import analysis::graphs::Graph;
reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1, 2, 3});
```
}
public set[&T] reachR(Graph[&T] G, set[&T] Start, set[&T] Restr)
{
	return (carrierR(G, Restr)+)[Start];
}

@doc{
#### Synopsis

Determine the graph nodes reachable from a set of nodes excluding certain intermediate nodes.

#### Description

Returns set of nodes in Graph `G` that are reachable from any of the nodes
in `Start` via path that exclude nodes in `Excl`.

#### Examples

```rascal-shell
import analysis::graphs::Graph;
reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {2});
```
}
public set[&T] reachX(Graph[&T] G, set[&T] Start, set[&T] Excl)
{
   return (carrierX(G, Excl)+)[Start];
}

@doc{
#### Synopsis

Determine the shortest path between two graph nodes.

#### Description

Returns the shortest path between nodes `From` and `To` in Graph `G`.
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] shortestPathPair(Graph[&T] G, &T From, &T To);

@doc{
#### Synopsis

Determine the direct successors of a graph node.

#### Description

Returns the direct successors of node `From` in Graph `G`.

#### Examples

```rascal-shell
import analysis::graphs::Graph;
successors({<1,2>, <1,3>, <2,4>, <3,4>}, 1);
```
}
public set[&T] successors(Graph[&T] G, &T From)
{
  return G[From];
}

@doc{
#### Synopsis

Determine the set of top nodes (roots) of a graph.

#### Description

Returns the top nodes of Graph `G`, i.e., the root nodes that do not have any predecessors.

#### Examples

```rascal-shell
import analysis::graphs::Graph;
top({<1,2>, <1,3>, <2,4>, <3,4>});
```
}
public set[&T] top(Graph[&T] G)
{
  return domain(G) - range(G);
}

@doc{
#### Synopsis

Determine the connected components of a graph.

#### Description

Returns the [connected components](http://en.wikipedia.org/wiki/Connected_component_(graph_theory) of Graph `G`, as sets of nodes. All nodes within one component are all reachable from one another, there are no paths between two nodes from different components. The graph is assumed to be undirected.

#### Examples

```rascal-shell
import analysis::graphs::Graph;
connectedComponents({<1,2>, <1,3>, <4,5>, <5,6>});
```
}
public set[set[&T]] connectedComponents(Graph[&T] G)
{
  set[set[&T]] components = {};

  Graph[&T] undirected = G + invert(G);

  set[&T] todo = domain(undirected);

  while (size(todo) > 0) {
    component = reach(undirected, {getOneFrom(todo)});
    components += {component};
    todo -= component;
  };

  return components;
}

@synopsis{Transitive reduction of a directed graph}
@description{
The transitive reduction removes all "superfluous" edges in the sense
that all nodes remain reachable but all "shortcuts" have been removed.

The algorithm is inspired by the following paper, and uses the builtin (fast) transitive closure
algorithm from Rascal, and the composition operator `o` as an oracle to find out
which edges span more than one level in the graph. Note that the transitive
reduction's worst case complexity is in the same order as transitive closure itself anyway.

> Aho, A. V.; Garey, M. R.; Ullman, J. D. (1972), 
> "The transitive reduction of a directed graph", SIAM Journal on Computing, 1 (2): 131–137, doi:10.1137/0201008
}
@benefits{
* directed acyclic graphs are simplified (easier to draw clearly) without breaking node reachability
}
@pitfalls{
* reduces cyclic sub-graphs to "empty"
}
Graph[&T] transitiveReduction(Graph[&T] g) = g - (g o g+);