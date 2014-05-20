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
			&& {*l3} == {*[ <{1,2},{}>,<{2},{1}>,<{1},{2}>,<{},{1,2}>,<{3,4},{}>,<{4},{3}>,<{3},{4}>,<{},{3,4}> ]}
			&& l0 == l4
			&& l1 == l5
			&& l1 == l2
			&& l2 == l6
			&& {*l3} == {*l7};	
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

public test bool test3() {

	// For-loops
	li0 = for(int i <- [1,2,3]) append int () { return i; };
	res0 = for(f <- li0) append f();
	
	li1 = for(i <- [1,2,3]) append int () { return i; };
	res1 = for(f <- li1) append f();
	
	// Comprehensions
	li2 = [ int () { return i; } | int i <- [1, 2, 3] ];
	res2 = for(f <- li0) append f();
	
	li3 = [ int () { return i; } | i <- [1, 2, 3] ];
	res3 = for(f <- li1) append f();
	
	return res0 == [3,3,3] && res1 == [3,3,3]
			&& res2 == [3,3,3] && res3 == [3,3,3];	
}

public test bool test4() {

	// For-loops
	li0 = for(int i <- [1,2,3], str j <- ["4","5"]) append tuple[int,str] () { return <i,j>; };
	res0 = for(f <- li0) append f();
	
	li1 = for(i <- [1,2,3], j <- ["4","5"]) append tuple[int,str] () { return <i,j>; };
	res1 = for(f <- li1) append f();
	
	// Comprehensions
	li2 = [ tuple[int,str] () { return <i,j>; } | int i <- [1,2,3], str j <- ["4","5"] ];
	res2 = for(f <- li0) append f();
	
	li3 = [ tuple[int,str] () { return <i,j>; } | i <- [1,2,3], j <- ["4","5"] ];
	res3 = for(f <- li1) append f();
	
	return {*res0} == {<3,"5">} && size(res0) == 6
			&& {*res1} == {*res0} && size(res1) == 6
			&& {*res2} == {*res0} && size(res2) == 6 
			&& {*res3} == {*res0};	
	
}

public test bool test5() {

	// For-loops
	li0 = for(int i <- [1,2], str j <- ["3","4"], value k <- [i,j]) append tuple[int,str,value] () { return <i,j,k>; };
	res0 = for(f <- li0) append f();
	
	li1 = for(i <- [1,2], j <- ["3","4"], k <- [i,j]) append tuple[int,str,value] () { return <i,j,k>; };
	res1 = for(f <- li1) append f();	

	// Comprehensions
	li2 = [ tuple[int,str,value] () { return <i,j,k>; } | int i <- [1,2], str j <- ["3","4"], value k <- [i,j] ];
	res2 = for(f <- li0) append f();
	
	li3 = [ tuple[int,str,value] () { return <i,j,k>; } | i <- [1,2], j <- ["3","4"], k <- [i,j] ];
	res3 = for(f <- li1) append f();
	
	return {*res0} == {<2,"4","3">,<2,"4","4">} && size(res0) == 8 
			&& {*res1} == {*res0} && size(res1) == 8
			&& {*res2} == {*res0} && size(res2) == 8 
			&& {*res3} == {*res0} && size(res3) == 8;	
}

public test bool test6() {

	// For-loops
	li0 = for(int i <- [1,2], str j <- ["3","4"], [*value k] <- [[i],[j]]) append tuple[int,str,value] () { return <i,j,k>; };
	res0 = for(f <- li0) append f();
	
	li1 = for(i <- [1,2], j <- ["3","4"], [*k] <- [[i],[j]]) append tuple[int,str,value] () { return <i,j,k>; };
	res1 = for(f <- li1) append f();	

	// Comprehensions
	li2 = [ tuple[int,str,value] () { return <i,j,k>; } | int i <- [1,2], str j <- ["3","4"], [*value k] <- [[i],[j]] ];
	res2 = for(f <- li0) append f();
	
	li3 = [ tuple[int,str,value] () { return <i,j,k>; } | i <- [1,2], j <- ["3","4"], [*k] <- [[i],[j]] ];
	res3 = for(f <- li1) append f();
	
	return {*res0} == {<2,"4",["3"]>,<2,"4",["4"]>} && size(res0) == 8 
			&& {*res1} == {*res0} && size(res1) == 8
			&& {*res2} == {*res0} && size(res2) == 8 
			&& {*res3} == {*res0} && size(res3) == 8;	
}

public test bool test7() {

	// For-loops
	li0 = for(int i <- [1,2,3], str j <- ["4","5"], value k := j) append tuple[int,str,value] () { return <i,j,k>; };
	res0 = for(f <- li0) append f();
	
	li1 = for(i <- [1,2,3], j <- ["4","5"], k := j) append tuple[int,str,value] () { return <i,j,k>; };
	res1 = for(f <- li1) append f();	

	// Comprehensions
	li2 = [ tuple[int,str,value] () { return <i,j,k>; } | int i <- [1,2,3], str j <- ["4","5"], value k := j ];
	res2 = for(f <- li0) append f();
	
	li3 = [ tuple[int,str,value] () { return <i,j,k>; } | i <- [1,2,3], j <- ["4","5"], k := j ];
	res3 = for(f <- li1) append f();
	
	return {*res0} == {<3,"5","4">,<3,"5","5">} && size(res0) == 6 
			&& {*res1} == {*res0} && size(res1) == 6
			&& {*res2} == {*res0} && size(res2) == 6 
			&& {*res3} == {*res0} && size(res3) == 6;	
}

public test bool test8() {

	// For-loops
	li0 = for(int i <- [1,2,3], str j <- ["4","5"], [*value k,*value r] := [i,j]) append tuple[int,str,value,value] () { return <i,j,k,r>; };
	res0 = for(f <- li0) append f();
	
	li1 = for(i <- [1,2,3], j <- ["4","5"], [*k,*r] := [i,j]) append tuple[int,str,value,value] () { return <i,j,k,r>; };
	res1 = for(f <- li1) append f();	

	// Comprehensions
	li2 = [ tuple[int,str,value,value] () { return <i,j,k,r>; } | int i <- [1,2,3], str j <- ["4","5"], [*value k,*value r] := [i,j] ];
	res2 = for(f <- li0) append f();
	
	li3 = [ tuple[int,str,value,value] () { return <i,j,k,r>; } | i <- [1,2,3], j <- ["4","5"], [*k,*r] := [i,j] ];
	res3 = for(f <- li1) append f();
	
	return {*res0} == {<3,"5",[1,"4"],[]>,<3,"5",[1,"5"],[]>,<3,"5",[2,"4"],[]>,<3,"5",[2,"5"],[]>,<3,"5",[3,"4"],[]>,<3,"5",[3,"5"],[]>} && size(res0) == 18 
			&& {*res1} == {*res0} && size(res1) == 18
			&& {*res2} == {*res0} && size(res2) == 18 
			&& {*res3} == {*res0} && size(res3) == 18;	
}

public test bool test9() {

	// For-loops
	li0 = for(int i <- [1,2,3], str j <- ["4","5"], [*value k,*value r] <- [[i],[j]]) append tuple[int,str,value,value] () { return <i,j,k,r>; };
	res0 = for(f <- li0) append f();
	
	li1 = for(i <- [1,2,3], j <- ["4","5"], [*k,*r] <- [[i],[j]]) append tuple[int,str,value,value] () { return <i,j,k,r>; };
	res1 = for(f <- li1) append f();
	
	// Comprehensions
	li2 = [ tuple[int,str,value,value] () { return <i,j,k,r>; } | int i <- [1,2,3], str j <- ["4","5"], [*value k,*value r] <- [[i],[j]] ];
	res2 = for(f <- li0) append f();
	
	li3 = [ tuple[int,str,value,value] () { return <i,j,k,r>; } | i <- [1,2,3], j <- ["4","5"], [*k,*r] <- [[i],[j]] ];
	res3 = for(f <- li1) append f();
	
	return {*res0} == {<3,"5",["4"],[]>,<3,"5",["5"],[]>} && size(res0) == 24 
			&& {*res1} == {*res0} && size(res1) == 24
			&& {*res2} == {*res0} && size(res2) == 24 
			&& {*res3} == {*res0} && size(res3) == 24;	
}

public test bool test10() {
	// Comprehensions
	l0 = [ <s,r,L> | L:[s,r] <- [ [[1],[2]],[[3],[4]] ] ];
	l1 = [ <s,r,L> | L:[*s,*r] <- [ [1,2],[3,4] ] ];
	l2 = [ <s,r,L> | list[int] L:[*int s, *int r] <- [ [1,2], [3,4] ]];
	l3 = [ <s,r,S> | set[int] S:{*int s, *int r} <- [ {1,2}, {3,4} ] ];
	// For-loops
	l4 = for(L:[s,r] <- [ [[1],[2]],[[3],[4]] ]) append <s,r,L>;
	l5 = for(L:[*s,*r] <- [ [1,2],[3,4] ]) append <s,r,L>;
	l6 = for(list[int] L:[*int s, *int r] <- [ [1,2], [3,4] ]) append <s,r,L>;
	l7 = for(set[int] S:{*int s, *int r} <- [ {1,2}, {3,4} ]) append <s,r,S>;
/*TODO:?*/	
//	l8 = [ <s,r,L> | list[str] L:[*int s, *int r] <- [ [1,2], ["3","4"] ]];
//	l9 = [ <s,r,L> | list[int] L:[*str s, *str r] <- [ [1,2], ["3","4"] ]];
	l10 = [ <s,r,L> | L:[*str s, *str r] <- [ [1,2], ["3","4"] ]];
	l11 = [ *L | L:[3,4] <- [ [1,2], [3,4] ]];
	l12 = [ <s,r,L> | list[value] L:[*int s, *int r] <- [ [1,2], ["3","4"] ]];
	l13 = [ <s,r,L> | list[str] L:[*value s, *value r] <- [ [1,2], ["3","4"] ]];
	
	return l0 == [<[1],[2],[[1],[2]]>,<[3],[4],[[3],[4]]>]
			&& l1 == [ <[],[1,2],[1,2]>,<[1],[2],[1,2]>,<[1,2],[],[1,2]>,<[],[3,4],[3,4]>,<[3],[4],[3,4]>,<[3,4],[],[3,4]> ]
			&& {*l3} == {*[ <{1,2},{},{1,2}>,<{2},{1},{1,2}>,<{1},{2},{1,2}>,<{},{1,2},{1,2}>,<{3,4},{},{3,4}>,<{4},{3},{3,4}>,<{3},{4},{3,4}>,<{},{3,4},{3,4}> ]}
			&& l0 == l4
			&& l1 == l5
			&& l1 == l2
			&& l2 == l6
			&& {*l3} == {*l7}
//			&& l8 == []
//			&& l9 == []
			&& l10 == [<[],["3","4"],["3","4"]>,<["3"],["4"],["3","4"]>,<["3","4"],[],["3","4"]>]
			&& l11 == [3,4]
			&& l12 == [<[],[1,2],[1,2]>,<[1],[2],[1,2]>,<[1,2],[],[1,2]>]
			&& l13 == l10
		    ;	
}

data D = d1(int i, str s) | d2(str s1, str s2);
public test bool test11() {
	return [ el | str el <- <1,"2",3,"4"> ] == ["2","4"]
	&& [ el | value el <- <"1","2","3","4"> ] == ["1","2","3","4"]
			&& [ el | str el <- "node"(1,"2",3,"4") ] == ["2","4"]
			&& [ el | str el <- d1(1,"2") ] == ["2"]
			&& [ el | value el <- d2("1","2") ] == ["1","2"]
			&& [ el | list[str] el <- [ [1], ["2"], [3], ["4"] ] ] == [ ["2"], ["4"] ]
			&& [ el | list[value] el <- [ [1], [2], [3], [4] ] ] == [ [1], [2], [3], [4] ]
			&& { k | str k <- (1 : 1, "2" : 2, 3 : 3, "4" : 4) } == {"2","4"};
}

data DATA = d1(set[int] s) | d2(list[int] l);
public test bool test12() {
	l0 = for( { d1({e,*r}) } := { d1({1,2}) } ) append <e,r>;
	l1 = for( [ d1({e,*r}) ] := [ d1({1,2}) ] ) append <e,r>;
	l2 = for( { d2([*l,*r]) } := { d2([1,2]) } ) append <l,r>;
	l3 = for( [ d2([*l,*r]) ] := [ d2([1,2]) ] ) append <l,r>;
	return {*l0} == {<2,{1}>,<1,{2}>}
		&& {*l1} == {*l0}
		&& l2 == [<[],[1,2]>,<[1],[2]>,<[1,2],[]>]
		&& l3 == l2;
}
