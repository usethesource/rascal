/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;

public class FunctionCompositionTests extends TestFramework {
	
	/*
	 * Tests of the 'o' composition operator
	 */
	
	String fib1 = 
			" public int fib(0) = 0; "; 
	String fib2 =
			" public int fib(1) = 1; ";
	String fib3 =
			" public default int fib(int n) = fib(n-1) + fib(n-2); \n";

	String fact1 = 
			" public int fact(0) = 1; ";
	String fact2 =
			" public int fact(1) = 1; ";
	String fact3 =
			" public default int fact(int n) = n*fact(n-1); ";

	String printResult1 =
			" public str printResult(int n) = \" <n>; \"; ";
	String printResult2 =
			" public str printResult(str s) = s + s; ";
	
	String f1 = 
			" public int f(0) = 0; ";
	String f2 =
			" public int f(1) = 1; ";
	String f3 =
			" public default int f(int n) = n + 1; ";
	
	String g1 = 
			" public int g(0) { fail; } ";
	String g2 =
			" public int g(1) = 1; ";
	String g3 =
			" public default int g(int n) = n + 2; ";
	
	@Test
	public void testFactorialFibonacci() {
		prepare(fib1);
		prepareMore(fib2);
		prepareMore(fib3);
		prepareMore(fact1);
		prepareMore(fact2);
		prepareMore(fact3);
		String test =
				" { " +
				" list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; " +
				" list[int] outputs1 = [ fact(fib(i)) | int i <- inputs ]; " +
				" list[int] outputs2 = [ (fact o fib)(i) | int i <- inputs ]; " +
				" outputs1 == outputs2; " +
				" } ";
		assertTrue(runTestInSameEvaluator(test));
	}
	
	@Test
	public void testFactorialFibonacciPrint() {
		prepare(fib1);
		prepareMore(fib2);
		prepareMore(fib3);
		prepareMore(fact1);
		prepareMore(fact2);
		prepareMore(fact3);
		prepareMore(printResult1);
		prepareMore(printResult2);
		String test1 = 
				" { " +
				" list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; " +
				" list[str] outputs1 = [ printResult(fact(fib(i))) | int i <- inputs ]; " +
				" list[str] outputs2 = [ (printResult o fact o fib)(i) | int i <- inputs ]; " +
				// associativity check of the 'o' operator
				" list[str] outputs3 = [ ( (printResult o fact) o fib)(i) | int i <- inputs ]; " +
				" list[str] outputs4 = [ (printResult o (fact o fib))(i) | int i <- inputs ]; " +	
				" (outputs1 == outputs2) && (outputs1 == outputs3) && (outputs1 == outputs4); " +
				" } "; 
		
		String test2 =
				" { " +
				" list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; " +
				" list[str] outputs1 = [ printResult(printResult(fact(fib(i)))) | int i <- inputs ]; " +
				" list[str] outputs2 = [ (str (str s) { return s + s; } o printResult o fact o fib)(i) | int i <- inputs ]; " +
				// associativity check of the 'o' operator
				" list[str] outputs3 = [ ( (printResult o printResult) o (fact o fib) )(i) | int i <- inputs ]; " +
				" list[str] outputs4 = [ ( printResult o (str (int n) { return \" <n>; \"; } o fact) o fib )(i) | int i <- inputs ]; " +
				" (outputs1 == outputs2) && (outputs1 == outputs3) && (outputs1 == outputs4); " +
				" } ";
		
		assertTrue(runTestInSameEvaluator(test1));
		assertTrue(runTestInSameEvaluator(test2));
	}
	
	@Test
	public void testAnonymousFunctionComposition() {
		String test =
				" { " +
				" list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; " +
				" list[int] outputs1 = [ int (int n) { switch(n) { case 0: return 1; case 1: return 1; case int n: return n*(n-1); } } " + 
				" 							( int (int n) { switch(n) { case 0: return 0; case 1: return 1; case int n: return (n-1) + (n-2); } } " + 
				"								(i)) | int i <- inputs ]; " +
				" list[int] outputs2 = [ (int (int n) { switch(n) { case 0: return 1; case 1: return 1; case int n: return n*(n-1); } } " +
				"							o int (int n) { switch(n) { case 0: return 0; case 1: return 1; case int n: return (n-1) + (n-2); } }) " +
				"						(i) | int i <- inputs ]; " +
				" outputs1 == outputs2; " +
				" } ";
		
		assertTrue(runTest(test));
	}
	
	@Test
	public void testComposedOverloadedFunctions() {
		prepare(f1);
		prepareMore(f2);
		prepareMore(f3);
		prepareMore(g1);
		prepareMore(g2);
		prepareMore(g3);
		String test =
				" { " +
				" (g o f)(0) == 2; " +
				" } ";
		assertTrue(runTestInSameEvaluator(test));
	}
	
	/*
	 * Tests of the '+' composition operator
	 */
	
	String h1 =
			" public str h(0) = \"0\"; ";
	String h2 =
			" public str h(1) = \"1\"; ";
	String h3 =
			" public default str h(int n) { fail; } ";

	String i1 =
			" public str i(0) = \"1\"; ";
	String i2 =
			" public str i(1) = \"2\"; ";
	String i3 =
			" public default str i(int n) = \"<n + 1>\"; ";
	
	String j0 = 
			" public int j0(0) = 0;";
	String j1 =
			" public int j1(1) = 1; ";
	
	String j3 = 
			" public default int j3(int n) = 2*n; ";
	
	String j4 = 
			" public default int j4(int n) = 2*n - 1; ";
		
	String k = 
			" public int k(int n) = (n%2 == 0) ? { fail; } : 2*n; ";
	String l = 
			" public int l(int n) = (n%2 == 0) ? n*(n-1) : { fail; }; ";
	
	@Test
	public void testNonDeterministicChoiceAndNormalComposition() {
		prepare(h1);
		prepareMore(h2);
		prepareMore(h3);
		prepareMore(i1);
		prepareMore(i2);
		prepareMore(i3);
		prepareMore(j0);
		prepareMore(j1);
		prepareMore(j3);
		prepareMore(j4);
		prepareMore(k);
		prepareMore(l);
		String test1 = 
				" { " +
				" list[int] inputs = [2,3];" +
				" list[str] outputs1 = [ i(n) | int n <- inputs ]; " +
				" list[str] outputs2 = [ (h + i)(n) | int n <- inputs ]; " +
				" list[str] outputs3 = [ (i + h)(n) | int n <- inputs ]; " +
				" outputs1 == outputs2 && outputs1 == outputs3 && " +
				" ( (h + i)(0) == \"0\" || (h + i)(0) == \"1\" ) &&" +
				" ( (h + i)(1) == \"1\" || (h + i)(1) == \"2\" ) &&" +
				" ( (i + h)(0) == \"0\" || (i + h)(0) == \"1\" ) &&" +
				" ( (i + h)(1) == \"1\" || (i + h)(1) == \"2\" ); " +
				" } ";
		String test2 =
				" { " +
				" list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10]; " +
				" list[int] outputs = [ (n%2 == 0) ? n*(n - 1) : 2*n | int n <- inputs ]; " +
				" list[int] outputs1 = [ (k + l)(n) | int n <- inputs ]; " +
				" list[int] outputs2 = [ (l + k)(n) | int n <- inputs ]; " +
				" list[int] outputs3 = [ ( (k + l) o (l + k) )(n) | int n <- inputs ]; " +
				" list[int] outputs4 = [ n*(n - 1) | int n <- outputs ]; " +
				" list[int] outputs5 = [ (j0 + j1 + (k + l) o j3)(n) | int n <- inputs ]; " +
				" list[int] outputs6 = [ ((k + l) o j4 + j0 + j1)(n) | int n <- inputs ]; " +
				" list[int] outputs7 = [0,1] + [ 2*n*(2*n - 1) | int n <- inputs - [0,1] ]; " +
				" list[int] outputs8 = [0,1] + [ 2*(2*n-1) | int n <- inputs - [0,1] ]; " +
				" list[int] outputs9 = [ 2*n*(2*n - 1) | int n <- inputs ]; " +
				" list[int] outputs10 = [ 2*(2*n-1) | int n <- inputs ]; " +
				" list[int] outputs11 = [ (( int (int n) { return (n%2 == 0) ? { fail; } : 2*n; } + l) o (int (int n) { return 2*n - 1; }) + j0 + j1)(n) | int n <- inputs ]; " +
				" outputs == outputs1 " +
				" && outputs == outputs2 " +
				" && outputs3 == outputs4 " +
				" && ( outputs5 == outputs7 || outputs5 == outputs9 ) " +
				" && ( outputs6 == outputs8 || outputs6 == outputs10 ) " +
				" && ( outputs11 == outputs8 || outputs11 == outputs10 ); " +
				
				" } ";
		
		assertTrue(runTestInSameEvaluator(test1));
		assertTrue(runTestInSameEvaluator(test2));
	}


}
