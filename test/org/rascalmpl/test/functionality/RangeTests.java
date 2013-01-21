/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;

public class RangeTests extends TestFramework {

	// Note: all range tests have been adapted to reflect new semantics: last element is NOT included in the range anymore
	@Test
	public void rangeInt() {
		assertTrue(runTest("{ [1..1] == []; }"));
		assertTrue(runTest("{ [1..-1] == [1, 0]; }"));
		assertTrue(runTest("{ [1..0] == [1]; }"));
		assertTrue(runTest("{ [1..2] == [1]; }"));
		assertTrue(runTest("{ [1..5] == [1,2,3,4]; }"));
		assertTrue(runTest("{ [1, 3..10] == [1, 3, 5, 7, 9 ]; }"));
		assertTrue(runTest("{ [1, -2 .. -5] == [1, -2]; }"));
	}
	
	@Test
	public void rangeNum() {
		assertTrue(runTest("{num n1 = 1; [n1..n1] == []; }"));
		assertTrue(runTest("{num n1 = 1; num n2 = 2; [n1..n2] == [1]; }"));
		assertTrue(runTest("{num n1 = 1; num n5 = 5; [n1..n5] == [1,2,3,4]; }"));
		assertTrue(runTest("{num n1 = 1; num n3 = 3; num n10 = 10; [n1, n3..n10] == [1, 3, 5, 7, 9 ]; }"));
		assertTrue(runTest("{num n1 = 1; num nm2 = -2; num nm5 = -5; [n1, nm2 .. nm5] == [1, -2]; }"));
	}


	@Test
	public void rangeReals() {
		assertTrue(runTest("{ [1.0 .. .1] == [1.0]; }"));
		assertTrue(runTest("{ [1.0 .. 1.0] == []; }"));
		assertTrue(runTest("{ [1.0 .. 5.0] == [1.0, 2.0, 3.0, 4.0]; }"));
		assertTrue(runTest("{ [1.0 .. 5.5] == [1.0, 2.0, 3.0, 4.0, 5.0]; }"));
		assertTrue(runTest("{ [1.0,1.5 .. 2.0] == [1.0, 1.5]; }"));
		assertTrue(runTest("{ [1.0, -2.0 .. -10.0] == [1.0, -2.0, -5.0, -8.0]; }"));
	}
	
	@Test
	public void rangeMixed() {
		assertTrue(runTest("{ [1 .. .1] == [1]; }"));
		assertTrue(runTest("{ [1 .. 1.0] == []; }"));
		assertTrue(runTest("{ [1 .. 5.0] == [1, 2.0, 3.0, 4.0]; }"));
		assertTrue(runTest("{ [1 .. 5.5] == [1, 2.0, 3.0, 4.0, 5.0]; }"));
		assertTrue(runTest("{ [1 ,1.5 .. 2.0] == [1.0, 1.5]; }"));
		assertTrue(runTest("{ [1 ,1.5 .. 3] == [1.0, 1.5, 2.0, 2.5]; }"));
		assertTrue(runTest("{ [1.0, -2 .. -10.0] == [1.0, -2.0, -5.0, -8.0]; }"));
	}
	
	@Test
	public void aliased() {
		prepare("alias nat = int;");
		assertTrue(runTestInSameEvaluator("{ nat x = 0; nat y = 3; int i <- [x..y]; }"));
	}

}
