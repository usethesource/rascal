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

// Backtracking for generators with regular expressions
public test bool test2() {
	str s = "abc";
	
	// Comprehensions
	res0 = [ <S0,S> | /@<S0:[a-z]+>@/ <- ["@abc@", "@def@"], /@<S:[a-z]+>@/ <- ["@abc@", "@def@"] ];
	res1 = [ <S0,S> | [/@<S0:[a-z]+>@/] <- [["@abc@"], ["@def@"]], /@<S:[a-z]+>@/ <- ["@abc@", "@def@"] ];
	res2 = [ <S0,S> | /@<S0:<s>>@/ <- ["@abc@", "@def@"], /@<S:[a-z]+>@/ <- ["@abc@", "@def@"] ];
	
	// For-loops
	res3 = for(/@<S0:[a-z]+>@/ <- ["@abc@", "@def@"], /@<S:[a-z]+>@/ <- ["@abc@", "@def@"]) append <S0,S>;
	res4 = for([/@<S0:[a-z]+>@/] <- [["@abc@"], ["@def@"]], /@<S:[a-z]+>@/ <- ["@abc@", "@def@"]) append <S0,S>;
	res5 = for(/@<S0:<s>>@/ <- ["@abc@", "@def@"], /@<S:[a-z]+>@/ <- ["@abc@", "@def@"]) append <S0,S>;
	
	return res0 == [<"abc","abc">,<"abc","def">,<"def","abc">,<"def","def">]
			&& res1 == [<"abc","abc">,<"abc","def">,<"def","abc">,<"def","def">]
			&& res2 == [<"abc","abc">,<"abc","def">]
			&& res0 == res3
			&& res1 == res4
			&& res2 == res5;
}
