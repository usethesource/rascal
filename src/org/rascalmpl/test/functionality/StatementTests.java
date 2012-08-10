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
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError;
import org.rascalmpl.test.infrastructure.TestFramework;

public class StatementTests extends TestFramework {

	@Test
	public void testAssert() {
		assertTrue(runTest("assert 3 > 2;"));
		assertTrue(runTest("assert (3 > 2): \"Yes assert succeeds\";"));
	}
	
	@Test(expected=Throw.class)
	public void assertError1() {
		runTest("assert 1 == 2;");
	}
	
	@Test(expected=StaticError.class)
	public void assertError2() {
		runTest("assert 3.5;");
	}
	
	@Test(expected=StaticError.class)
	public void assertError3() {
		runTest("assert 3.5 : \"Wrong expression type\";");
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void assertError4() {
		runTest("assert X;");
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void assertError5() {
		runTest("assert X : \"Wrong expression type\";");
	}
	

	@Test
	public void assignment() {
		assertTrue(runTest("{int x = 3; x == 3;}"));
		assertTrue(runTest("{int x = 3; x = 4; x == 4;}"));
		assertTrue(runTest("{<x, y> = <3, 4>; (x == 3) && (y == 4);}"));
		assertTrue(runTest("{<x, y, z> = <3, 4, 5>; (x == 3) && (y == 4) && (z == 5);}"));
		assertTrue(runTest("{<x, y> = <3, 4>; x = 5; (x == 5) && (y == 4);}"));

		assertTrue(runTest("{int x = 3; x += 2; x == 5;}"));
		assertTrue(runTest("{int x = 3; x -= 2; x == 1;}"));
		assertTrue(runTest("{int x = 3; x *= 2; x == 6;}"));
		assertTrue(runTest("{int x = 3; x /= 2; x == 1;}"));
		
		assertTrue(runTest("{list[int] x = [0,1,2]; x == [0,1,2];}"));
		assertTrue(runTest("{list[int] x = [0,1,2]; x[0] == 0;}"));
		assertTrue(runTest("{list[int] x = [0,1,2]; x[1] == 1;}"));
		assertTrue(runTest("{list[int] x = [0,1,2]; x[2] == 2;}"));
		assertTrue(runTest("{list[int] x = [0,1,2]; x[1] = 10; (x[0] == 0) && (x[1] == 10) && (x[2] == 2);}"));

		assertTrue(runTest("{map[int,int] x = (0:0,1:10,2:20); x == (0:0,1:10,2:20);}"));
		assertTrue(runTest("{map[int,int] x = (0:0,1:10,2:20); x[1] = 15; (x[0] == 0) && (x[1] == 15) && (x[2] == 20);}"));

		assertTrue(runTest("{set[int] x = {0,1,2}; x == {0,1,2};}"));
		assertTrue(runTest("{set[int] x = {0,1,2}; x = x + {3,4}; x == {0,1,2, 3,4};}"));

		assertTrue(runTest("{rel[str,list[int]] s = {<\"a\", [1,2]>, <\"b\", []>, <\"c\", [4,5,6]>}; s != {};}"));
		assertTrue(runTest("{rel[str,list[int]] s = {<\"a\", [1,2]>, <\"b\", []>, <\"c\", [4,5,6]>}; s != {};}"));
	}

	@Test
	public void block() {
		assertTrue(runTest("{int x = 3; x = 4; x ==4;}"));
		assertTrue(runTest("{int x = 3; x = 4; x;} == 4"));
	}

	@Test
	public void testBreak() {
		// assertTrue(runTest("{int n = 0; while(n < 10){ n = n + 1; break;}; n == 1;};"));
	}

	@Test
	public void testContinue() {
	}

	@Test
	public void doWhile() {
		assertTrue(runTest("{int n = 0; m = 2; do {m = m * m; n = n + 1;} while (n < 1); (n == 1) && (m == 4);}"));
		assertTrue(runTest("{int n = 0; m = 2; do {m = m * m; n = n + 1;} while (n < 3); m == 256;}"));
	}
	
	@Test(expected=StaticError.class)
	public void doWhileError() {
		runTest("do {n = 4;} while(3);");
	}
	
	@Test
	public void testWhile() {
		assertTrue(runTest("{int n = 0; int m = 2; while(n != 0){ m = m * m;}; (n == 0)&& (m == 2);}"));
		assertTrue(runTest("{int n = 0; int m = 2; while(n < 3){ m = m * m; n = n + 1;}; (n ==3) && (m == 256);}"));
	}
	
	@Test(expected=StaticError.class)
	public void whileError() {
		runTest("while(3){n = 4;}");
	}

	@Test
	public void fail() {
		prepare("data X = x(int i) | x();");
		prepareMore("X x(int i) { if (i % 2 == 0) fail x; else return x(); }");
		assertTrue(runTestInSameEvaluator("x(2) := x(2)"));
		assertTrue(runTestInSameEvaluator("x(3) == x()"));
		
	}

	@Test
	public void testFor() {
		assertTrue(runTest("{int n = 0; for(int i <- [1,2,3,4]){ n = n + i;} n == 10;}"));
		assertTrue(runTest("{int n = 0; for(int i <- [1,2,3,4], n <= 3){ n = n + i;} n == 6;}"));
		assertTrue(runTest("{int n = 0; for(int i <- [1,2,3,4]){ n = n + 1; if (n == 3) break; } n == 3;}"));
		assertTrue(runTest("{int n = 0; for(int i <- [1,2,3,4], n <= 3){ if (n == 3) continue; n = n + 1; } n == 3;}"));
		assertTrue(runTest("{int n = 0; loop:for(int i <- [1,2,3,4], n <= 3){ if (n == 3) fail loop; n = n + 1; } n == 3;}"));
	}
	
	@Test
	public void testAppend(){
		//assertTrue(runTest("for(int i <- [1,2,3,4]){ 3 * i; } == 12;"));
		assertTrue(runTest("{ L = for(int i <- [1,2,3,4]){ append 3 * i; }; L == [3,6,9,12];}"));
		assertTrue(runTest("{ L = for(int i <- [1,2,3,4]){ append 3 * i; append 4 *i;}; L == [3,4,6,8,9,12,12,16];}"));
	}

	@Test
	public void ifThen() {
		assertTrue(runTest("{int n = 10; if(n < 10){n = n - 4;} n == 10;}"));
		assertTrue(runTest("{int n = 10; if(n < 15){n = n - 4;} n == 6;}"));
		assertTrue(runTest("{int n = 10; l:if(int i <- [1,2,3]){ if (i % 2 != 0) { n = n + 4; fail l; } n = n - 4;} n == 10;}"));
	}
	

	@Test(expected=StaticError.class)
	public void ifThenError() {
		runTest("if(3){n = 4;}");
	}
	
	@Test
	public void ifThenElse() {
		assertTrue(runTest("{int n = 10; if(n < 10){n = n - 4;} else { n = n + 4;} n == 14;}"));
		assertTrue(runTest("{int n = 12; if(n < 10){n = n - 4;} else { n = n + 4;} n == 16;}"));
	}
	
	@Test(expected=StaticError.class)
	public void ifThenElseError() {
		runTest("if(\"abc\"){n = 4;} else {n=5;}");
	}

	@Test
	public void testSwitch() {
		assertTrue(runTest("{int n = 0; switch(2){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} n == 2;}"));
		assertTrue(runTest("{int n = 0; switch(4){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} n == 4;}"));
		assertTrue(runTest("{int n = 0; switch(6){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} n == 6;}"));
		assertTrue(runTest("{int n = 0; switch(8){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} n == 10;}"));
	}
	
	@Test
	public void solve(){
		String S = 	"rel[int,int] R1 =  {<1,2>, <2,3>, <3,4>};" +
	                " rel[int,int] T = R1;" +
	                " solve (T)  T = T + (T o R1);";
		assertTrue(runTest("{" + S + " T =={<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>};}"));
	}
	
	@Test(expected=StaticError.class)
	public void solveError1(){
		String S = 	"rel[int,int] R1 =  {<1,2>, <2,3>, <3,4>};" +
	                " rel[int,int] T = R1;" +
	                " solve (T; true)   T = T + (T o R1);";
		assertTrue(runTest("{" + S + " T =={<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>};}"));
	}
	
	@Test(expected=Throw.class)
	public void solveError2(){
		String S = 	"rel[int,int] R1 =  {<1,2>, <2,3>, <3,4>};" +
	                " rel[int,int] T = R1;" +
	                " solve (T; -1)   T = T + (T o R1);";
		assertTrue(runTest("{" + S + " T =={<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>};}"));
	}
	
	@Test
	public void solveMaximumUnboundedBug888() {
		prepare("int j = 0;");
		prepareMore("solve (j) if (j < 100000) j += 1;");
		assertTrue(runTestInSameEvaluator("j == 100000;"));
	}
	
}

