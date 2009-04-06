module demo::Dashti 

import List;
import Set;
import Relation;
import Graph;
import Map;
import IO;
import Benchmark;
import Exception;

/* A security problem originating from work of Mohammed Dashti and
   suggested by Yaroslav Usenko.
   First compute a graph as follows:
   (a) The initial state S0 consists of a set of all permutations of the numbers [1 .. N].
   (b) From each state S there are transitions for each number I in [1 .. N] such that:
       - There is a new state S' that consists of all permutations in S that start
         with I but with that first I removed.
       - There is a transition from S to S' labeled with the number I.
   (c) The end state has an empty set of permutations and no outgoing transitions.
   
   Problem: what is the shortest path from S0 to the end state for varying N?
   
   Solution:
   (1) Generate the graph;
   (2) Compute shortest path from S0 to the end state.
   
   Note: this version optimizes speed and not readability.
*/

alias Permutation = list[int];                  // One permutation
alias StateId = int;                            // A unique state identifier
alias Symbol = int;                             // Symbols used for state transitions

int nStates = 0;                                // Global state counter

map[set[Permutation], StateId] allStates = (); // Associate a list of permutations with a state
//map[StateId, int] distance = ();               // The distance of each state from the start state

map[StateId, set[tuple[StateId, Symbol]]] Transitions = ();

// Solve problem of size N

int N = 0;

public void dashti(int n){
   N = n;
   nStates = 0;
   allStates = ({[]} : 0);                      // predefine the final state
   Transitions = (0:{});
//   distance[0] = 0;
//  expand(toSet(permutations([1 .. N])), 0);
	expand(toSet(permutations([1 .. N])));
}

Permutation transfer(Permutation perm, Symbol sym){
  if(perm != [] && perm[0] == sym)
     return tail(perm);
  else 
     return perm;
}

// Expand list of permutations

public StateId expand(set[Permutation] elms/*, int dist*/){
    StateId sid;
    try {
    	return allStates[elms];
    } catch NoSuchKey(value key): {
     	
    	nStates = nStates + 1;
    	allStates[elms] = nStates;
    	sid = nStates;
 
    };
 /*  
    contributions =
       {<expand(nextState, dist + 1), sym> | Symbol sym <- [1 .. N], 
       	                           set[Permutation] nextState := { transfer(perm, sym) | Permutation perm <- elms }
       };
       
    dist1 = dist + 1;
     
    Transitions[sid] = {<s, sym> | <StateId s, Symbol sym> <- contributions, s != sid, !distance[s]? || distance[s] <= dist1};
    
    distance[sid] = dist;
*/

  
    Transitions[sid] =
       {<expand(nextState), sym> | Symbol sym <- [1 .. N], 
       	                           set[Permutation] nextState := { transfer(perm, sym) | Permutation perm <- elms }
       };
    return sid;
}

void printStates(){
    map[StateId, set[Permutation]] invertedStates = (allStates[elms] : elms | set[Permutation] elms <- allStates);
 
	for(int I <- [0 .. nStates]){
	    elms = invertedStates[I];
 	    trans = Transitions[I];
	    println("S<I>:  <elms>;\n   <trans>");
	}
}

// Transform transition table into a graph

rel[StateId, StateId] buildGraph(){
    return { <from, to> | int from <- [ 1 .. nStates], <StateId to, Symbol sym> <- Transitions[from]};
}

public rel[StateId,StateId] getGraph(int n) {
  if (n <= 4) {
     N = n;
     return buildGraph();
  }
  
  return {};
}

public void test(int N){
  time1 = currentTimeMillis(); dashti(N); time2 = currentTimeMillis(); delta = (time2 - time1)/1000;
  
  if(N <= 3)
  	printStates();
  println("Number of States = <nStates>, Time=<delta> sec.");

  G = buildGraph();
  if(N <= 4)
 	 println("Graph = <G>");
  L = size(G);
  println("Edges: <L>");
  time1 = currentTimeMillis();
  P = shortestPathPair(G, 1, 0);          // 1 is always the start state, 0 the end state
  time2 = currentTimeMillis();
  delta = (time2 - time1)/1000;
  L = size(P);
  println("Length = <L>; Shortest path = <P>; Time=<delta> sec.");
}

/*
   Execution times on weepinbell.sen.cwi.nl:
   
   1: 2 states, Length = 2; Shortest path = [1,0], 
      Graph={<1,0>}
   2: 5 states, Length = 4; Shortest path = [1,4,5,0], 
      Graph = {<1,2>,<3,3>,<2,3>,<1,4>,<2,2>,<3,0>,<5,0>,<5,5>,<4,4>,<4,5>}
      Edges = 10
      State generation: 0.037 sec
      Shortest Path: 0 sec
   3: 43 states; Length = 8; Shortest path = [1,2,3,10,14,15,13,0]
      Graph= {<5,8>,<1,2>,<3,4>,<3,3>,<2,3>,<2,2>,<6,7>,<6,6>,<7,7>,<7,0>,<3,10>,
              <5,5>,<5,6>,<4,4>,<4,5>,<31,32>,<13,13>,<15,9>,<12,13>,<12,12>,
              <14,14>,<29,33>,<2,16>,<11,6>,<14,15>,<15,13>,<15,15>,<9,0>,<12,7>,<11,11>,
              <10,10>,<10,11>,<1,24>,<8,9>,<13,0>,<8,8>,<9,9>,<10,14>,<11,12>,<26,27>,<23,7>,
              <26,26>,<27,27>,<24,29>,<1,36>,<31,23>,<25,25>,<25,26>,<24,24>,<24,25>,<26,28>,
              <28,28>,<29,30>,<30,27>,<19,7>,<29,29>,<21,14>,<31,31>,<20,13>,<23,9>,<30,31>,
              <30,30>,<23,23>,<22,22>,<22,23>,<27,12>,<21,22>,<20,20>,<21,21>,<22,19>,<17,17>,
              <25,4>,<28,15>,<16,17>,<18,20>,<17,18>,<16,16>,<18,18>,<18,19>,<28,8>,<16,21>,
              <19,19>,<43,12>,<37,17>,<39,23>,<40,15>,<40,20>,<32,9>,<35,13>,<42,39>,<41,33>,
              <43,35>,<41,41>,<40,40>,<41,42>,<42,43>,<42,42>,<43,43>,<38,40>,<34,34>,<34,35>,
              <34,32>,<35,35>,<36,41>, <33,33>,<32,32>,<33,34>,<36,37>,<37,38>,<36,36>,<37,37>,
              <39,39>,<38,38>,<38,39>}
       Edges = 114
       State generation: 0.42 sec
       Shortest path: 0.004 sec
     4: 2697 States; Length = 13; Shortest path = [1,2,1197,1447,1465,1466,1467,1141,1142,365,123,15,0]
        Edges: 9712
        State generation: 11.7 sec
        Shortest path: 0.03 sec
     5: 
 */
