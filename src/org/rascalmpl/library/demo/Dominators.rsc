@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::Dominators

import Set;
import Relation;
import  analysis::graphs::Graph;

/*
 *  A dominator tree describes the dominance relationship between nodes in a control flow graph
 *  (statements in a program).
 *
 *  Given nodes X and Y in a control flow graph, node X dominates node Y if and only if every 
 *  directed path from entry to Y (not including Y) contains X.
 *
 *  A dominator tree has the following characteristics:
 *
 *       - the initial node is the entry node
 *       - each node dominates only its descendents in the tree
 *
 * The function dominators computes the direct and indirect descendants that each node dominates.
 */


public rel[&T, set[&T]] dominators(rel[&T,&T] PRED, &T ROOT)
{
	set[&T] VERTICES = carrier(PRED);

	return  { <V,  (VERTICES - {V, ROOT}) - reachX(PRED, {ROOT}, {V})> |  &T V <- VERTICES};
}

/*
 * Example 1 from T. Lengauer and R.E. Tarjan, A Fast Algorithm for Finding Dominators
 * in a Flowgraph, TOPLAS, Vol. 1, N0. 1, July 1979, 121-141.
 *
 *    +------------------- R ----------+
 *    |         +----- / /             |
 *    |         |       /              |
 *    |         |       v              v
 *    |         |    +--B       +----- C -----+
 *    |         |    |  |       |             | 
 *    |         v    |  |       |             |       
 *    |         A <--+  |       |             |
 *    |         |       |       |             |
 *    |         v       v       v             v       
 *    |         D    +--E       F      +----- G
 *    |         |    |  ^       |      |      |
 *    |         v    |  |       |      |      |
 *    |         L    |  |       |      |      |
 *    |         |    v  |       |      v      v
 *    |         +->  H -+       +----> I <--- J
 *    |              +--------+      ^ |
 *    |                       |     /  |
 *    |                       v   /    |
 *    +---------------------> K / <----+
 */

public rel[str,set[str]] example1(){

	str ROOT = "R";

	rel[str,str] PRED ={
		<"R", "A">,<"R", "B">, <"R", "C">,
		<"A", "D">,
		<"B", "A">, <"B", "D">, <"B", "E">,
		<"C", "F">, <"C", "G">,
		<"D", "L">,
		<"E", "H">,
		<"F", "I">,
		<"G", "I">, <"G", "J">,
		<"H", "K">, <"H", "E">,
		<"I", "K">, 
		<"K", "I">, <"K", "R">,
		<"L", "H">
	};
	return dominators(PRED, ROOT);
}

public test bool t1() =
  example1() ==
 	{
		<"R", {"A", "B", "C", "D", "E", "F", "G", "L", "H", "I", "J", "K"}>, 
		<"A", {}>, 
		<"B", {}>, 
		<"C", {"F", "G", "J"}>, 
		<"D", {"L"}>, 
		<"E", {}>, 
		<"F", {}>, 
		<"G", {"J"}>, 
		<"L", {}>, 
		<"H", {}>, 
		<"I", {}>, 
		<"J", {}>, 
		<"K", {}>
	};
	

// Example 2 taken from Aho, Sethi Ullman, p603

public rel[int,set[int]] example2(){
	int ROOT = 1;

	rel[int,int] PRED = {
		<1,2>, <1,3>,
		<2,3>,
		<3,4>,
		<4,3>,<4,5>, <4,6>,
		<5,7>,
		<6,7>,
		<7,4>,<7,8>,
		<8,9>,<8,10>,<8,3>,
		<9,1>,
		<10,7>
	};
	return dominators(PRED, ROOT);
}

public test bool t2() =
  example2() ==
	{
		<1, {2, 3, 4, 5, 6, 7, 8, 9, 10}>, 
		<2, {}>,
		<3, {4, 5, 6, 7, 8, 9, 10}>,
		<4, {5, 6, 7, 8, 9, 10}>, 
		<5, {}>,
		<6, {}>,
		<7, {8, 9, 10}>,
		<8, {9, 10}>,
		<9, {}>,
		<10, {}>
	};

