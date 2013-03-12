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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;

public class BackTrackingTests extends TestFramework {
	
	@Test public void testSimple(){
		assertTrue(runTest("int i <- [1,4] && int k := i && k >= 4;"));
		assertTrue(runTest("int i <- [1,4] && int j <- [2,1] && int k := i + j && k >= 5;"));
	}
	
	@Test public void testList() {
		
		assertTrue(runTest("{([*list[int] L1, int N, *list[int] L2] := [1,2,3,4]) && (N == 3) && " + 
								"(L1 == [1,2]) && (N == 3) && (L2 == [4]);}"));
		
		assertTrue(runTest("{([*list[int] L1, int N, *list[int] L2] := [1,2,3,4]) && ((N == 3) || (N==4)) && " + 
		"(L1 == [1,2]) && (N == 3) && (L2 == [4]);}"));
		
		assertTrue(runTest("{([*list[int] L1, int N, *list[int] L2] := [1,2,3,4]) && " +
								"([*list[int] L3, int M, *list[int] L4] := [3,4]) && (N > M) && " + 
							  "(N == 4);}"));

		assertTrue(runTest("{[1, [*list[int] P, int N, *list[int] Q], 3] := [1, [1,2,3,2], 3];}"));
		assertTrue(runTest("{[*list[int] P, int N, *list[int] Q]:= [1,2,3,2] && N > 1;}"));
		
		assertTrue(runTest("{[1, [*list[int] P, int N, *list[int] Q], 3] := [1, [10,20], 3] && N > 10;}"));
		
	}
	
	@Test public void testSet() {
		
		assertTrue(runTest("{({*set[int] S1, int N} := {1,2,3,4}) && (N == 1);}"));
		assertTrue(runTest("{({*set[int] S1, int N} := {1,2,3,4}) && (N == 2);}"));
		assertTrue(runTest("{({*set[int] S1, int N} := {1,2,3,4}) && (N == 3);}"));
		assertTrue(runTest("{({*set[int] S1, int N} := {1,2,3,4}) && (N == 4);}"));

		
		assertTrue(runTest("{({*set[int] S1, int N, *set[int] S2} := {1,2,3,4}) && (N == 1);}"));
		assertTrue(runTest("{({*set[int] S1, int N, *set[int] S2} := {1,2,3,4}) && (N == 2);}"));
		assertTrue(runTest("{({*set[int] S1, int N, *set[int] S2} := {1,2,3,4}) && (N == 3);}"));
		assertTrue(runTest("{({*set[int] S1, int N, *set[int] S2} := {1,2,3,4}) && (N == 4);}"));
		
		assertTrue(runTest("{{1, {*set[int] S1, int N}, 3} := {1, {1,2,3}, 3};}"));
		assertTrue(runTest("{{*set[int] S1, int N}:= {1,2,3,2} && N == 1;}"));
		assertTrue(runTest("{{*set[int] S1, int N}:= {1,2,3,2} && N == 2;}"));
		assertTrue(runTest("{{*set[int] S1, int N}:= {1,2,3,2} && N == 3;}"));
		
		assertTrue(runTest("{{1, {*set[int] S1, int N, *set[int] S2}, 3} := {1, {1,2,3}, 3};}"));
//		assertTrue(runTest("{{1, {*set[int] S1, int N, *set[int] S2}, 3} := {1, {1,2,3}, 3} && N == 1;}"));
//		assertTrue(runTest("{{1, {*set[int] S1, int N, *set[int] S2}, 3} := {1, {1,2,3}, 3} && N == 2;}"));
//		assertTrue(runTest("{{1, {*set[int] S1, int N, *set[int] S2}, 3} := {1, {1,2,3}, 3} && N == 3;}"));
	}
	
	@Test public void and(){
		assertTrue(runTest("int i <- [0, 1] && [\"a\",\"b\"][i] == \"a\";"));
		assertTrue(runTest("int i <- [0, 1] && [\"a\",\"b\"][i] == \"b\";"));
	}
}
