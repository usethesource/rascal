package test;

import junit.framework.TestCase;

import java.io.IOException;

public class ComprehensionTests extends TestCase {
	
	public void testSetComprehension1() throws IOException {
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("{ X | int X : {} } == {};"));
		assertTrue(tf.runTest("{ X | int X : [] } == {};"));
		
		assertTrue(tf.runTest("{ X | int X : {1}} == {1};"));
		assertTrue(tf.runTest("{ X | int X : [1]} == {1};"));
		
		assertTrue(tf.runTest("{ X | int X : {1, 2}} == {1,2};"));
		assertTrue(tf.runTest("{ X | int X : [1, 2]} == {1,2};"));
		
		assertTrue(tf.runTest("{ X | int X : {1, 1, 1}} == {1};"));
		assertTrue(tf.runTest("{ X | int X : [1, 1, 1]} == {1};"));
		
		assertTrue(tf.runTest("{ 1 | int X : {1,2,3}} == {1};"));
		assertTrue(tf.runTest("{ 1 | int X : [1,2,3]} == {1};"));
		
		assertTrue(tf.runTest("{ 1 | int X : {1,2,3}, true } == {1};"));
		assertTrue(tf.runTest("{ 1 | int X : [1,2,3], true } == {1};"));
		
		assertTrue(tf.runTest("{ 1 | int X : {1,2,3}, false} 	== {};"));
		assertTrue(tf.runTest("{ 1 | int X : [1,2,3], false} 	== {};"));
		
		assertTrue(tf.runTest("{ X | int X : {1,2,3}} == {1,2,3};"));
		assertTrue(tf.runTest("{ X | int X : [1,2,3]} == {1,2,3};"));
		
		assertTrue(tf.runTest("{  X | int X : {1,2,3}, true} == {1,2,3};"));
		assertTrue(tf.runTest("{  X | int X : [1,2,3], true} == {1,2,3};"));
		
		assertTrue(tf.runTest("{  X | int X : {1,2,3}, false} 	== {};"));
		assertTrue(tf.runTest("{  X | int X : [1,2,3], false} 	== {};"));
		
		assertTrue(tf.runTest("{  X | int X : {1,2,3}, X >= 2, X < 3} == {2};"));
		assertTrue(tf.runTest("{  X | int X : [1,2,3], X >= 2, X < 3} == {2};"));
	}
	
	public void testSetComprehension2() throws IOException {	
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("{  {} | int X : {1,2,3}} == {{}};"));
		assertTrue(tf.runTest("{  {} | int X : [1,2,3]} == {{}};"));
		
		assertTrue(tf.runTest("{  {} | int X : {1,2,3}, true} == {{}};"));
		assertTrue(tf.runTest("{  {} | int X : [1,2,3], true} == {{}};"));
		
		assertTrue(tf.runTest("{  {} | int X : {1,2,3}, false} == {};"));
		assertTrue(tf.runTest("{  {} | int X : [1,2,3], false} == {};"));
		
		assertTrue(tf.runTest("{ <1,2,3> | int X : {1,2,3}} 	== {<1,2,3>};"));
		assertTrue(tf.runTest("{ <1,2,3> | int X : [1,2,3]} 	== {<1,2,3>};"));
		
		assertTrue(tf.runTest("{ <1,2,3> | int X : {1,2,3}, true} 	== {<1,2,3>};"));
		assertTrue(tf.runTest("{ <1,2,3> | int X : [1,2,3], true} 	== {<1,2,3>};"));
		
		assertTrue(tf.runTest("{ <1,2,3> | int X : {1,2,3}, true, true} == {<1,2,3>};"));
		assertTrue(tf.runTest("{ <1,2,3> | int X : [1,2,3], true, true} == {<1,2,3>};"));
		
		assertTrue(tf.runTest("{ <1,2,3> | int X : {1,2,3}, false}	== {} ;"));
		assertTrue(tf.runTest("{ <1,2,3> | int X : [1,2,3], false}	== {} ;"));
		
		assertTrue(tf.runTest("{ Y | list[int] Y : [[1,2,3],[10,20,30],[100,200,300]] } == { [1,2,3],[10,20,30],[100,200,300]};"));
		assertTrue(tf.runTest("{1 | 3 > 2} == {1} ;"));
		assertTrue(tf.runTest("{1 | 2 > 3} == {} ;"));
	}
	
	public void testAny() throws IOException {
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("any(int X : {1,2,3}, X > 2);"));
		assertTrue(tf.runTest("any(int X : {1,2,3}, X > 2, X <10);"));
		assertTrue(tf.runTest("any(int X : {1,2,3}, X > 2 && X <10);"));
		
		assertTrue(tf.runTest("any(int X : [1,2,3], X > 2);"));
		assertTrue(tf.runTest("any(int X : [1,2,3], X > 2, X < 10);"));
		assertTrue(tf.runTest("any(int X : [1,2,3], X > 2 && X < 10);"));
		
		assertFalse(tf.runTest("any(int X : {1,2,3}, X > 10);"));
		assertFalse(tf.runTest("any(int X : [1,2,3], X > 10);"));
		
		assertTrue(tf.runTest("any(<int X, int Y> : {<1,10>,<30,3>,<2,20>}, X > Y);"));
		assertTrue(tf.runTest("any(<int X, int Y> : [<1,10>,<30,3>,<2,20>], X > Y);"));
		
		assertFalse(tf.runTest("any(<int X, int Y> : {<1,10>,<30,3>,<2,20>}, X > 100*Y);"));
		assertFalse(tf.runTest("any(<int X, int Y> : [<1,10>,<30,3>,<2,20>], X > 100*Y);"));
	}
	
	public void testAll() throws IOException {
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("all(int X : {1,2,3}, X >= 1);"));
		assertTrue(tf.runTest("all(int X : {1,2,3}, X >= 1, X < 10);"));
		assertTrue(tf.runTest("all(int X : {1,2,3}, X >= 1 && X < 10);"));
		assertTrue(tf.runTest("all(int X : [1,2,3], X >= 1);"));
		assertTrue(tf.runTest("all(int X : {1,2,3}, X >= 1, X < 10);"));
		assertTrue(tf.runTest("all(int X : {1,2,3}, X >= 1 && X < 10);"));
		
		assertFalse(tf.runTest("all(int X : {1,2,3}, X >= 2);"));
		assertFalse(tf.runTest("all(int X : {1,2,3}, X >= 2, X <=2);"));
		assertFalse(tf.runTest("all(int X : {1,2,3}, X >= 2 && X <=2);"));
		assertFalse(tf.runTest("all(int X : [1,2,3], X >= 2);"));
		assertFalse(tf.runTest("all(int X : [1,2,3], X >= 2, X <= 2);"));
		assertFalse(tf.runTest("all(int X : [1,2,3], X >= 2 && X <= 2);"));
		
		assertTrue(tf.runTest("all(<int X, int Y> : {<1,10>,<3,30>,<2,20>}, X < Y);"));
		assertTrue(tf.runTest("all(<int X, int Y> : [<1,10>,<3,30>,<2,20>], X < Y);"));
		
		assertFalse(tf.runTest("all(<int X, int Y> : {<1,10>,<30,3>,<2,20>}, X < Y);"));
		assertFalse(tf.runTest("all(<int X, int Y> : [<1,10>,<30,3>,<2,20>], X < Y);"));
	}
	
	public void testSetComprehension3() throws IOException {	
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("{X + 1 | int X : {1,2,3}} == {2,3,4};"));
		assertTrue(tf.runTest("{X + 1 | int X : [1,2,3]} == {2,3,4};"));
		
		assertTrue(tf.runTest("{X | int X : {1,2,3}, X + 1 < 3} == {1};"));
		assertTrue(tf.runTest("{X | int X : [1,2,3], X + 1 < 3} == {1};"));
		
		assertTrue(tf.runTest("{X - 1 | int X : {1,2,3}} == {0,1,2};"));
		assertTrue(tf.runTest("{X - 1 | int X : [1,2,3]} == {0,1,2};"));
		
		assertTrue(tf.runTest("{X | int X : {1,2,3}, X - 1 < 3} == {1,2,3};"));
		assertTrue(tf.runTest("{X | int X : [1,2,3], X - 1 < 3} == {1,2,3};"));
		
		assertTrue(tf.runTest("{X * 2 | int X : {1,2,3}} == {2,4,6};"));
		assertTrue(tf.runTest("{X * 2 | int X : [1,2,3]} == {2,4,6};"));
	}
	
	public void testListComprehension1() throws IOException {
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("[ X | int X : {} ] == [];"));
		assertTrue(tf.runTest("[ X | int X : [] ] == [];"));
		assertTrue(tf.runTest("[ X |     X : [] ] == [];"));
		
		assertTrue(tf.runTest("[ X | int X : {1}] == [1];"));
		assertTrue(tf.runTest("[ X | int X : [1]] == [1];"));
		assertTrue(tf.runTest("[ X |     X : [1]] == [1];"));
		
		assertTrue(tf.runTest("{L = [ X | int X : {1, 2}]; (L == [1,2]) || (L == [2, 1]);}"));
		assertTrue(tf.runTest("[ X | int X : [1, 2]] == [1,2];"));
		
		assertTrue(tf.runTest("[ X | int X : {1, 1, 1}] == [1];"));
		assertTrue(tf.runTest("[ X | int X : [1, 1, 1]] == [1, 1, 1];"));
		
		assertTrue(tf.runTest("[ 1 | int X : {1,2,3}] == [1, 1, 1];"));
		assertTrue(tf.runTest("[ 1 | int X : [1,2,3]] == [1, 1, 1];"));
		
		assertTrue(tf.runTest("[ 1 | int X : {1,2,3}, true ] == [1, 1, 1];"));
		assertTrue(tf.runTest("[ 1 | int X : [1,2,3], true ] == [1, 1, 1];"));
		
		assertTrue(tf.runTest("[ 1 | int X : {1,2,3}, false] 	== [];"));
		assertTrue(tf.runTest("[ 1 | int X : [1,2,3], false] 	== [];"));
		
		assertTrue(tf.runTest("{L = [ X | int X : {1,2}]; (L == [1,2]) || (L == [2, 1]);}"));
		assertTrue(tf.runTest("[ X | int X : [1,2,3]] == [1,2,3];"));
		
		assertTrue(tf.runTest("{L = [  X | int X : {1,2}, true]; (L == [1,2]) || (L == [2, 1]);}"));
		assertTrue(tf.runTest("[  X | int X : [1,2,3], true] == [1,2,3];"));
		
		assertTrue(tf.runTest("[  X | int X : {1,2,3}, false] == [];"));
		assertTrue(tf.runTest("[  X | int X : [1,2,3], false] == [];"));
		
		assertTrue(tf.runTest("[  X | int X : {1,2,3}, X >= 2, X < 3] == [2];"));
		assertTrue(tf.runTest("[  X | int X : [1,2,3], X >= 2, X < 3] == [2];"));
	}
	
	public void testListComprehension2() throws IOException {
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("[  [] | int X : {1,2,3}] == [[], [], []];"));
		assertTrue(tf.runTest("[  [] | int X : [1,2,3]] == [[], [], []];"));
		
		assertTrue(tf.runTest("[  [] | int X : {1,2,3}, true] == [[], [], []];"));
		assertTrue(tf.runTest("[  [] | int X : [1,2,3], true] == [[], [], []];"));
		
		assertTrue(tf.runTest("[  [] | int X : {1,2,3}, false] == [];"));
		assertTrue(tf.runTest("[  [] | int X : [1,2,3], false] == [];"));
		
		assertTrue(tf.runTest("[ <1,2,3> | int X : {1,2,3}] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		assertTrue(tf.runTest("[ <1,2,3> | int X : [1,2,3]] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		
		assertTrue(tf.runTest("[ <1,2,3> | int X : {1,2,3}, true] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		assertTrue(tf.runTest("[ <1,2,3> | int X : [1,2,3], true] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		
		assertTrue(tf.runTest("[ <1,2,3> | int X : {1,2,3}, true, true] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		assertTrue(tf.runTest("[ <1,2,3> | int X : [1,2,3], true, true] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		
		assertTrue(tf.runTest("[ <1,2,3> | int X : {1,2,3}, false]	== [] ;"));
		assertTrue(tf.runTest("[ <1,2,3> | int X : [1,2,3], false]	== [] ;"));
	}
	
	public void testListComprehension3() throws IOException {
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("[ Y | list[int] Y : [[1,2,3],[10,20,30],[100,200,300]] ] == [ [1,2,3], [10,20,30],[100,200,300]];"));
		assertTrue(tf.runTest("[1 | 3 > 2] == [1] ;"));
		assertTrue(tf.runTest("[1 | 2 > 3] == [] ;"));
		
		assertTrue(tf.runTest("{L = [X + 1 | int X : {1,2}]; (L == [2,3]) || (L == [3,2]);}"));
		assertTrue(tf.runTest("[X + 1 | int X : [1,2,3]] == [2,3,4];"));
		
		assertTrue(tf.runTest("[X | int X : {1,2,3}, X + 1 < 3] == [1];"));
		assertTrue(tf.runTest("[X | int X : [1,2,3], X + 1 < 3] == [1];"));
		
		assertTrue(tf.runTest("{L = [X - 1 | int X : {1,2}]; (L == [0,1]) || (L == [1,0]);}"));
		assertTrue(tf.runTest("[X - 1 | int X : [1,2,3]] == [0,1,2];"));
		
		assertTrue(tf.runTest("{L = [X | int X : {2,3}, X - 1 < 3]; (L == [2,3]) || (L == [3,2]);}"));
		assertTrue(tf.runTest("[X | int X : [1,2,3], X - 1 < 3] == [1,2,3];"));
		
		assertTrue(tf.runTest("{ L = [X * 2 | int X : {2,3}]; (L == [4,6]) || (L == [6,4]);}"));
		assertTrue(tf.runTest("[X * 2 | int X : [1,2,3]] == [2,4,6];"));

	}
	
	public void testRelationComprehension() throws IOException {
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("{<X,Y> | <int X, int Y> : {}} == {} ;"));
		assertTrue(tf.runTest("{<X,Y> | <int X, int Y> : []} == {} ;"));
		
		assertTrue(tf.runTest("{<X,Y> | int X : {}, int Y : {}} == {};"));
		assertTrue(tf.runTest("{<X,Y> | int X : [], int Y : []} == {};"));
		
		assertTrue(tf.runTest("{<X,Y> | int X : {1,1,1}, int Y : {2,2,2}} == {<1,2>};"));
		assertTrue(tf.runTest("{<X,Y> | int X : [1,1,1], int Y : [2,2,2]} == {<1,2>};"));
		
		assertTrue(tf.runTest("{<1,2> | int X : {1,2,3}} == {<1,2>};"));
		assertTrue(tf.runTest("{<1,2> | int X : [1,2,3]} == {<1,2>};"));
		
		assertTrue(tf.runTest("{<X,Y> | int X : {1,2,3}, int Y : {2,3,4}} ==  {<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		assertTrue(tf.runTest("{<X,Y> | int X : [1,2,3], int Y : [2,3,4]} ==  {<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		
		assertTrue(tf.runTest("{<X,Y> | int X : {1,2,3}, int Y : {2,3,4}, true} ==	{<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		assertTrue(tf.runTest("{<X,Y> | int X : [1,2,3], int Y : [2,3,4], true} ==	{<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		
		
		assertTrue(tf.runTest("{<X,Y> | int X : {1,2,3}, int Y : {2,3,4}, false} == {};"));
		assertTrue(tf.runTest("{<X,Y> | int X : [1,2,3], int Y : [2,3,4], false} == {};"));
		
		assertTrue(tf.runTest("{<X,Y> | int X : {1,2,3}, int Y : {2,3,4}, X >= Y} =={<2, 2>, <3, 2>, <3, 3>};"));
		assertTrue(tf.runTest("{<X,Y> | int X : [1,2,3], int Y : [2,3,4], X >= Y} =={<2, 2>, <3, 2>, <3, 3>};"));
	/****/	
		assertTrue(tf.runTest("{<X,Y> | int X : {1,2,3}, <X, int Y> : {<1,10>, <7,70>, <3,30>,<5,50>}} == {<1, 10>, <3, 30>};"));
		assertTrue(tf.runTest("{<X,Y> | int X : [1,2,3], <X, int Y> : [<1,10>, <7,70>, <3,30>,<5,50>]} == {<1, 10>, <3, 30>};"));
		
		assertTrue(tf.runTest("{<X,Y> | int X : {1,2,3}, <X, str Y> : {<1,\"a\">, <7,\"b\">, <3,\"c\">,<5,\"d\">}} == {<1, \"a\">, <3, \"c\">};"));
		assertTrue(tf.runTest("{<X,Y> | int X : [1,2,3], <X, str Y> : [<1,\"a\">, <7,\"b\">, <3,\"c\">,<5,\"d\">]} == {<1, \"a\">, <3, \"c\">};"));
		
		}
	
	public void testMapComprehension() throws IOException {
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("( X : 2 * X | int X : {} ) == ();"));
		assertTrue(tf.runTest("( X : 2 * X | int X : [] ) == ();"));
		
		assertTrue(tf.runTest("( X : 2 * X | int X : {1}) == (1:2);"));
		assertTrue(tf.runTest("( X : 2 * X | int X : [1]) == (1:2);"));
		
		assertTrue(tf.runTest("( X : 2 * X | int X : {1, 2}) == (1:2,2:4);"));
		assertTrue(tf.runTest("( X : 2 * X | int X : [1, 2]) == (1:2,2:4);"));
		
		assertTrue(tf.runTest("( X: 2 * X| int X: [1,2,3] ) == (1:2,2:4,3:6);"));
	}
	
	public void testNodeGenerator() throws IOException {
		TestFramework tf = new TestFramework("data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);");
		
		assertTrue(tf.runTestInSameEvaluator("[ X | int X : f(i(1),g(i(2),i(3))) ] == [1,2,3];"));
		assertTrue(tf.runTestInSameEvaluator("[ X | value X : f(i(1),g(i(2),i(3))) ] == [1,i(1),2,i(2),3,i(3),g(i(2),i(3)),f(i(1),g(i(2),i(3)))];"));
		
		assertTrue(tf.runTestInSameEvaluator("[N | value N : f(i(1),i(2))] == [1,i(1),2,i(2),f(i(1),i(2))];"));
		assertTrue(tf.runTestInSameEvaluator("[N | bottom-up value N : f(i(1),i(2))] == [1,i(1),2,i(2),f(i(1),i(2))];"));
		
		assertTrue(tf.runTestInSameEvaluator("[N | top-down value N : f(i(1),i(2))] == [f(i(1),i(2)),i(1),1,i(2),2];"));
		
		assertTrue(tf.runTestInSameEvaluator("[N | TREE N : f(i(1),i(2))] == [i(1),i(2),f(i(1),i(2))];"));
		assertTrue(tf.runTestInSameEvaluator("[N | bottom-up TREE N : f(i(1),i(2))] == [i(1),i(2),f(i(1),i(2))];"));
		assertTrue(tf.runTestInSameEvaluator("[N | top-down TREE N : f(i(1),i(2))] == [f(i(1),i(2)),i(1),i(2)];"));
		
		assertTrue(tf.runTestInSameEvaluator("[N | int N : f(i(1),i(2))] == [1,2];"));
		
		assertTrue(tf.runTestInSameEvaluator("[N | value N : f(i(1),g(i(2),i(3)))] == [1,i(1),2,i(2),3,i(3),g(i(2),i(3)),f(i(1),g(i(2),i(3)))];"));
		assertTrue(tf.runTestInSameEvaluator("[N | bottom-up value N : f(i(1),g(i(2),i(3)))] == [1,i(1),2,i(2),3,i(3),g(i(2),i(3)),f(i(1),g(i(2),i(3)))];"));
		
		assertTrue(tf.runTestInSameEvaluator("[N | top-down value N : f(i(1),g(i(2),i(3)))] == [f(i(1),g(i(2),i(3))),i(1),1,g(i(2),i(3)),i(2),2,i(3),3];"));
		
		assertTrue(tf.runTestInSameEvaluator("[N | TREE N : f(i(1),g(i(2),i(3)))] == [i(1),i(2),i(3),g(i(2),i(3)),f(i(1),g(i(2),i(3)))];"));
		assertTrue(tf.runTestInSameEvaluator("[N | bottom-up TREE N : f(i(1),g(i(2),i(3)))] == [i(1),i(2),i(3),g(i(2),i(3)),f(i(1),g(i(2),i(3)))];"));
		
		assertTrue(tf.runTestInSameEvaluator("[N | top-down TREE N : f(i(1),g(i(2),i(3)))] == [f(i(1),g(i(2),i(3))), i(1),g(i(2),i(3)),i(2),i(3)];"));		
		
		assertTrue(tf.runTestInSameEvaluator("[N | int N : f(i(1),g(i(2),i(3)))] == [1,2,3];"));
		assertTrue(tf.runTestInSameEvaluator("[N | bottom-up int N : f(i(1),g(i(2),i(3)))] == [1,2,3];"));
		
		assertTrue(tf.runTestInSameEvaluator("[N | top-down int N : f(i(1),g(i(2),i(3)))] == [1,2,3];"));
	}
	
	public void xxtestRegularGenerators() throws IOException{
		TestFramework tf = new TestFramework();
		
		assertTrue(tf.runTest("[S | /@<S:[a-z]+>@/ : [\"@abc@\", \"@def@\"]] == [\"abc\",\"def\"];"));
		assertTrue(tf.runTest("{S | /@<S:[a-z]+>@/ : [\"@abc@\", \"@def@\"]} == {\"abc\", \"def\"};"));
		assertTrue(tf.runTest("{S | /@<S:[a-z]+>@/ : {\"@abc@\", \"@def@\"}} == {\"abc\", \"def\"};"));
	}
}
