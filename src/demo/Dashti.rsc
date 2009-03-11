module demo::Dashti 

import List;
import Graph;
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
alias State = list[list[int]];
alias Trans = map[int, State];
*/

data State = state(list[list[int]] elms, map[int, int] trans);

int nStates = 0;

map[list[list[int]], int] allStates = ([[]] : 0);

map[int, State] ordStates = (0: state([[]], ()));

public void dashti(int N){
   nStates = 0;
   allStates = ([[]] : 0);
   ordStates = (0: state([[]], ()));
   expand(permutations([1 .. N]));
}

int newState(list[list[int]] elms, map[int,int] trans){
  if(allStates[elms]?)
  	return allStates[elms];
  else {
  	nStates = nStates + 1;
  	allStates[elms] = nStates;
  	ordStates[nStates] = state(elms, trans);
  	return nStates;  
  }
}
   
public int expand(list[list[int]] elms){
   
   if(elms == [[]])
   	  return 0;
   map[int, list[list[int]]] trans = ();
   for(list[int] perm <- elms){
       list[list[int]] nextState = trans[perm[0]] ?= [];
       nextState = nextState + [[tail(perm)]];
       trans[perm[0]] = nextState;
   }
   map[int, int] strans = ();
   for(int key <- trans){
       strans[key] = expand(trans[key]);
   }
   return newState(elms, strans);
}

void printStates () {

  for(int id <- ordStates) {
      s = ordStates[id];
      elms = s.elms;
  
  	  trans = s.trans;
 	  trs = "";
  	  sep = "";
  	  for(int key <- trans){
    	  toState = trans[key];
   	      trs = trs + sep + "<key> -> S<toState>";
   	      sep = "; ";
 	  }
 	  println("S<id>: <elms>;\n     <trs>");
  }
}

public rel[int,int] extractGraph(){
    rel[int,int] Graph = {};
	for(int id <- ordStates) {
	    s = ordStates[id];
	    trans = s.trans;
	    rel[int,int] contrib = {<id, trans[key]> | int key <- trans};
	    Graph = Graph + contrib;
	 }
	 return Graph;
}

public void test(int N){
  dashti(N);
  printStates();
  println("Number of States = <nStates>");
  G = extractGraph();
  println("Graph = <G>");
  P = shortestPathPair(G, nStates, 0);
  L = size(P);
  println("Length = <L>; Shortest path = <P>");
}
