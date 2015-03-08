module experiments::Compiler::Examples::Tst3

import Type;
import Set;
//import lang::rascal::tests::types::StaticTestingUtils;
//private bool allLabeled1(list[Symbol] l) = all(li <- l, Symbol::\label(_,_) := li);

value main(list[value] args) {S = {10}; return (isEmpty(S) || all(x <- S, x >= 10));}

// {S = {10}; return (isEmpty(S) || all(x <- S, x >= 10));}

//test bool tst_max(set[int] S) = isEmpty(S) || all(x <- S, x <= max(S));

//test bool tst_min(set[int] S) = isEmpty(S) || all(x <- S, x >= min(S));

//all(k <- [10,20,30], 20 := k);

//all(li <- [10,20,30], 20 := li); //allLabeled1([\int(),\int()]);

//value main(list[value] args) =
//	unexpectedType("list[int] L = {1,2,3}; L *= [4];  L==[\<1,4\>,\<2,4\>,\<3,4\>];");	

//test bool all1()  = all(int X <- {1,2,3}, X >= 1);
//test bool all2()  = all(int X <- {1,2,3}, X >= 1, X < 10);
//test bool all3()  = all(int X <- {1,2,3}, X >= 1 && X < 10);
//test bool all4()  = all(int X <- [1,2,3], X >= 1);
//test bool all5()  = all(int X <- {1,2,3}, X >= 1, X < 10);
//test bool all6()  = all(int X <- {1,2,3}, X >= 1 && X < 10);
//  		
//test bool all7()  = !(all(int X <- {1,2,3}, X >= 2));
//test bool all8()  = !(all(int X <- {1,2,3}, X >= 2, X <=2));
//test bool all9()  = !(all(int X <- {1,2,3}, X >= 2 && X <=2));
//test bool all10()  = !(all(int X <- [1,2,3], X >= 2));
//test bool all11()  = !(all(int X <- [1,2,3], X >= 2, X <= 2));
//test bool all12()  = !(all(int X <- [1,2,3], X >= 2 && X <= 2));
//  		
//test bool all13()  = all(<int X, int Y> <- {<1,10>,<3,30>,<2,20>}, X < Y);
//test bool all14()  = all(<int X, int Y> <- [<1,10>,<3,30>,<2,20>], X < Y);
//  		
//test bool all15()  = !(all(<int X, int Y> <- {<1,10>,<30,3>,<2,20>}, X < Y));
//test bool all16()  = !(all(<int X, int Y> <- [<1,10>,<30,3>,<2,20>], X < Y));
//  		
//test bool all17()  = all(int i <- [0, 1] && [0, 1][i] == i);
//  		
//		// The following were all asserTrue, how can this have worked?
//  		
//@ignore{Changed semantics}
//test bool all18()  = !(all(_ <- []));
//		@ignore{Changed semantics}
//test bool all19()  = !(all(_ <- {}));
//@ignore{Changed semantics}
//test bool all20()  = !(all(_ <- ()));
//  		
//test bool all21()  = all(k <- [1,2,3], (k % 2 == 0 || k % 2 == 1)?true:false);
//
//test bool all22()  = all(k <- [10,10,10], 10 := k);
//test bool all23()  = !all(k <- [10,20,30], 20 := k);