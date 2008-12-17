package test;

import java.io.IOException;

import junit.framework.TestCase;

public class SubscriptTests extends TestCase {
	private TestFramework tf = new TestFramework();
	
	public void testList() throws IOException{
		
		assertTrue(tf.runTest("[0,1,2,3][0] == 0;"));
		assertTrue(tf.runTest("[0,1,2,3][1] == 1;"));
		assertTrue(tf.runTest("[0,1,2,3][2] == 2;"));
		assertTrue(tf.runTest("[0,1,2,3][3] == 3;"));
		
		assertTrue(tf.runWithError("[0,1,2,3][4] == 3;", "Subscript out of bounds"));
		
		assertTrue(tf.runTest("{list[int] L = [0,1,2,3]; L[0] = 10; L == [10,1,2,3];}"));
		assertTrue(tf.runTest("{list[int] L = [0,1,2,3]; L[1] = 11; L == [0,11,2,3];}"));
		assertTrue(tf.runTest("{list[int] L = [0,1,2,3]; L[2] = 22; L == [0,1,22,3];}"));
		assertTrue(tf.runTest("{list[int] L = [0,1,2,3]; L[3] = 33; L == [0,1,2,33];}"));
		
		assertTrue(tf.runWithError("{list[int] L = [0,1,2,3]; L[4] = 44; L == [0,1,2,3,44];}", "out of bounds"));
	}
	
	public void testMap() throws IOException{
		assertTrue(tf.runTest("(1:10, 2:20, 3:30)[1] == 10;"));
		assertTrue(tf.runTest("(1:10, 2:20, 3:30)[2] == 20;"));
		assertTrue(tf.runTest("(1:10, 2:20, 3:30)[3] == 30;"));
		
		//assertTrue(tf.runWithError("(1:10, 2:20, 3:30)[4] == 30;", "xxx"));
		
		assertTrue(tf.runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[1] = 100; M == (1:100, 2:20, 3:30);}"));
		assertTrue(tf.runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[2] = 200; M == (1:10, 2:200, 3:30);}"));
		assertTrue(tf.runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[3] = 300; M == (1:10, 2:20, 3:300);}"));
		assertTrue(tf.runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[4] = 400; M == (1:10, 2:20, 3:30, 4:400);}"));
	}
	
	public void testTuple() throws IOException{
		assertTrue(tf.runTest("<0, \"a\", 3.5>[0] == 0;"));
		assertTrue(tf.runTest("<0, \"a\", 3.5>[1] == \"a\";"));
		assertTrue(tf.runTest("<0, \"a\", 3.5>[2] == 3.5;"));
		
		assertTrue(tf.runWithError("<0, \"a\", 3.5>[3] == 3.5;", "Subscript out of bounds"));
	}
	
	public void testRelation() throws IOException{
		assertTrue(tf.runTest("{<1, \"a\">, <2, \"b\">}[0] == {};"));
		assertTrue(tf.runTest("{<1, \"a\">, <2, \"b\">}[1] == {\"a\"};"));
		assertTrue(tf.runTest("{<1, \"a\">, <2, \"b\">}[2] == {\"b\"};"));
		
		assertTrue(tf.runTest("{<1, \"a\">, <2, \"b\">, <1, \"abc\">}[1] == {\"a\", \"abc\"};"));
		
		assertTrue(tf.runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[0] == {};"));
		assertTrue(tf.runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[1] == {<\"a\", 10>, <\"abc\", 100>};"));
		assertTrue(tf.runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[2] == {<\"b\", 20>};"));
	}
	
	public void testRelationMultiIndex() throws IOException{
		assertTrue(tf.runTest("{<1,\"a\",1.0>,<2,\"b\",2.0>,<3,\"c\",3.0>}[0] == {};"));
		assertTrue(tf.runTest("{<1,\"a\",1.0>,<2,\"b\",2.0>,<3,\"c\",3.0>}[1] == {<\"a\",1.0>};"));
		assertTrue(tf.runTest("{<1,\"a\",1.0>,<2,\"b\",2.0>,<3,\"c\",3.0>}[2, \"b\"] == {2.0};"));
	}
	
	public void testTree() throws IOException{
		assertTrue(tf.runTest("f(0, \"a\", 3.5)[0] == 0;"));
		assertTrue(tf.runTest("f(0, \"a\", 3.5)[1] == \"a\";"));
		assertTrue(tf.runTest("f(0, \"a\", 3.5)[2] == 3.5;"));
		
		assertTrue(tf.runWithError("f(0, \"a\", 3.5)[3] == 3.5;", "Subscript out of bounds"));
		
		assertTrue(tf.runTest("{tree T = f(0, \"a\", 3.5); T[0] = 10; T == f(10, \"a\", 3.5);}"));
		
	}
}
