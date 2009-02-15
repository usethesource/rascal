package test;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AssignmentTests extends TestFramework {
	
	@Test public void testSimple() throws IOException {
		
		assertTrue(runTest("{bool b = true; b == true;}"));
		assertTrue(runTest("{b = true; b == true;}"));
	}
	
	@Test public void testTuple() throws IOException {
		assertTrue(runTest("{int a = 1; int b = 2; <a, b> = <b, a>; (a == 2) && (b == 1);}"));
		assertTrue(runTest("{<a, b> = <1, 2>; (a == 1) && (b == 2);}"));
	}
	
	@Test public void testList() throws IOException {
		assertTrue(runTest("{list[int] L = []; L == [];}"));
		assertTrue(runTest("{list[int] L = [0,1,2]; L[1] = 10; L == [0,10,2];}"));
		assertTrue(runTest("{L = [0,1,2]; L[1] = 10; L == [0,10,2];}"));
		assertTrue(runTest("{list[list[int]] L = [[0,1],[2,3]]; L[1][0] = 20; L == [[0,1],[20,3]];}"));
		assertTrue(runTest("{L = [[0,1],[2,3]]; L[1][0] = 20; L == [[0,1],[20,3]];}"));
	}
	
	@Test public void testSet() throws IOException {
		assertTrue(runTest("{set[int] S = {}; S == {};}"));
	}
}
