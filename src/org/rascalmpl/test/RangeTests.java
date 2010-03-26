package org.rascalmpl.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RangeTests extends TestFramework {


	@Test
	public void rangeInt() {
		assertTrue(runTest("{ [1..1] == [1]; }"));
		assertTrue(runTest("{ [1..2] == [1,2]; }"));
		assertTrue(runTest("{ [1..5] == [1,2,3,4,5]; }"));
		assertTrue(runTest("{ [1, 3..10] == [1, 3, 5, 7, 9 ]; }"));
		assertTrue(runTest("{ [1, -2 .. -5] == [1, -2, -5]; }"));
	}


	@Test
	public void rangeReals() {
		assertTrue(runTest("{ [1.0 .. .1] == []; }"));
		assertTrue(runTest("{ [1.0 .. 1.0] == [1.0]; }"));
		assertTrue(runTest("{ [1.0 .. 5.0] == [1.0, 2.0, 3.0, 4.0, 5.0]; }"));
		assertTrue(runTest("{ [1.0 .. 5.5] == [1.0, 2.0, 3.0, 4.0, 5.0]; }"));
		assertTrue(runTest("{ [1.0,1.5 .. 2.0] == [1.0, 1.5, 2.0]; }"));
		assertTrue(runTest("{ [1.0, -2.0 .. -10.0] == [1.0, -2.0, -5.0, -8.0]; }"));
	}
	
	@Test
	public void rangeMixed() {
		assertTrue(runTest("{ [1 .. .1] == []; }"));
		assertTrue(runTest("{ [1 .. 1.0] == []; }")); // is this desired?
		assertTrue(runTest("{ [1 .. 5.0] == [1, 2.0, 3.0, 4.0, 5.0]; }"));
		assertTrue(runTest("{ [1 .. 5.5] == [1, 2.0, 3.0, 4.0, 5.0]; }"));
		assertTrue(runTest("{ [1 ,1.5 .. 2.0] == [1.0, 1.5, 2.0]; }"));
		assertTrue(runTest("{ [1 ,1.5 .. 3] == [1.0, 1.5, 2.0, 2.5, 3.0]; }"));
		assertTrue(runTest("{ [1.0, -2 .. -10.0] == [1.0, -2.0, -5.0, -8.0]; }"));
		
	}

}
