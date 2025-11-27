module lang::rascal::tests::functionality::Subscription
/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/

import Exception;

data NODE = f(int a, str b, real c);

//	 list

test bool listTest1() = [0,1,2,3][0] == 0;
test bool listTest2() = [0,1,2,3][1] == 1;
test bool listTest3() = [0,1,2,3][2] == 2;
test bool listTest4() = [0,1,2,3][3] == 3;

test bool listTest5() {list[int] L = [0,1,2,3]; L[0] = 10; return L == [10,1,2,3];}
test bool listTest6() {list[int] L = [0,1,2,3]; L[1] = 11; return L == [0,11,2,3];}
test bool listTest7() {list[int] L = [0,1,2,3]; L[2] = 22; return L == [0,1,22,3];}
test bool listTest8() {list[int] L = [0,1,2,3]; L[3] = 33; return L == [0,1,2,33];}
	
@expected{IndexOutOfBounds}
test bool listError1() = [0,1,2,3][4] == 3;
	
@expected{IndexOutOfBounds}
test bool listError2(){ list[int] L = [0,1,2,3]; L[4] = 44; L == [0,1,2,3,44]; return false; 	}
	
//	 map

test bool mapTest1() = (1:10, 2:20, 3:30)[1] == 10;
test bool mapTest2() = (1:10, 2:20, 3:30)[2] == 20;
test bool mapTest3() = (1:10, 2:20, 3:30)[3] == 30;

@expected{NoSuchKey}
test bool mapTest4() = (1:10, 2:20, 3:30)[4] == 30;

test bool mapTest5() {map[int,int] M = (1:10, 2:20, 3:30); M[1] = 100; return M == (1:100, 2:20, 3:30);}
test bool mapTest6() {map[int,int] M = (1:10, 2:20, 3:30); M[2] = 200; return M == (1:10, 2:200, 3:30);}
test bool mapTest7() {map[int,int] M = (1:10, 2:20, 3:30); M[3] = 300; return M == (1:10, 2:20, 3:300);}
test bool mapTest8() {map[int,int] M = (1:10, 2:20, 3:30); M[4] = 400; return M == (1:10, 2:20, 3:30, 4:400);}
	
//	 tuple

test bool  tupleTest1() = <0, "a", 3.5>[0] == 0;
test bool  tupleTest2() = <0, "a", 3.5>[1] == "a";
test bool  tupleTest3() = <0, "a", 3.5>[2] == 3.5;

// 	relation

test bool relationTest1() = {<1, "a">, <2, "b">}[0] == {};
test bool relationTest2() = {<1, "a">, <2, "b">}[1] == {"a"};
test bool relationTest3() = {<1, "a">, <2, "b">}[2] == {"b"};

test bool relationTest4() = {<1, "a">, <2, "b">, <1, "abc">}[1] == {"a", "abc"};

test bool relationTest5() = {<1, "a", 10>, <2, "b", 20>, <1, "abc", 100>}[0] == {};
test bool relationTest6() = {<1, "a", 10>, <2, "b", 20>, <1, "abc", 100>}[1] == {<"a", 10>, <"abc", 100>};
test bool relationTest7() = {<1, "a", 10>, <2, "b", 20>, <1, "abc", 100>}[2] == {<"b", 20>};
test bool relationTest8() = {<1, "a", 10>, <2, "b", 20>, <1, "abc", 100>}[{1,2}] == {<"a", 10>, <"b", 20>, <"abc", 100>};
	
test bool relationTest9() = {<1, "a", 10>, <2, "b", 20>, <1, "abc", 100>}[1,_] == {10, 100};
	
// 	relationMultiIndex

test bool relationMultiIndex1() = {<1,"a",1.0>,<2,"b",2.0>,<3,"c",3.0>}[0] == {};
test bool relationMultiIndex2() = {<1,"a",1.0>,<2,"b",2.0>,<3,"c",3.0>}[1] == {<"a",1.0>};
test bool relationMultiIndex3() = {<1,"a",1.0>,<2,"b",2.0>,<3,"c",3.0>}[2, "b"] == {2.0};
test bool relationMultiIndex4() = {<1,10,10.5>, <2,20,20.5>, <3,20,30.5>, <2,10,100.5>}[{1},{10,20}] == {10.5};

// node

test bool nodeTest1() = f(0, "a", 3.5)[0] == 0;
test bool nodeTest2() = f(0, "a", 3.5)[1] == "a";
test bool nodeTest3() = f(0, "a", 3.5)[2] == 3.5;
/* structure assignment no longer allowed; */
//test bool nodeTest4() {NODE T = f(0, "a", 3.5); T[0] = 10; return  T == f(10, "a", 3.5);}
	
@expected{IndexOutOfBounds}
test bool nodeBoundsError() = f(0, "a", 3.5)[3] == 3.5;
	
	

