@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module CompareShortestPath

import  analysis::graphs::Graph;/* currently contains a Java version of the algorithm below */
import Relation;
import util::PriorityQueue;
import util::Benchmark;
import IO;
import List;
import util::Math;

private rel[int,int] Graph ={};
private map[int, int] distance =();
private map[int, int] pred = ();
private set[int] settled = {};
//private set[int] Q = {};
private PriorityQueue Q = priorityQueue();
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
    // Q = {From};
    Q = priorityQueue(0, From);
    
 //   while (Q != {}){
    while(!isEmpty(Q)){
       // u = extractMinimum();
       <d, u, Q> = extractMinimum(Q);
        if(u == To)
        	return extractPath(From, u);
        settled = settled + {u};
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
           //Q = Q + v;
           Q = insertElement(Q, distance[v], v);
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
  
private list[int] extractPath(int \start, int u)
{
    list[int] path = [u];
    while(pred[u] != \start){
          u = pred[u];
          path = [u] + path;
    }
    return [\start] + path;
}
  
public rel[int,int] Graph1 = {<5,8>,<1,2>,<3,4>,<3,3>,<2,3>,<2,2>,<6,7>,<6,6>,<7,7>,<7,0>,<3,10>,
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
              <39,39>,<38,38>,<38,39>};
             
               
public rel[int,int] randomGraph(int N, list[int] interval)
{
	return {<getOneFrom(interval), getOneFrom(interval)> | int n <- [1 .. N]};
}

public void measure1(rel[int,int] Graph1){

    G = Graph1;
	/* warm up for JVM */
	for(int i <- [1 .. 50])
		shortestPathPair(G, 1, 0);

    jtime = 0.0; jmin = 10000.0; jmax = 0.0;
    rtime = 0.0; rmin = 10000.0; rmax = 0.0;
    for(int i <- [1 .. 20]){
 		time1 = getMilliTime(); P1 = shortestPathPair(G, 1, 0); time2 = getMilliTime();
                                P2 = shortestPathPair1(G, 1, 0); time3 = getMilliTime();
                              
 		d1 = time2 - time1; jtime = jtime + d1; jmin = min(d1, jmin); jmax = max(d1, jmax);
 		d2 = time3 - time2; rtime = rtime + d2; rmin = min(d2, rmin); rmax = max(d2, rmax);
 		println("Java version:   <P1> in <d1> millis");
 		println("Rascal version: <P1> in <d2> millis");
 	}
 	println("Java average: <jtime/20> [<jmin> .. <jmax>]");
 	println("Rascal average: <rtime/20> [<rmin> .. <rmax>]");
 	
}

public void measure2(rel[int,int] Graph2)
{
   for(int i <- [1 .. 2000])
     shortestPathPair1(Graph2, 1, 0);
}

public void measure(){
	println("Graph1 -------"); measure1(Graph1);
	Graph2 = randomGraph(10000, [0 .. 50]);
	println("Graph2 -------"); measure1(Graph2);
}
