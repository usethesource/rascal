 module lang::rascal::tests::library::analysis::graphs::GraphTests
  /*******************************************************************************
   * Copyright (c) 2009-2015 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
   *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
  *******************************************************************************/ 
 
import analysis::graphs::Graph;
import List;
import Set;
import Relation;

/*
              1 -----> 3
              |        |
              v        |
              2        |
              |        |
              v        |
              4 <------+

*/
public Graph[int] G1 = {<3,4>, <1,2>, <2,4>, <1,3>};

/*
               1
               |
               v
               2 <----> 3
               ^        ^
               |        |
               v        v
               4 <----> 5

*/
public Graph[int] G2 = {<1, 2>, <2, 3>, <3, 2>, <2, 4>, <4, 2>, <3, 5>, <5, 3>, <4, 5>, <5, 3>};

/*
               1------->6------>7<---->8
               | 
               v
               2 <----> 3
               ^        ^
               |        |
               v        v
               4 <----> 5

*/
public Graph[int] G3 = {<1, 2>, <2, 3>, <3, 2>, <2, 4>, <4, 2>, <3, 5>, <5, 3>, <4, 5>, <5, 3>, <1,6>, <6,7>, <7,8>,<8,7>};

/*
          2 -----> 3 ------+
          ^        |       |
          |        v       v
          1 -----> 4 ----> 5
                   |
                   V
                   6
*/
 public Graph[int] G4 = { <1, 2>, <1,4>, <2,3>, <3, 4>, <3, 5>, <4, 5>, <4, 6> };
 
 /*
 
        5    +---------- 7       +------- 3
        |  /             |       |        |
        v v              v       |        |
        11 ------+       8 <-----+        |
        |  \     |       |                |
        v   \    |       v                v
        2   |    +---->  9               10
            |                             ^
            +-----------------------------+
 */
 public Graph[int] G5 ={ <5, 11>, <11, 2>, <11, 9>, <11, 10>, <7, 11>, <7, 8>, <8, 9>, <3, 8>, <3, 10> };
 
 /*
                +-------+       +-----+
                |       |       |     |
                v       |       v     |
        0 ----> 1 ----> 2 ----> 3 ----+
*/
public Graph[int] G6 = { <0, 1>, <1,2>, <2,1>, <2,3>, <3, 3> };

/*
    +---------------+
    |               |
    v               |
    1 ----> 0 ----> 2
            |
            v
            3 ----> 4

*/
public Graph[int] G7 = { <1, 0>, <0, 2>, <2, 1>, <0, 3>, <3, 4> };

/*
    0 ----> 1 ----> 2 ----> 3

*/
public Graph[int] G8 = { <0, 1>, <1, 2>, <2,3> };

/*
    +---------------+
    |               |
    v               |
    0 ----> 1 ----> 2
            |
            |
    3 <-----+ ----> 6
    |       |
    v       v
    5 <---- 4

*/
public Graph[int] G9 = { <0, 1>, <1,2>, <2,0>, <1,3>, <1,4>, <1,6>, <3,5>, <4,5>};

/*
    +---------------+               9 <------+-----+
    |               |               ^        |     |
    |               v               |        |     |
    |       +------ 2 ----> 6 <-----+------> 7     |
    |       |       ^       |       |              |
    |       v       |       v       |              |
    3 <---- 0 ----> 1 ----> 4 ----> 5 -----> 8 <---+
                                    
*/
public Graph[int] G10 = {<0,1>, <0,3>, <1,2>, <1,4>, <2,0>, <2,6>, <3,2>, <4,5>, <4,6>, <5,6>, <5,7>, <5,8>, <5,9>, <6,4>, <7,9>, <8,9>, <9,8> };

/*
    +-----------------------+
    |                       |
    v                       |            
    0 ----> 1 ----> 2 ----> 3
                    ^
                    |
                    v
                    4
*/
public Graph[int] G11 = { <0,1>, <1,2>, <2,3>, <2,4>, <3,0>, <4,2> };

// bottom
  
test bool bottom1() = bottom({}) == {};
test bool bottom2() = bottom({<1,2>, <1,3>, <2,4>, <3,4>}) == {4};
test bool bottom3() = bottom(G1) == {4};
test bool bottom4() = bottom(G2) == {};
test bool bottom5() = bottom(G3) == {};
test bool bottom6() = bottom(G4) == {5,6};
test bool bottom7() = bottom(G5) == {2,9,10};
test bool bottom8() = bottom(G6) == {};
test bool bottom9() = bottom(G7) == {4};
test bool bottom10() = bottom(G8) == {3};
test bool bottom11() = bottom(G9) == {5,6};
test bool bottom12() = bottom(G10) == {};

// TODO: connectedComponents

// stronglyConnectedComponents

test bool scc0() = stronglyConnectedComponents({}) == {};
test bool scc1() = stronglyConnectedComponents(G1) == {{1}, {2}, {3}, {4}};
test bool scc2() = stronglyConnectedComponents(G2) == {{1}, {2,3,4,5}};
test bool scc3() = stronglyConnectedComponents(G3) == {{1}, {2,3,4,5}, {6}, {7,8}};
test bool scc4() = stronglyConnectedComponents(G4) == {{1}, {2}, {3}, {4}, {5}, {6}};
test bool scc5() = stronglyConnectedComponents(G5) == {{2}, {3}, {5}, {7}, {8}, {9}, {10}, {11}};
test bool scc6() = stronglyConnectedComponents(G6) == {{0}, {1,2}, {3}};
test bool scc7() = stronglyConnectedComponents(G7) == {{4}, {3}, {1, 2, 0}};
test bool scc8() = stronglyConnectedComponents(G8) == {{0}, {1}, {2}, {3}};
test bool scc9() = stronglyConnectedComponents(G9) == {{3}, {4}, {5}, {6}, {0, 1, 2}};
test bool scc10() = stronglyConnectedComponents(G10) == {{0,1,2,3}, {4,5,6}, {7}, {8,9}};
test bool scc11() = stronglyConnectedComponents(G11) == {{0, 1, 2, 3, 4}};

test bool sccPreservesElements(Graph[int] G) {
    components = stronglyConnectedComponents(G);
    return {*comp | comp <- components} == carrier(G);
}

test bool sccDisjointComponents(Graph[int] G) {
    components = stronglyConnectedComponents(G);
    return isEmpty(G) || size(components) <= 1 || all(set[int] comp1 <- components, set[int] comp2 <- components, ((comp1 == comp2) ? true : isEmpty(comp1 & comp2)));
}

// stronglyConnectedComponentsAndTopSort

test bool sccNoDuplicatesInOrder(Graph[int] G){
    <components, ordered> = stronglyConnectedComponentsAndTopSort(G);
    return size(ordered) == size(toSet(ordered));
}

test bool sccOrderEqualsComponents(Graph[int] G){
    <components, ordered> = stronglyConnectedComponentsAndTopSort(G);
    return {*comp | comp <- components} == toSet(ordered);
}

// order

test bool order1() = order(G1) == [1,2,3,4];
test bool order2() = order(G2)[0] == 1;
test bool order3() = order(G3)[0] == 1 &&  order(G3)[1] == 6;
test bool order4() = order(G4) == [1,2,3,4,6,5];
test bool order5() = order(G5) == [5,3,7,11,2,8,9,10];
test bool order6() = order(G6) == [0,1,2,3];
test bool order7() = order(G7) ==  [1,0,2,3,4];
test bool order8() = order(G8) == [0,1,2,3];
test bool order9() = order(G9) ==   [1,2,0,6,4,3,5];
test bool order10() = order(G10) ==  [1,2,0,3,6,4,5,7,9,8];
test bool order11() = order(G11) == [1,2,3,0,4];

// predecessors
  
test bool predecessors1()=  predecessors({<1,2>, <1,3>, <2,4>, <3,4>}, 4) =={2, 3};
test bool predecessors2() = predecessors(G1, 2) == {1};
test bool predecessors3() = predecessors(G1, 4) == {2,3};
test bool predecessors4() = predecessors(G2, 2) == {1,3,4};
test bool predecessors5() = predecessors(G2, 5) == {3,4};
test bool predecessors6() = predecessors(G3, 8) == {7};
test bool predecessors7() = predecessors(G3, 5) == {3,4};
  
// reachR()
  
test bool reachR1() = reachR({}, {}, {}) == {};
test bool reachR2() = reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {}) =={};
test bool reachR3() = reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2}) =={2};
test bool reachR4() = reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2,3}) =={2,3};
test bool reachR5() = reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2,4}) =={2, 4};
  
// reachX
  
test bool reachX1() = reachX({}, {}, {}) == {};
test bool reachX2() = reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {}) =={2, 3, 4};
test bool reachX3() = reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {2}) =={3, 4};
test bool reachX4() = reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {2,3}) =={};
test bool reachX5() = reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {4}) =={2, 3};
  	
// reach
  
test bool reach0() = reach({}, {}) == {};
test bool reach1() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {1}) =={1,2, 3, 4};
test bool reach2() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {2}) =={2, 4};
test bool reach3() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {3}) =={3, 4};
test bool reach4() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {4}) =={4};
test bool reach5() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {3,4}) =={3,4};
test bool reach6() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {2,3}) =={2, 3,4};
  	
// TODO: shortestPathPair
  	
// successors
  
test bool successors1() = successors({<1,2>, <1,3>, <2,4>, <3,4>}, 1) =={2, 3};
test bool successors2() = successors(G1, 3) == {4};
test bool successors3() = successors(G1, 1) == {2,3};
test bool successors4() = successors(G2, 1) == {2};
test bool successors5() = successors(G3, 2) == {3,4};

// top
  
test bool top1() = top({}) == {};
test bool top2() = top({<1,2>, <1,3>, <2,4>, <3,4>}) == {1};
test bool top3() = top(G1) == {1};
test bool top4() = top(G2) == {1};
test bool top5() = top(G3) == {1};
test bool top6() = top(G4) == {1};
test bool top7() = top(G5) == {5,7,3};
test bool top8() = top(G6) == {0};
test bool top9() = top(G7) == {};
test bool top10() = top(G8) == {0};
test bool top11() = top(G9) == {};
test bool top12() = top(G10) == {};
