package test;

import junit.framework.TestCase;
import java.io.IOException;

public class AssignmentTests extends TestCase {
	
	private static TestFramework tf = new TestFramework();
	
	public void testSimple() throws IOException {
		
		assertTrue(tf.runTest("{bool b = true; b == true;}"));
		assertTrue(tf.runTest("{b = true; b == true;}"));
	}
	
	public void testTuple() throws IOException {
		assertTrue(tf.runTest("{int a = 1; int b = 2; <a, b> = <b, a>; (a == 2) && (b == 1);}"));
		assertTrue(tf.runTest("{<a, b> = <1, 2>; (a == 1) && (b == 2);}"));
		assertTrue(tf.runTest("{int a; int b; <a, b> = <1, 2>; (a == 1) && (b == 2);}"));
	}
	
	public void testList() throws IOException {
		assertTrue(tf.runTest("{list[int] L = []; L == [];}"));
		assertTrue(tf.runTest("{list[int] L = [0,1,2]; L[1] = 10; L == [0,10,2];}"));
		assertTrue(tf.runTest("{L = [0,1,2]; L[1] = 10; L == [0,10,2];}"));
		assertTrue(tf.runTest("{list[list[int]] L = [[0,1],[2,3]]; L[1][0] = 20; L == [[0,1],[20,3]];}"));
		assertTrue(tf.runTest("{L = [[0,1],[2,3]]; L[1][0] = 20; L == [[0,1],[20,3]];}"));
	}
	
	public void testSet() throws IOException {
		assertTrue(tf.runTest("{set[int] S = {}; S == {};}"));
	}
}
