/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.test.infrastructure.TestFramework;


public class AssignmentTests extends TestFramework {
	
	@Test(expected=StaticError.class)
	public void testUninit() {
		runTest("zzz;");
	}
	
	@Test(expected=StaticError.class)
	public void assignmentError1() {
		runTest("{int n = 3; n = true;}");
	}

	@Test(expected=StaticError.class)
	public void assignmentError2() {
		runTest("{int i = true;}");
	}
	
	@Test(expected=StaticError.class)
	public void assignmentError3() {
		runTest("int i = true;");
	}

	@Test(expected=StaticError.class)
	public void assignmentError4() {
		runTest("{int n = 3; n = true;}");
	}
	
	@Test public void testSimple() {
		
		assertTrue(runTest("{bool b = true; b == true;}"));
		assertTrue(runTest("{b = true; b == true;}"));
	}
	
	@Test
	public void testInteger(){
		assertTrue(runTest("{int N = 3; N += 2; N==5;}"));
		assertTrue(runTest("{int N = 3; N -= 2; N==1;}"));
		assertTrue(runTest("{int N = 3; N *= 2; N==6;}"));
		assertTrue(runTest("{int N = 6; N /= 2; N==3;}"));
		assertTrue(runTest("{int N = 6; N ?= 2; N==6;}"));
		assertTrue(runTest("{           N ?= 2; N==2;}"));
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void integerError1(){
		runTest("N += 2;");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void integerError2(){
		runTest("N -= 2;");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void integerError3(){
		runTest("N *= 2;");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void integerError4(){
		runTest("N /= 2;");
	}
	
	@Test public void testTuple() {
		assertTrue(runTest("{int a = 1; int b = 2; <a, b> = <b, a>; (a == 2) && (b == 1);}"));
		assertTrue(runTest("{<a, b> = <1, 2>; (a == 1) && (b == 2);}"));
		assertTrue(runTest("{tuple[str name, int cnt] T = <\"abc\", 1>; T.name = \"def\"; T.name == \"def\";}"));
		assertTrue(runTest("{tuple[str name, int cnt] T = <\"abc\", 1>; T[name = \"def\"] == <\"def\", 1>;}"));
	}
	
	@Test public void testList1() {
		assertTrue(runTest("{list[int] L = []; L == [];}"));
		assertTrue(runTest("{list[int] L = [0,1,2]; L[1] = 10; L == [0,10,2];}"));
		assertTrue(runTest("{L = [0,1,2]; L[1] = 10; L == [0,10,2];}"));
		assertTrue(runTest("{list[list[int]] L = [[0,1],[2,3]]; L[1][0] = 20; L == [[0,1],[20,3]];}"));
		assertTrue(runTest("{L = [[0,1],[2,3]]; L[1][0] = 20; L == [[0,1],[20,3]];}"));
		
		assertTrue(runTest("{list[int] L = [1,2,3]; L += [4]; L==[1,2,3,4];}"));
		assertTrue(runTest("{list[int] L = [1,2,3]; L -= [2]; L==[1,3];}"));
		assertTrue(runTest("{list[int] L = [1,2,3]; L ?= [4]; L==[1,2,3];}"));
		assertTrue(runTest("{                       L ?= [4]; L==[4];}"));
	}
	
	@Test public void testList2() {
		assertTrue(runTest("{list[list[int]] L = [[1,2,3],[10,20,30]]; L[0] += [4]; L==[[1,2,3,4],[10,20,30]];}"));
		assertTrue(runTest("{list[list[int]] L = [[1,2,3],[10,20,30]]; L[0] -= [2]; L==[[1,3],[10,20,30]];}"));
		assertTrue(runTest("{list[list[int]] L = [[1,2,3],[10,20,30]]; L[0] ?= [4]; L==[[1,2,3],[10,20,30]];}"));
	}
	
	@Test(expected=UnexpectedTypeError.class)
	public void errorList(){
		assertTrue(runTest("{list[int] L = {1,2,3}; L *= [4]; L==[<1,4>,<2,4>,<3,4>];}"));
	}
	
	@Test public void testMap1() {
		assertTrue(runTest("{map[int,int] M = (); M == ();}"));
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20); M == (1:10, 2:20);}"));
		
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20); M += (3:30); M==(1:10, 2:20,3:30);}"));
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20); M -= (2:20); M==(1:10);}"));
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20); M ?= (3:30); M==(1:10, 2:20);}"));
		assertTrue(runTest("{                               M ?= (3:30); M==(3:30);}"));
	}
	
	@Test(expected=UnexpectedTypeError.class)
	public void errorMap(){
		assertTrue(runTest("{map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] *= [4]; M==(0:[<1,4>,<2,4>,<3,4>],1:[10,20,30]);}"));

	}
	
	@Test public void testMap2() {
		assertTrue(runTest("{map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] += [4]; M==(0:[1,2,3,4],1:[10,20,30]);}"));
		assertTrue(runTest("{map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] -= [2]; M==(0:[1,3],1:[10,20,30]);}"));
		assertTrue(runTest("{map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] ?= [4]; M==(0:[1,2,3],1:[10,20,30]);}"));
		assertTrue(runTest("{map[int, list[int]] M = (0:[1,2,3],1:[10,20,30]); M[2] ?= [4]; M==(0:[1,2,3],1:[10,20,30], 2:[4]);}"));
		
	}
	
	@Test public void testSet() {
		assertTrue(runTest("{set[int] S = {}; S == {};}"));
		assertTrue(runTest("{set[int] S = {0,1,2}; S == {0, 1, 2};}"));
		
		assertTrue(runTest("{set[int] L = {1,2,3}; L += {4}; L=={1,2,3,4};}"));
		assertTrue(runTest("{set[int] L = {1,2,3}; L -= {2}; L=={1,3};}"));
		assertTrue(runTest("{set[int] L = {1,2,3}; L ?= {4}; L=={1,2,3};}"));
		assertTrue(runTest("{                       L ?= {4}; L=={4};}"));
	}
	
	@Test(expected=UnexpectedTypeError.class)
	public void errorSet(){
		assertTrue(runTest("{set[int] L = {1,2,3}; L *= {4}; L=={<1,4>,<2,4>,<3,4>};}"));
	}
	
	@Test public void testADT(){
		
		prepare("data D = listfield(list[int] ints) | intfield(int i);");

		assertTrue(runTestInSameEvaluator("{D d = listfield([1,2]); d.ints += [3]; d == listfield([1,2,3]);}"));
		assertTrue(runTestInSameEvaluator("{D d = listfield([1,2]); d.ints -= [2]; d == listfield([1]);}"));
		
		assertTrue(runTestInSameEvaluator("{D d = intfield(2); d.i += 3; d == intfield(5);}"));
		assertTrue(runTestInSameEvaluator("{D d = intfield(5); d.i -= 3; d == intfield(2);}"));
		assertTrue(runTestInSameEvaluator("{D d = intfield(5); d.i *= 3; d == intfield(15);}"));
		assertTrue(runTestInSameEvaluator("{D d = intfield(6); d.i /= 3; d == intfield(2);}"));
	}
	
	@Test
	public void testAnnotations(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		
		assertTrue(runTestInSameEvaluator("{F X = f(); X @ pos = 1; X @ pos == 1;}"));
		assertTrue(runTestInSameEvaluator("{F X = f(); X @ pos = 2; X @ pos += 3;  X @ pos == 5;}"));
		assertTrue(runTestInSameEvaluator("{F X = f(); X @ pos = 3; X @ pos -= 2;  X @ pos == 1;}"));
		assertTrue(runTestInSameEvaluator("{F X = f(); X @ pos = 2; X @ pos *= 3;  X @ pos == 6;}"));
		assertTrue(runTestInSameEvaluator("{F X = f(); X @ pos = 6; X @ pos /= 3;  X @ pos == 2;}"));
		assertTrue(runTestInSameEvaluator("{F X = f(); X @ pos = 6; X @ pos ?= 3;  X @ pos == 6;}"));
		assertTrue(runTestInSameEvaluator("{F X = f();              X @ pos ?= 3;  X @ pos == 3;}"));
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void annotationError1(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("X @ pos = 1;");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void annotationError2(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("X @ pos += 1;");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void annotationError3(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("X @ pos -= 1;");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void annotationError4(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("X @ pos *= 1;");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void annotationError5(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("X @ pos /= 1;");
	}
	
	@Test
	public void assigningClosureToVariableBug877() {
		prepare("bool() x = bool(){ return true; };");
		assertTrue(runTestInSameEvaluator("x() == true;"));
	}
	
}	
	
