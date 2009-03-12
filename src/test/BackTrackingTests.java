package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BackTrackingTests extends TestFramework {
	
	@Test public void testSimple() {
		
		assertTrue(runTest("{([list[int] L1, int N, list[int] L2] := [1,2,3,4]) && (N == 3); " + 
								"(L1 == [1,2]) && (N == 3) && (L2 == [4]);}"));
		
		assertTrue(runTest("{([list[int] L1, int N, list[int] L2] := [1,2,3,4]) && ((N == 3) || (N==4)); " + 
		"(L1 == [1,2]) && (N == 3) && (L2 == [4]);}"));
		
		assertTrue(runTest("{([list[int] L1, int N, list[int] L2] := [1,2,3,4]) && " +
								"([list[int] L3, int M, list[int] L4] := [3,4]) && (N > M); " + 
							  "(N == 4);}"));
		
	}
}
