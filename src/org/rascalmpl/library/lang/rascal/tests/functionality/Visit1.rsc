@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module lang::rascal::tests::functionality::Visit1

import Node;

test bool visit1a() {
	return visit([1,2,3]) {
				case list[int] l => [ ( 0 | it + i | int i <- l) ]
				case int i => i + 100
			}
			== [306]
			;
}

test bool visit1b() {
	int f1b(){
		visit([1,2,3]) {
				case list[int] l => [ ( 0 | it + i | int i <- l) ]
				case int _ : return 42;
			};
		return 101;
		}
	return f1b() == 42;
}

test bool visit2a(){
	return top-down visit([1,2,3]) {
				case list[int] l => [ ( 0 | it + i | int i <- l) ]
				case int i => i + 100
			}
			== [106];
}

test bool visit2b(){
	int f2b(){
		top-down visit([1,2,3]) {
				case list[int] l => [ ( 0 | it + i | int i <- l) ]
				case int _: return 42;
			}
		return 101;
	}
	return f2b() == 42;
}


test bool visit3a() {
	return visit([1,2,3]) {
				case list[int] l: insert [ ( 0 | it + i | int i <- l) ];
				case int i: { i = i + 100; insert i; i = i + 200; }
			}
			== [306];
}

test bool visit3b() {
	int f3b(){
		 visit([1,2,3]) {
				case list[int] l: insert [ ( 0 | it + i | int i <- l) ];
				case int i: { i = i + 100; return 42; }
			}
		return 101;
	}
	return f3b() == 42;	
}

test bool visit4a() {
	return top-down visit([1,2,3]) {
				case list[int] l: insert [ ( 0 | it + i | int i <- l) ];
				case int i: { i = i + 100; insert i; i = i + 200; }
			}
			== [106];
}

test bool visit4b() {
	int f4b(){
		top-down visit([1,2,3]) {
				case list[int] l: insert [ ( 0 | it + i | int i <- l) ];
				case int i: { i = i + 100; return 42; }
			}
		return 101;
	}
	return f4b() == 42;
}

test bool visit5(){
	return visit( (1:"1",2:"2",3:"3") ) { // ( 306:"1 + 100; 2 + 100; 3 + 100; " )
				case map[int,str] m => ( ( 0 | it + k | int k <- m) : ( "" | it + m[k] | int k <- m) )
				case int i => i + 100
				case str s => s + " + 100; "
			}
			== (306:"3 + 100; 2 + 100; 1 + 100; ");
}

test bool visit6(){
	return top-down visit( (1:"1",2:"2",3:"3") ) { // ( 106:"321 + 100; " )
				case map[int,str] m => ( ( 0 | it + k | int k <- m) : ( "" | it + m[k] | int k <- m) )
				case int i => i + 100
				case str s => s + " + 100; "
			}
			== (106:"132 + 100; ");
}

test bool visit3() {
	return visit({ [1,1], [2,2], [3,3] }) { // { [202], [204], [206], [4,5] }
				case set[list[int]] s => s + { [4,5] }
				case list[int] l => [ ( 0 | it + i | int i <- l) ]
				case int i => i + 100
			}
			== { [206], [4,5], [204], [202] };
}

test bool visit7() {
	return top-down visit({ [1,1], [2,2], [3,3] }) { // { [102], [104], [106], [109] }
				case set[list[int]] s => s + { [4,5] }
				case list[int] l => [ ( 0 | it + i | int i <- l) ]
				case int i => i + 100
			}
			== { [102], [104], [106], [109] };
}

test bool visit8() {
	return visit({ <1,1>, <2,2>, <3,3> }) {
				case set[tuple[int,int]] s => s + { <4,5> }
				case tuple[int,int] t => { elem = ( 0 | it + i | int i <- t); <elem,elem>; }
				case int i => i + 100
			}
			== {<204,204>, <202,202>, <206,206>, <4,5>};
			
}

test bool visit9() {
	return top-down visit({ <1,1>, <2,2>, <3,3> }) {
				case set[tuple[int,int]] s => s + { <4,5> }
				case tuple[int,int] t => { elem = ( 0 | it + i | int i <- t); <elem,elem>; }
				case int i => i + 100
			}
			== {<106,106>, <104,104>, <109,109>, <102,102>}
			;
}

test bool visit10() {
	return visit({ "a"(1,1), "b"(2,2), "c"(3,3) }) {
				case set[node] s => s + { "d"(4,5) }
				case node n:str s(int _,int _) => { elem = ( 0 | it + i | int i <- n); (s + "_been_here")(elem,elem); }
				case int i => i + 100
			} 
			==
			{ "a_been_here"(202,202),
  			  "b_been_here"(204,204),
  			  "d"(4,5),
  			  "c_been_here"(206,206)
			}
			;
}

test bool visit11() {
	return top-down visit({ "a"(1,1), "b"(2,2), "c"(3,3) }) {
				case set[node] s => s + { "d"(4,5) }
				case node n:str s(int _,int _) => { elem = ( 0 | it + i | int i <- n); (s + "_been_here")(elem,elem); }
				case int i => i + 100
			}
			==
			{ "a_been_here"(102,102),
  			  "c_been_here"(106,106),
  			  "b_been_here"(104,104),
  			  "d_been_here"(109,109)
			}
			;
}

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);

test bool visit12() {
	return visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[list[ABCD]] s => s + { [ d(4,5) ] }
				case a(int x, int y) => a(x + 1000, y + 1000)
				case ABCD nd => { elem = ( 0 | it + i | int i <- nd); a(elem,elem); }
				case int i => i + 100
			} 
			==
			{	[d(4,5)],
  				[a(1101,1101)],
  				[a(204,204)],
  				[a(206,206)]
			}
			;
}

test bool visit13() {
	return top-down visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[list[ABCD]] s => s + { [ d(4,5) ] }
				case a(int x, int y) => b(x + 1000, y + 1000)
				case ABCD nd => { elem = ( 0 | it + i | int i <- nd); a(elem,elem); }
				case int i => i + 100
			}
			==
			{	[a(106,106)],
  				[a(109,109)],
  				[b(1101,1101)],
  				[a(104,104)]
			}
			;
}

test bool visit14() {
	return bottom-up-break visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[list[ABCD]] s => s + { [ d(5,5) ] }  // should not match
				case list[ABCD] l => l + [ d(4,4) ]           // should match only for [ c(3,3) ]
				case a(int x, int y) => a(x + 1000, y + 1000) // should match
				case b(int x, int y) => b(x + 1000, y + 1000) // should not match
				case 2 => 102
			} // { [ b(102,102) ], [ c(3,3), d(4,4) ], [ a(1001,1001) ] }
			==
			{	[b(102,102)],
  				[a(1001,1001)],
  				[c(3,3), d(4,4)]
			}
			;
}

test bool visit15() {
	return top-down-break visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				// case set[list[ABCD]] s => s + { [ d(5,5) ] } 
				case [ a(int x, int y) ] => [ a(x + 10, y + 10), d(4,4) ]
				case a(int x, int y) => a(x + 1000, y + 1000)
				case b(int x, int y) => b(x + 1000, y + 1000)
				case int i => i + 100
			} 
			==
			{	[a(11,11), d(4,4)],
  				[b(1002,1002)],
  				[c(103,103)]
			}
			;
}


test bool visit16a() {
	
	l = [ 1,0,1,1,0,1,0,1,0,1,0 ]; // 11

	return outermost visit({ l }) {
		case [*int sub, 1, 0] => [ 10, *sub ]
		case 10 => 20
	}
	== 
	{[20,20,20,20,1,0,1]};
}

test bool visit16b() {
	
	l = [ 1,0,1,1,0,1,0,1,0,1,0 ]; // 11
	int f16b(){
		outermost visit({ l }) {
			case [*int sub, 1, 0] => [ 10, *sub ]
			case 10: return 42;
		}
		return 101;
	}
	return f16b() == 42;
}

test bool visit17a() {
	
	l = [ 1,0,1,1,0,1,0,1,0,1,0 ]; // 11

	return innermost visit({ l }) {
		case [*int sub, 1, 0] => [ 10, *sub ]
		case 10 => 20
	}
	==
	{[10,10,10,10,1,0,1]};
}

test bool visit17b() {
	l = [ 1,0,1,1,0,1,0,1,0,1,0 ]; // 11
	int f17b(){
	 	innermost visit({ l }) {
			case [*int _, 1, 0]: return 42;
			case 10 => 20
		}
		return 101;
	}	
	return f17b() == 42;
}

test bool visit18() {
	l = [ 1,0,1,0,1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,0,1,0 ];

	return outermost visit({ l }) {
		case [*int sub, 1, 0] => [ 998, *sub ]
		case [*int sub, 1, 1] => [ 666, *sub ]
		case 998 => 999
	}
	==
	{[666,999,999,666,666,999,999,999,999,1,0,1,0,1]};
}

data LIST = lst(list[int] elems);

@ignoreInterpreter{Interpreter crashes on this test}
@ignoreCompiler{Gives [lst(["666"])] which the most likely answer}
test bool visit19() {
	return [ visit(lst([1])) {
				// list[str] <: list[value]; typeOf(subject) <: list[value] 
				// '+' list[str] <: typeOf(subject)
				case list[value] _: insert [ "666" ];
				case list[int] l: { insert l + [ 666 ]; l = l + [ 777 ]; }
				case int _: insert 999;
		     } ]
		     == 
		      [lst([999,666])];
}

@ignoreInterpreter{Interpreter crashes on this test}
@ignoreCompiler{Gives incorrect answer [lst([999])]}
test bool visit20() {
	return 
		   [ visit(lst([1])) {
				case list[value] _ => [ "666" ]
				case list[int] l => l + [ 666 ]
				case int _ => 999
		     } ]
		   ==
		   [ lst([999,666]) ];
}



data X = weird1(list[void] x);

@ignoreInterpreter{Interpreter crashes on this test}
test bool visit21() = visit (weird1([])) { case list[int] _ => [1] } == weird1([]);

data Y = weird2(list[int] y);

@IgnoreCompiler{
/* TODO: fails in compiler: because we require that the dynamic type of the replacement is a subtype of the dynamic type of the subject, 
 *       however list[int] !<: list[void]
 *       Should become: require that the dynamic type of the replacement is a subtype of the static type of the subject.
 */
 
/* This a very tricky case! At the moment the compiler has the following information available:
 *  - the static types of the subject, the pattern and the replacement
 * At runtime are available:
 * -  the dynamic types of the subject, the visited subtree and the replacement.
 * What is NOT available is the static type of the visited subtree of the subject.
 * This would require the following:
 * - maintain a path from the root of the subject to the current subtree
 * - use this path to determine the static type of the current subtree.
 */
}
test bool visit22() = 
	visit (weird2([])) { case list[int] _ => [1] } == weird2([1]);

data Z = z(int n);

test bool visit23() = visit (z(2)) { case node _ => z(3) } == z(3);

@ignoreInterpreter{Interpreter crashes on this test}
test bool visit24() = visit([]) { case _ => [1] } == [];


test bool visit27() {
	return visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
			  		case a(x,y) => a(x + 1000, y + 2000)
			    }
		==      { [a(1001,2001)],
  				  [c(3,3)],
 				  [b(2,2)]
				};
}

test bool visit28() {
	return visit([1,2]) {
    		case list[int] l => l when [1,2] := l 
		   }
		   ==
		  [1, 2];
}

data NODE1 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2)|h(value V1, value V2, value V3);

data T = knot(int i, T l, T r) | tip(int i);

data NODE10 = f1(int I) | g1(list[NODE10] L) | h1(NODE10 N1, NODE10 N2);
	
int cnt(NODE1 t) {
	int C = 0;
	visit(t) {
		case int _: C = C + 1;
	}
	return C;
}
	     
NODE1 walk(NODE1 t) {
	return 
		visit(t) {
			case int N=>x when x:=N*2, x>=1
		};
}
	     
NODE1 drepl(NODE1 T) {
	return 
		bottom-up-break visit (T) {
			case g(value T1, value T2) =>  h(T1, T2)
		};
}
		   
NODE1 frepa(NODE1 T) {
	return 
		visit (T) {
			case g(value T1, value T2): insert h(T1, T2);
		};
}
			
NODE1 frepb(NODE1 T) { 
	return 
		visit (T) {
			case g(value T1, value T2) => h(T1, T2)
		};
}
			
NODE1 frepG2H3a(NODE1 T) {
	return 
		visit (T) {
			case g(value T1, value T2): insert h(T1, T2, 0);
		};
}
			
NODE1 frepG2H3b(NODE1 T) {
	return
		visit (T) {
			case g(value T1, value T2) => h(T1, T2, 0)
		};
}
			
NODE1 inc(NODE1 T) {
	return 
		visit(T) {
			case int N: insert N + 1;
		}
}
	
tuple[int, NODE1] inc_and_count(NODE1 T, int D) {
	int C = 0;
	T = visit (T) {
			case int N: {
				C = C + 1;
			    insert N + D;
			}
	};
	return <C, T>;
}
	
NODE1 srepl(NODE1 T) {
	return 
		top-down-break visit (T) {
			case g(value T1, value T2) =>  h(T1, T2)
		};
}	
  
list[int] order(NODE10 T) {
	res = [];
	visit (T) {
		case int N:  res += N;
	};
	return res;
}	
 
//  Cnt()

test bool Cnt1()= cnt(f(3)) == 1;
test bool Cnt2()= cnt(f(1,2,3)) == 3;
test bool Cnt3()= cnt(f(1,g(2,3))) == 3;
test bool Cnt4()= cnt(f(1,g(2,[3,4,5]))) == 5;
test bool Cnt5()= cnt(f(1,g(2,{3,4,5}))) == 5;
test bool Cnt6()= cnt(f(1,g(2,<3,4,5>))) == 5;
test bool Cnt7()= cnt(f(1,g(2,{<1,10>,<2,20>}))) == 6;
test bool Cnt8()= cnt(f(1,g(2,(1:10,2:20)))) == 6;
	
//	When

test bool When1()= walk(f(3)) == f(6);
test bool When2()= walk(f(1,2,3)) == f(2,4,6);
test bool When3()= walk(f(1,g(2,3))) == f(2, g(4, 6));
test bool When4()= walk(f(1,g(2,[3,4,5]))) == f(2, g(4, [6, 8, 10]));

//	NewTreeVisibleBottomUp

	test bool NewTreeVisibleBottomUp() =
		visit(knot(0,tip(0),tip(0))) { case tip(int i) => tip(i+1) case knot(int i, T l, T r) => knot(i + l.i + r.i, l, r) } == knot(2,tip(1),tip(1));
	
//	Drepl

test bool Drepl1()= drepl(f(3)) == f(3);
test bool Drepl2()= drepl(g(1,2)) == h(1,2);
test bool Drepl3()= drepl(g(1,f(g(2,3)))) == g(1,f(h(2,3)));
		// The following test used to work, but now that we are using more and more static types it fails.
		// Explanation: [g(2,3),4,5] has as type list[value] and the elements have static type value as well.
		// In particular g(2,3) has type value.
		// As a result the node pattern g(value T1, value T2) in the case does not match.
		// test bool Drepl()= + drepl + "drepl(g(1,f([g(2,3),4,5]))) == g(1,f([h(2,3),4,5]));}"));


//	FrepA	

test bool FrepA1()= frepa(f(3)) == f(3);
test bool FrepA2()= frepa(f(1,2,3)) == f(1,2,3);
test bool FrepA3()= frepa(f(1,g(2,3))) == f(1,h(2,3));
test bool FrepA4()= frepa(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));
test bool FrepA5()= frepa(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));
test bool FrepA6()= frepa(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));
test bool FrepA7()= frepa(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));
test bool FrepA8()= frepa(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));
	
//	FrepB

test bool FrepB1()= frepb(f(3)) == f(3);
test bool FrepB2()= frepb(f(1,2,3)) == f(1,2,3);
test bool FrepB3()= frepb(f(1,g(2,3))) == f(1,h(2,3));
test bool FrepB4()= frepb(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));
test bool FrepB5()= frepb(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));
test bool FrepB6()= frepb(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));
test bool FrepB7()= frepb(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));
test bool FrepB8()= frepb(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));

//	FrepG2H3a

test bool FrepG2H3a1()= frepG2H3a(f(3)) == f(3);
test bool FrepG2H3a2()= frepG2H3a(f(1,2,3)) == f(1,2,3);
test bool FrepG2H3a3()= frepG2H3a(f(1,g(2,3))) == f(1,h(2,3,0));
test bool FrepG2H3a4()= frepG2H3a(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));
test bool FrepG2H3a5()= frepG2H3a(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));
test bool FrepG2H3a6()= frepG2H3a(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));
test bool FrepG2H3a7()= frepG2H3a(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));
test bool FrepG2H3a8()= frepG2H3a(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));
		
//	FrepG2H3b
		
test bool FrepG2H3b1()= frepG2H3b(f(3)) == f(3);
test bool FrepG2H3b2()= frepG2H3b(f(1,2,3)) == f(1,2,3);
test bool FrepG2H3b3()= frepG2H3b(f(1,g(2,3))) == f(1,h(2,3,0));
test bool FrepG2H3b4()= frepG2H3b(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));
test bool FrepG2H3b5()= frepG2H3b(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));
test bool FrepG2H3b6()= frepG2H3b(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));
test bool FrepG2H3b7()= frepG2H3b(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));
test bool FrepG2H3b8()= frepG2H3b(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));

//	Inc

test bool Inc1()= inc(f(3)) == f(4);
test bool Inc2()= inc(f(1,2,3)) == f(2,3,4);
test bool Inc3()= inc(f(1,g(2,3))) == f(2,g(3,4));
test bool Inc4()= inc(f(1,g(2,[3,4,5]))) == f(2,g(3,[4,5,6]));
test bool Inc5()= inc(f(1,g(2,{3,4,5}))) == f(2,g(3,{4,5,6}));
test bool Inc6()= inc(f(1,g(2,<3,4,5>))) == f(2,g(3,<4,5,6>));
test bool Inc7()= inc(f(1,g(2,{<1,10>,<2,20>}))) == f(2,g(3,{<2,11>,<3,21>}));
test bool Inc8()= inc(f(1,g(2,(1:10,2:20)))) == f(2,g(3,(2:11,3:21)));
	
//	IncAndCount

test bool IncAndCount1()= inc_and_count(f(3),10)                       == <1,f(13)>;
test bool IncAndCount2()= inc_and_count(f(1,2,3), 10)                  == <3,f(11,12,13)>;
test bool IncAndCount3()= inc_and_count(f(1,g(2,3)), 10)               == <3, f(11,g(12,13))>;
test bool IncAndCount4()= inc_and_count(f(1,g(2,[3,4,5])), 10)         == <5,f(11,g(12,[13,14,15]))>;
test bool IncAndCount5()= inc_and_count(f(1,g(2,{3,4,5})), 10)         == <5,f(11,g(12,{13,14,15}))>;
test bool IncAndCount6()= inc_and_count(f(1,g(2,<3,4,5>)), 10)         == <5,f(11,g(12,<13,14,15>))>;
test bool IncAndCount7()= inc_and_count(f(1,g(2,{<1,10>,<2,20>})), 10) == <6,f(11,g(12,{<11,20>,<12,30>}))>;
test bool IncAndCount8()= inc_and_count(f(1,g(2,(1:10,2:20))),10)      == <6, f(11,g(12,(11:20,12:30)))>;
	
//	Srepl

test bool srepl1()= srepl(f(3)) == f(3);
test bool srepl2()= srepl(g(1,2)) == h(1,2);
test bool srepl3()= srepl(g(1,f(g(2,3)))) == h(1,f(g(2,3)));
test bool srepl4()= srepl(g(1,f([g(2,3),4,5]))) == h(1,f([g(2,3),4,5]));

//	Order

test bool order1()= order(f1(3)) == [3];
test bool order2()= order(g1([f1(1),f1(2)])) == [1,2];
test bool order3()= order(h1(f1(1),h1(f1(2),f1(3)))) == [1,2,3];
test bool order4()= order(h1(f1(1),g1([h1(f1(2),f1(3)),f1(4),f1(5)]))) == [1,2,3,4,5];


// VisitWithAnno

data NODE = nd(NODE left, NODE right) | leaf(int n);

anno int NODE@pos;

NODE N1 = nd(leaf(0)[@pos=0], leaf(1)[@pos=1])[@pos=2];

test bool visitWithAnno1() {
	return visit(leaf(1)[@pos=1]){
		case leaf(1) => leaf(10)
		default:;
	}
	==
	leaf(10);
}

test bool visitWithAnno2() {
	return visit(N1){
		default:;
	}
	==
	N1;
}

test bool visitWithAnno3() {
	return visit(N1){
		case leaf(1) => leaf(10)
		default:;
	}
	==
	nd(leaf(0)[@pos=0], leaf(10))[@pos=2];
}

test bool visitWithAnno4() {
	return visit(N1){
		case leaf(0) => leaf(0)
		case leaf(1) => leaf(10)
		default:;
	}
	==
	nd(leaf(0), leaf(10))[@pos=2];
}

test bool visitWithAnno5() {
	return visit(N1){
		case leaf(0) => leaf(0)
		case leaf(1) => leaf(10)
		case nd(left, right) => nd(right, left)
		default:;
	}
	==
	nd(leaf(10), leaf(0));
}

public &T delAnnotationsRec1(&T v) = visit(v) { 
     case node n => delAnnotations(n) 
  };
  
public &T delAnnotationsRec2(&T v) = visit(v) { 
     case node n: { insert delAnnotations(n); }
  };

public NODE A1 = leaf(3);
public NODE A2 = leaf(3)[@pos = 1];

test bool visitWithAnno6() = !delAnnotationsRec1(A2)@pos?;

test bool visitWithAnno7() = !delAnnotationsRec2(A2)@pos?;


// StringVisit1a

test bool StringVisit1a1()=visit(""){ case /b/: insert "B";} == "";
test bool StringVisit1a2()=visit("a"){ case /b/: insert "B";} == "a";
test bool StringVisit1a3()=visit("b"){ case /b/: insert "B";} == "B";
test bool StringVisit1a4()=visit("abc"){ case /b/: insert "B";} == "aBc";
test bool StringVisit1a5()=visit("abcabc"){ case /b/: insert "B";} == "aBcaBc";
	
//	StringVisit1b

test bool StringVisit1b1()=visit(""){ case /b/ => "B"} == "";
test bool StringVisit1b2()=visit("a"){ case /b/ => "B"} == "a";
test bool StringVisit1b3()=visit("b"){ case /b/ => "B"} == "B";
test bool StringVisit1b4()=visit("abc"){ case /b/ => "B"} == "aBc";
test bool StringVisit1b5()=visit("abcabc"){ case /b/ =>"B"} == "aBcaBc";
	
//	StringVisit2
		
test bool StringVisit2a1()=visit(""){ case /b/: insert "BB";} == "";
test bool StringVisit2a2()=visit("a"){ case /b/: insert "BB";} == "a";
test bool StringVisit2a3()=visit("b"){ case /b/: insert "BB";} == "BB";
test bool StringVisit2a4()=visit("abc"){ case /b/: insert "B";} == "aBc";
test bool StringVisit2a5()=visit("abcabc"){ case /b/: insert "BB";} == "aBBcaBBc";
	
//	StringVisit3
		
test bool StringVisit3a1()=visit(""){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "";
test bool StringVisit3a2()=visit("a"){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "AA";
test bool StringVisit3a3()=visit("b"){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "BB";
test bool StringVisit3a4()=visit("abcabc"){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "AABBcAABBc";
test bool StringVisit3a5()=visit("abcabca"){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "AABBcAABBcAA";
	
// StringVisit4
		
test bool StringVisit4a1()=visit(""){ case "a": insert "AA"; case /b/: insert "BB";} == "";
test bool StringVisit4a2()=visit("a"){ case "a": insert "AA"; case /b/: insert "BB";} == "AA";
test bool StringVisit4a3()=visit("b"){ case "a": insert "AA"; case /b/: insert "BB";} == "BB";
test bool StringVisit4a4()=visit("abcabc"){ case "a": insert "AA"; case /b/: insert "BB";} == "aBBcaBBc";
test bool StringVisit4a5()=visit("abcabca"){ case "a": insert "AA"; case /b/: insert "BB";} == "aBBcaBBcAA";

// StringVisit5

tuple[int,int] cntAB(str s){
	int cntA = 0;
	int cntB = 0;
	visit(s){ case /^a/: cntA += 1; case /^b/: cntB += 10;}
	
	return <cntA, cntB>;
}

test bool StringVisit51() = cntAB("") == <0, 0>;
test bool StringVisit52() = cntAB("cdefg") == <0, 0>;
test bool StringVisit53() = cntAB("a") == <1, 0>;
test bool StringVisit54() = cntAB("b") == <0, 10>;
test bool StringVisit55() = cntAB("ab") == <1, 10>;
test bool StringVisit56() = cntAB("ba") == <1, 10>;
test bool StringVisit57() = cntAB("abcabca") == <3, 20>;

// StringVisit6

tuple[int,int] TDCntAB(str s){
	int cntA = 0;
	int cntB = 0;
	top-down visit(s){ case /^a/: cntA += 1; case /^b/: cntB += 10;}
	
	return <cntA, cntB>;
}

test bool StringVisit61() = TDCntAB("") == <0, 0>;
test bool StringVisit62() = TDCntAB("cdefg") == <0, 0>;
test bool StringVisit63() = TDCntAB("a") == <1, 0>;
test bool StringVisit64() = TDCntAB("b") == <0, 10>;
test bool StringVisit65() = TDCntAB("ab") == <1, 10>;
test bool StringVisit66() = TDCntAB("ba") == <1, 10>;
test bool StringVisit67() = TDCntAB("abcabca") == <3, 20>;

// StringVisit7

str deescape(str s) = visit(s) { case /\\<c: [\" \' \< \> \\ b f n r t]>/m => c };

test bool StringVisit71() = deescape("abc") == "abc";
test bool StringVisit72() = deescape("\\") == "\\";
test bool StringVisit73() = deescape("\\\\") == "\\";
test bool StringVisit74() = deescape("\\\<") == "\<";
test bool StringVisit75() = deescape("\\\>") == "\>";
test bool StringVisit76() = deescape("\\n") == "n";

// Keywords and visit

data RECT = rect(int w, int h, str color = "white");

test bool KeywordVisit1()=visit("f"(1, kw1="abc", kw2=13)){ case 1 => 10 case "abc" => "def" case 13 => 14} == "f"(10, kw1="def", kw2=14);
test bool KeywordVisit2()=visit(rect(10,20,color="white")){ case "white" => "red"} == rect(10, 20, color="red");

test bool nestedEmptyStringVisit1() {
  x = [""];
  result = [];
  bottom-up visit (x) {
    case value y: 
      result += [y];
   }

  return result == ["",  [""]];
}

test bool nestedEmptyStringVisit2() {
  x = ["","1","2"];
  result = [];
  bottom-up visit (x) {
    case value y: 
      result += [y];
   }

  return result == ["", "1", "2", ["", "1", "2"]];
}
