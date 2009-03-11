module demo::Dashti 

import List;
import Relation;
import Graph;
import Map;
import IO;

/* A cryptographic problem originating from work of Mohammed Dashti and
   suggested by Yaroslav Usenko.
   First compute a graph as follows:
   (a) The initial state S0 consists of a list of all permutations of the numbers [1 .. N].
   (b) From each state S there are transitions for each number I in [1 .. N] such that:
       - There is a new state S' that consists of all permutations in S that start
         with I but with that first I removed.
       - There is a transition from S to S' labeled with the number I.
   (c) The end state has an empty list of permutations and no outgoing transitions.
   
   Problem: what is the shortest path from S0 to the end state for varying N?
   
   Solution:
   (1) Generate the graph;
   (2) Compute shortest path from S0 to the end state.
*/
/*
alias Permutation = list[int];                  // One permutation
alias StateID = int;                            // A unique state identifier
alias Symbol = int;                             // Symbols used for state transitions
*/

int nStates = 0;                                // Global state counter

map[list[list[int]], int] allStates = ();       // Associate a list of permutations with a state

rel[int from,int to,int symbol] Transitions = {};  // The transition table

// Solve problem of size N

public void dashti(int N){
   nStates = 0;
   allStates = ([[]] : 0);                      // predefine the final state
   Transitions = {};
   expand(permutations([1 .. N]));
}

// Create a new StateId for a list of permutations

int newState(list[list[int]] elms){
  if(allStates[elms]?)       
  	return allStates[elms];                    // Already defined? return it
  else {
  	nStates = nStates + 1;
  	allStates[elms] = nStates;
  	return nStates;  
  }
}

// Expand list of permutations

public int expand(list[list[int]] elms){
   
   if(elms == [[]])
   	  return 0;
   int sid = newState(elms);
   
   map[int, list[list[int]]] localTransitions = ();
   for(list[int] perm <- elms){
       list[list[int]] nextState = localTransitions[perm[0]] ?= [];
       nextState = nextState + [[tail(perm)]];
       localTransitions[perm[0]] = nextState;
   }
   
   rel[int,int,int] contrib = {};   // TODO: this local is needed due to bug in Rascal implementation;
   for(int key <- localTransitions){
     contrib = contrib + {<sid, expand(localTransitions[key]), key>};
   }
   Transitions = Transitions + contrib;
   return sid;
}

void printStates () {
  map[int, list[list[int]]] invertedStates = (allStates[key] : key | list[list[int]] key <- allStates);
 
  for(int I <- [0 .. nStates]){
     elms = invertedStates[I];
 	 trans = Transitions[I];
 	 strtrans = "";
 	 for(<int to, int sym> <- trans){
 	     strtrans = strtrans + " <sym> -> S<to> ";
 	 }
 	 println("S<I>: <elms>;\n    <strtrans>");
  }
}

public void test(int N){
  dashti(N);
  printStates();
  println("Number of States = <nStates>");
  G = Transitions<from,to>;               // restrict Transitions to first to columns
  println("Graph = <G>");
  P = shortestPathPair(G, 1, 0);          // 1 is always the start state, 0 the end state
  L = size(P);
  println("Length = <L>; Shortest path = <P>");
}
