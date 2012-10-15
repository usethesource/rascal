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
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.test.infrastructure.TestFramework;

public class SubscriptTests extends TestFramework {

	@Test
	public void list() {

		assertTrue(runTest("[0,1,2,3][0] == 0;"));
		assertTrue(runTest("[0,1,2,3][1] == 1;"));
		assertTrue(runTest("[0,1,2,3][2] == 2;"));
		assertTrue(runTest("[0,1,2,3][3] == 3;"));

		assertTrue(runTest("{list[int] L = [0,1,2,3]; L[0] = 10; L == [10,1,2,3];}"));
		assertTrue(runTest("{list[int] L = [0,1,2,3]; L[1] = 11; L == [0,11,2,3];}"));
		assertTrue(runTest("{list[int] L = [0,1,2,3]; L[2] = 22; L == [0,1,22,3];}"));
		assertTrue(runTest("{list[int] L = [0,1,2,3]; L[3] = 33; L == [0,1,2,33];}"));
	}
	
	@Test(expected=Throw.class)
	public void listError1(){
		runTest("[0,1,2,3][4] == 3;");
	}
	
	@Test(expected=Throw.class)
	public void listError2(){
		runTest("{list[int] L = [0,1,2,3]; L[4] = 44; L == [0,1,2,3,44];}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedListVariable1(){
		runTest("{list[int] L; L[4];}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedListVariable2(){
		runTest("{list[int] L; L[4] = 44;}");
	}
	
	@Test(expected=StaticError.class)
	public void WrongListIndex1(){
		runTest("{list[int] L = [0,1,2,3]; L[\"abc\"];}");
	}
	
	@Test(expected=StaticError.class)
	public void WrongListIndex2(){
		runTest("{list[int] L = [0,1,2,3]; L[\"abc\"] = 44;}");
	}
	
	@Test(expected=StaticError.class)
	public void WrongListAssignment(){
		runTest("{list[int] L = [0,1,2,3]; L[2] = \"abc\";}");
	}

	@Test
	public void map() {
		assertTrue(runTest("(1:10, 2:20, 3:30)[1] == 10;"));
		assertTrue(runTest("(1:10, 2:20, 3:30)[2] == 20;"));
		assertTrue(runTest("(1:10, 2:20, 3:30)[3] == 30;"));

		// assertTruerunWithError("(1:10, 2:20, 3:30)[4] == 30;", "xxx"));

		assertTrue(runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[1] = 100; M == (1:100, 2:20, 3:30);}"));
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[2] = 200; M == (1:10, 2:200, 3:30);}"));
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[3] = 300; M == (1:10, 2:20, 3:300);}"));
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[4] = 400; M == (1:10, 2:20, 3:30, 4:400);}"));
	}
	
	@Test(expected=StaticError.class)
	public void WrongMapIndex1(){
		runTest("{map[int,int] M = (1:10,2:20); M[\"abc\"];}");
	}
	
	@Test(expected=StaticError.class)
	public void WrongMapIndex2(){
		runTest("{map[int,int] M  = (1:10,2:20); M[\"abc\"] = 3;}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedMapVariable1(){
		runTest("{map[int,int] M; M[4];}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedMapVariable2(){
		runTest("{map[int,int] M; M[4] = 44;}");
	}
	
	@Test(expected=StaticError.class)
	public void WrongMapAssignment(){
		runTest("{map[int,int] M = (1:10,2:20); M[2] = \"abc\";}");
	}

	@Test
	public void tuple() {
		assertTrue(runTest("<0, \"a\", 3.5>[0] == 0;"));
		assertTrue(runTest("<0, \"a\", 3.5>[1] == \"a\";"));
		assertTrue(runTest("<0, \"a\", 3.5>[2] == 3.5;"));
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedTupleVariable1(){
		runTest("{tuple[int,int] T; T[1];}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedTupleVariable2(){
		runTest("{tuple[int,int] T; T[1] = 10;}");
	}
	
	@Test(expected=Throw.class)
	public void tupleBoundsError(){
		runTest("<0, \"a\", 3.5>[3] == 3.5;");
	}
	
	@Test(expected=StaticError.class)
	public void tupleIndexError(){
		runTest("<0, \"a\", 3.5>[\"abc\"];");
	}
	
	@Test(expected=StaticError.class)
	public void tupleAssignmentError(){
		runTest("{T = <0, \"a\", 3.5>[\"abc\"]; T[1] = 3;}");
	}

	@Test
	public void relation() {
		assertTrue(runTest("{<1, \"a\">, <2, \"b\">}[0] == {};"));
		assertTrue(runTest("{<1, \"a\">, <2, \"b\">}[1] == {\"a\"};"));
		assertTrue(runTest("{<1, \"a\">, <2, \"b\">}[2] == {\"b\"};"));

		assertTrue(runTest("{<1, \"a\">, <2, \"b\">, <1, \"abc\">}[1] == {\"a\", \"abc\"};"));

		assertTrue(runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[0] == {};"));
		assertTrue(runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[1] == {<\"a\", 10>, <\"abc\", 100>};"));
		assertTrue(runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[2] == {<\"b\", 20>};"));
		assertTrue(runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[{1,2}] == {<\"a\", 10>, <\"b\", 20>, <\"abc\", 100>};"));
	
		assertTrue(runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[1,_] == {10, 100};"));
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedRelVariable1(){
		runTest("{rel[int,int] R; R[1];}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedRelVariable2(){
		runTest("{rel[int,int] R; R[1,2];}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedRelVariable3(){
		runTest("{rel[int,int] R; R[1] = 10;}");
	}

	@Test
	public void relationMultiIndex() {
		assertTrue(runTest("{<1,\"a\",1.0>,<2,\"b\",2.0>,<3,\"c\",3.0>}[0] == {};"));
		assertTrue(runTest("{<1,\"a\",1.0>,<2,\"b\",2.0>,<3,\"c\",3.0>}[1] == {<\"a\",1.0>};"));
		assertTrue(runTest("{<1,\"a\",1.0>,<2,\"b\",2.0>,<3,\"c\",3.0>}[2, \"b\"] == {2.0};"));

		assertTrue(runTest("{<1,10,10.5>, <2,20,20.5>, <3,20,30.5>, <2,10,100.5>}[{1},{10,20}] == {10.5};"));
	}

	@Test
	public void node() {

		prepare("data NODE = f(int a, str b, real c);");

		assertTrue(runTestInSameEvaluator("f(0, \"a\", 3.5)[0] == 0;"));
		assertTrue(runTestInSameEvaluator("f(0, \"a\", 3.5)[1] == \"a\";"));
		assertTrue(runTestInSameEvaluator("f(0, \"a\", 3.5)[2] == 3.5;"));
		assertTrue(runTestInSameEvaluator("{NODE T = f(0, \"a\", 3.5); T[0] = 10; T == f(10, \"a\", 3.5);}"));
	}
	
	@Test(expected=Throw.class)
	public void nodeBoundsError(){
		prepare("data NODE = f(int a, str b, real c);");
		
		runTestInSameEvaluator("f(0, \"a\", 3.5)[3] == 3.5;");
	}
	
	@Test(expected=StaticError.class)
	public void nodeIndexError(){
		prepare("data NODE = f(int a, str b, real c);");
		
		runTestInSameEvaluator("f(0, \"a\", 3.5)[\"abc\"];");
	}
	
	@Test(expected=StaticError.class)
	public void nodeAssignmentError(){
		prepare("data NODE = f(int a, str b, real c);");
		
		runTestInSameEvaluator("{NODE N = f(0, \"a\", 3.5); N.b = 3;}");
	}
}
