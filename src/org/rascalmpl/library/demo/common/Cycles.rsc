@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::common::Cycles

import Set;
import Relation;

public rel[int, set[int]] cycles(rel[int,int] Graph) {
	rel[int,int] Closure = Graph+;
  	return { <N, Closure[N]> | int N <- carrier(Graph), <N, N> in Closure};
}

public bool isProperCycle(rel[int,int] Graph, int N, set[int] C){
 	rel[int,int] RC  = carrierR(Graph, C)+;
    return all(int M <- C, <N, M> in RC && <M, N> in RC);
}

public set[set[int]] subCycles (rel[int,int] Graph, int N, set[int] Cycle){
	return { B | set[int] B <- power1(Cycle), N in B, isProperCycle(Graph, N, B) };
}

public rel[int, set[set[int]]] allSubCycles(rel[int,int] Graph, rel[int, set[int]] Cycles) {
	return { <N, subCycles(Graph, N, B)> | <int N, set[int] B> <- Cycles};
}

// Tests

//         1-------+   +---->4
//         ^       |  /      |
//         |       v /       v
//         3<----- 2<--------5
//                 |
//                 v
//                 6<--+
//                 |   |
//                 v   |
//                 7---+   


private	rel[int, int] Graph = {<1,2>,<2,3>,<3,1>,<2,4>,<4,5>,<5,2>,<2,6>,<6,7>,<7,6>};


public test bool t1() =
  Graph+ ==
		{<1, 1>, <1, 2>, <1, 3>, <1, 4>, <1, 5>, <1, 6>, <1, 7>,
  	 	 <2, 1>, <2, 2>, <2, 3>, <2, 4>, <2, 5>, <2, 6>, <2, 7>,
  	 	 <3, 1>, <3, 2>, <3, 3>, <3, 4>, <3, 5>, <3, 6>, <3, 7>,
  	 	 <4, 1>, <4, 2>, <4, 3>, <4, 4>, <4, 5>, <4, 6>, <4, 7>,
  	 	 <5, 1>, <5, 2>, <5, 3>, <5, 4>, <5, 5>, <5, 6>, <5, 7>,
  	 	 <6, 6>, <6, 7>,
  		 <7, 6>, <7, 7>};
  		
private rel[int, set[int]] Cycles = cycles(Graph);

public test bool t2() =
  Cycles ==
		{< 1, {1, 2, 3, 4, 5, 6, 7}>,
  		 < 2, {1, 2, 3, 4, 5, 6, 7}>,
  		 < 3,  {1, 2, 3, 4, 5, 6, 7}>,
  		 < 4,  {1, 2, 3, 4, 5, 6, 7}>,
  		 < 5,  {1, 2, 3, 4, 5, 6, 7}>,
  		 < 6,  {6, 7}>,
  		 < 7,  {6, 7}> };

public test bool t3() =
  allSubCycles(Graph, Cycles) ==
		{< 1, { {1, 2, 3, 4, 5}, {1, 2, 3}} >,
  		 < 2, { {1, 2, 3, 4, 5}, {1, 2, 3}, {2, 4, 5}} >,
  		 < 3, { {1, 2, 3, 4, 5}, {1, 2, 3}} >,
  		 < 4, { {1, 2, 3, 4, 5}, {2, 4, 5}} >,
  		 < 5, { {1, 2, 3, 4, 5}, {2, 4, 5}} >,
  		 < 6, { {6, 7} } >,
  		 < 7, { {6, 7} } >};
