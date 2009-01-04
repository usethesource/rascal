module Dominators1

/*
 * Example from T. Lengauer and R.E. Tarjan, A Fast Algorithm for Finding Dominators
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

import Set;
import Relation;
import Graph;

str ROOT1 = "R";

rel[str,str] PRED1 ={
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

public rel[str, str] dominators(rel[str,str] PRED, str ROOT)
{
	set[str] VERTICES = Relation::carrier(PRED);

	return  { <V,  (VERTICES - {V, ROOT}) - Graph::reachX({ROOT}, {V}, PRED)> |  str V : VERTICES};
}


public bool testDominators1(){
	return dominators(PRED1, ROOT1) ==
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
}