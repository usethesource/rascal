package test;

import java.io.IOException;

import junit.framework.TestCase;

public class BackTrackingTests extends TestCase {
	private static TestFramework tf = new TestFramework();
	
	public void testSimple() throws IOException {
		
		assertTrue(tf.runTest("{([list[int] L1, int N, list[int] L2] ~= [1,2,3,4]) && (N == 3); " + 
								"(L1 == [1,2]) && (N == 3) && (L2 == [4]);}"));
		
		assertTrue(tf.runTest("{([list[int] L1, int N, list[int] L2] ~= [1,2,3,4]) && ((N == 3) || (N==4)); " + 
		"(L1 == [1,2]) && (N == 3) && (L2 == [4]);}"));
		
		assertTrue(tf.runTest("{([list[int] L1, int N, list[int] L2] ~= [1,2,3,4]) && " +
								"([list[int] L3, int M, list[int] L4] ~= [3,4]) && (N > M); " + 
							  "(N == 4);}"));
		
	}
}
