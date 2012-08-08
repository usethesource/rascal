/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;

public class DeltaListPatternTests extends TestFramework {
	
	/*
	 * Warning: these tests only work with a fixed delta=2 in AbstractPatternList
	 */
	
	@Test
	public void matchListDelta2() {
		

		assertTrue(runTest("[] := [];"));
		assertTrue(runTest("[1] := [1];"));
		assertTrue(runTest("[1,true,2] := [1,false,2];"));
		assertFalse(runTest("[1,true,2] := [1,false,3];"));
		

		assertTrue(runTest("([int N] := [1]) && (N == 1);"));
		assertTrue(runTest("[ _ ] := [1];"));
		
		assertTrue(runTest("([int N, true, 2, true, int M] := [1,false,2,false,3]) && (N == 1) && (M==3);"));
		assertTrue(runTest("[ _, true,2,true, _] := [1,false,2,false,3];"));
		
		assertTrue(runTest("([int N, true,2, true,N] := [1,false,2,false,1]) && (N == 1);"));
		
		assertFalse(runTest("([int N, true,2,true, N] := [1,false,2,false,3]);"));
		assertFalse(runTest("([int N, true,2, true,N] := [1,false,2,false,\"a\"]);"));
		
		assertTrue(runTest("{int N = 1; ([N, true,2,true, int M] := [1,false,2,false,3]) && (N == 1) && (M==3);}"));
		assertFalse(runTest("{int N = 1; ([N, true,2,true int M] := [4,false,2,false,3]);}"));
		
		assertTrue(runTest("{list[int] L = [3]; [1,true,2,true,L] := [1,false,2,false,3];}"));
		assertTrue(runTest("{L = [2, true, 3]; [1, true, L] := [1,false,2,false,3];}"));
		assertTrue(runTest("{L = [2, false, 3]; [1, true, L] := [1,false,2,false,3];}"));

		assertTrue(runTest("[1, true, [2, true, 3], true, 4] := [1, false, [2, false, 3], false, 4];"));
		assertFalse(runTest("[1, true, [2, true, 3], true, 4] := [1, false, [2, false, 3, false, 4], false, 4];"));

		assertTrue(runTest("([list[int] L] := []) && (L == []);"));
		assertTrue(runTest("{ list[int] X = []; ([list[int] L] := X) && (L == []); }"));
		assertTrue(runTest("([list[int] L] := ([1] - [1])) && (L == []);"));
		assertTrue(runTest("([list[int] L] := [1]) && (L == [1]);"));
		assertTrue(runTest("([L*] := [1,true,2]) && (L == [1,true,2]);"));

		assertTrue(runTest("([1, true, L*] := [1]) && (L == []);"));
		assertTrue(runTest("([1, true, L*] := [1, false, 2]) && (L == [2]);"));
		assertTrue(runTest("([1, true, L*] := [1, false, 2, false, 3]) && (L == [2, false, 3]);"));

		assertTrue(runTest("([L*, true, 10] := [10]) && (L == []);"));
		assertTrue(runTest("([L*, true, 10] := [1,false,10]) && (L == [1]);"));
		assertTrue(runTest("([L*, true, 10] := [1,false,2,false,10]) && (L == [1,false,2]);"));

		assertTrue(runTest("([1, true, L*, true, 10] := [1,false,10]) && (L == []);"));
		assertTrue(runTest("([1, true, L*, true, 10] := [1,false,2,false,10]) && (L == [2]);"));
		assertTrue(runTest("([1, true, L*, true, 10, true, M*, true, 20] := [1,false,10,false,20]) && (L == []) && (M == []);"));
		assertTrue(runTest("([1, true, L*, true, 10, true, M*, true, 20] := [1,false,2,false,10,false,20]) && (L == [2]) && (M == []);"));
		assertTrue(runTest("([1, true, L*, true, 10, true, M*, true, 20] := [1,false,2,false,10,false,3,false,20]) && (L == [2]) && (M==[3]);"));
		assertTrue(runTest("([1, true, L*, true, 10, true, M*, true, 20] := [1,false,2,false,3,false,10,false,4,false,5,false,20]) && (L == [2,false,3]) && (M==[4,false,5]);"));
		
		assertTrue(runTest("([1, true, L*, true, 10, true, L, true, 20] := [1,false,2,false,3,false,10,false,2,false,3,false,20]) && (L == [2,false,3]);"));
		
	}
	
	
}
