package org.rascalmpl.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.control_exceptions.Throw;

public class ProjectionTests extends TestFramework {

	@Test
	public void empty() {
		assertTrue(runTest("{}<0> == {};"));
		assertTrue(runTest("{}<1> == {};"));
	}
	
	@Test
	public void nonEmpty() {
		assertTrue(runTest("{<1,2>}<0> == {1}"));
		assertTrue(runTest("{<1,2>}<1> == {2}"));
	}
	
	@Test(expected=Throw.class)
	public void outOfBounds() {
		assertTrue(runTest("{<1,2>}<2> == {2}"));
	}
	
}
