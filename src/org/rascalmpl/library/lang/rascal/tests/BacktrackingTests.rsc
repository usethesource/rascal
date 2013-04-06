module lang::rascal::tests::BacktrackingTests

import List;
import IO;


// Backtracking for generators with list and set patterns
public test bool test1() {
	// Comprehensions
	l0 = [ <s,r> | [s,r] <- [ [[1],[2]],[[3],[4]] ] ];
	l1 = [ <s,r> | [*s,*r] <- [ [1,2],[3,4] ] ];
	l2 = [ <s,r> | [*int s, *int r] <- [ [1,2], [3,4] ]];
	l3 = [ <s,r> | {*int s, *int r} <- [ {1,2}, {3,4} ] ];
	// For-loops
	l4 = for([s,r] <- [ [[1],[2]],[[3],[4]] ]) append <s,r>;
	l5 = for([*s,*r] <- [ [1,2],[3,4] ]) append <s,r>;
	l6 = for([*int s, *int r] <- [ [1,2], [3,4] ]) append <s,r>;
	l7 = for({*int s, *int r} <- [ {1,2}, {3,4} ]) append <s,r>;
	
	return l0 == [<[1],[2]>,<[3],[4]>]
			&& l1 == [ <[],[1,2]>,<[1],[2]>,<[1,2],[]>,<[],[3,4]>,<[3],[4]>,<[3,4],[]> ]
			&& l3 == [ <{1,2},{}>,<{2},{1}>,<{1},{2}>,<{},{1,2}>,<{3,4},{}>,<{4},{3}>,<{3},{4}>,<{},{3,4}> ]
			&& l0 == l4
			&& l1 == l5
			&& l1 == l2
			&& l2 == l6
			&& l3 == l7;	
}
