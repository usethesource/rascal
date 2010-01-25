package org.meta_environment.rascal.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class ReducerTests extends TestFramework {
	@Test
	public void testCount() {
		assertTrue(runTest("( 0 | it + 1 | x <- [1,2,3] ) == 3"));
	}

	@Test
	public void testMax() {
		assertTrue(runTest("( 0 | x > it ? x : it | x <- [1,2,3] ) == 3"));
	}
	
	@Test
	public void testSum() {
		assertTrue(runTest("( 0 | it + x  | x <- [1,2,3] ) == 6"));
	}

	@Test
	public void testFlatMap() {
		assertTrue(runTest("( {} | it + x  | x <- {{1,2}, {2,3,4}} ) == {1,2,3,4}"));
	}
}
