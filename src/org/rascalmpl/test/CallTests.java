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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.ArgumentsMismatchError;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperationError;


public class CallTests extends TestFramework{
	
	
	@Test(expected=UndeclaredVariableError.class)
	public void callError1() {
		runTest("zap(1,2);");
	}
	
	@Test
	public void qualifiedNameType() {
		prepareModule("M", "module M\n" +
		         "data X = x();");

		prepareMore("import M;");
		prepareMore("M::X f() { return x(); }");
		assertTrue(runTestInSameEvaluator("f() == x();"));
	}
	@Test(expected=ArgumentsMismatchError.class)
	public void callError2() {
		runTest("{ int f(int n) {return 2*n;}  f(\"abc\");}");
	}
	
	@Test(expected=UndeclaredModuleError.class)
	public void callError3() {
		runTest("zip::zap(1,2);");
	}
	
	@Test(expected=UnsupportedOperationError.class)
	public void callError4() {
		runTest("{zap = 10; zap(1,2);}");
	}
	
	@Test(expected=StaticError.class)
	public void callError5() {
		runTest("{ int f(){return \"a\";} f();}");
	}
	
	@Test(expected=StaticError.class)
	public void callError6() {
		runTest("{ int f(){ } f();}");
	}
	
	@Ignore("can't check this anymore due to pattern dispatch") @Test(expected=StaticError.class)
	public void callError7() {
		runTest("{ int f(int n) {return \"a\";}  int f(value v) {return \"a\";} }");
	}
	
	@Test(expected=StaticError.class)
	public void callError8() {
		runTest("{ int f(int n) {return n;} f(undef);}");
	}

	
	@Test
	public void voidFun() {
		assertTrue(runTest("{ void f(){ } f(); true;}"));
	}
	
	@Test public void fac() {
		String fac = "int fac(int n){ return (n <= 0) ? 1 : (n * fac(n - 1));}";
		
		assertTrue(runTest("{" + fac + " fac(0) == 1;}"));
		//assertTrue(tf.runTest("{ public " + fac + " fac(0) == 1;}"));
		//assertTrue(tf.runTest("{ private " + fac + " fac(0) == 1;}"));
		
		assertTrue(runTest("{" +  fac + " fac(1) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	@Test public void facNotTailRec() {
		
		String fac = "int fac(int n) { if (n == 0) { return 1; } int z = fac(n - 1); return z * n; }";
		
		assertTrue(runTest("{" + fac + " fac(0) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(1) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	@Test public void formalsAreLocal() {
		
		String fac = "int fac(int n) { if (n == 0) { return 1; } int z = n; int m = fac(n - 1); return z * m; }";
		// "m" used to be "n", but now we forbid redeclarations.
		
		assertTrue(runTest("{" + fac + " fac(0) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(1) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	@Test public void higherOrder() {
		
		String add = "int add(int a, int b) { return a + b; }";
		String sub = "int sub(int a, int b) { return a - b; }";
		String doSomething = "int doSomething(int (int a, int b) F) { return F(1,2); }";

		assertTrue(runTest("{" + add + " " + doSomething + " " + "doSomething(add) == 3;}"));
		assertTrue(runTest("{" + add + " " + sub + " " + doSomething + " " + "doSomething(sub) == -1;}"));
	}
	
	@Test public void closures() {
		
		String doSomething = "int f(int (int i) g, int j) { return g(j); }";
		
	    assertTrue(runTest("{ " + doSomething + " f(int (int i) { return i + 1; }, 0) == 1; }"));
	    assertTrue(runTest("{ int x = 1; " + doSomething + " (f(int (int i) { x = x * 2; return i + x; }, 1) == 3) && (x == 2); }"));
	}
	
	@Test public void varArgs() {
		
		String add0 = "int add(int i...) { return 0; }";
		String add1 = "int add(int i...) { return i[0]; }";
		String add2 = "int add(int i, int j...) { return i + j[0]; }";
		
		assertTrue(runTest("{" + add0 + " add() == 0; }"));
		assertTrue(runTest("{" + add0 + " add([]) == 0; }"));
		assertTrue(runTest("{" + add0 + " add(0) == 0; }"));
		assertTrue(runTest("{" + add0 + " add([0]) == 0; }"));
		assertTrue(runTest("{" + add0 + " add(0,1,2) == 0; }"));
		assertTrue(runTest("{" + add0 + " add([0,1,2]) == 0; }"));
		
		assertTrue(runTest("{" + add1 + " add(0) == 0; }"));
		assertTrue(runTest("{" + add1 + " add([0]) == 0; }"));
		assertTrue(runTest("{" + add1 + " add(0,1,2) == 0; }"));
		assertTrue(runTest("{" + add1 + " add([0,1,2]) == 0; }"));
		
		assertTrue(runTest("{" + add2 + " add(1,2) == 3; }"));
		assertTrue(runTest("{" + add2 + " add(1,[2]) == 3; }"));
		assertTrue(runTest("{" + add2 + " add(1,2,3) == 3; }"));
		assertTrue(runTest("{" + add2 + " add(1,[2,3]) == 3; }"));
	}
	
	@Test public void sideEffect() {
		
		String one = "void One() { called = called + 1; return; }";
		
		assertTrue(runTest("{ int called = 0; " + one + " One(); One(); One(); called == 3;}"));
	}
	
	@Test public void max1() {
		
		String maxInt = "int max(int a, int b) { return a > b ? a : b; }";
		String maxReal = "real max(real a, real b) { return a > b ? a : b; }";
		assertTrue(runTest("{" + maxInt + " max(3,4) == 4;}"));
		assertTrue(runTest("{" + maxInt + maxReal + " (max(3,4) == 4) && (max(3.0,4.0) == 4.0);}"));
	}
	
	@Test public void max2() {
		
		String max = "&T max(&T a, &T b) { return a > b ? a : b; }";
		assertTrue(runTest("{" + max + " max(3,4) == 4;}"));
		assertTrue(runTest("{" + max + " max(3.0,4.0) == 4.0;}"));
		assertTrue(runTest("{" + max + " max(\"abc\",\"def\") == \"def\";}"));
	}
	
	@Test public void ident() {
		
		String ident = "&T ident(&T x){ return x; }";
		assertTrue(runTest("{" + ident + " ident(true) == true;}"));
		assertTrue(runTest("{" + ident + " ident(4) == 4;}"));
		assertTrue(runTest("{" + ident + " ident(4.5) == 4.5;}"));
		assertTrue(runTest("{" + ident + " ident(\"abc\") == \"abc\";}"));
//		assertTrue(runTest("{" + ident + " ident(f(1)) == f(1);}"));
		assertTrue(runTest("{" + ident + " ident([1,2,3]) == [1,2,3];}"));
		assertTrue(runTest("{" + ident + " ident({1,2,3}) == {1,2,3};}"));
		assertTrue(runTest("{" + ident + " ident((1:10,2:20,3:30)) == (1:10,2:20,3:30);}"));
	}
	
	@Test public void map() {
		
		String put = "map[&K,&V] put(map[&K,&V] m, &K k, &V v) { m[k] = v; return m; }";
		
		assertTrue(runTest("{" + put + " put((),1,\"1\") == (1:\"1\"); }"));
	}
	
	@Ignore
	@Test public void add() {
		
		String add = "list[&T] java add(&T elm, list[&T] lst) { return lst.insert(elm); }";
		
		assertTrue(runTest("{" + add + " add(1, [2,3]) == [1,2,3];}"));
		assertTrue(runTest("{" + add + " add(\"a\", [\"b\",\"c\"]) == [\"a\",\"b\", \"c\"];}"));
	}
	
	@Ignore
	@Test public void putAt() {
		
		String putAt = "list[&T] java putAt(&T elm, int n, list[&T] lst){return lst.put(n.intValue(), elm);}";
		
		assertTrue(runTest("{" + putAt + " putAt(1, 0, [2,3]) == [1,3];}"));
	}
	
	@Test public void dispatchTest1() {
		prepare("data X = x() | y() | z();");
		prepareMore("public int f(x()) = 1;");
		prepareMore("public int f(y()) = 2;");
		prepareMore("public int f(z()) = 3;");

		assertTrue(runTestInSameEvaluator("[f(x()),f(y()),f(z())] == [1,2,3]"));
	}
	
	@Test public void dispatchTest2() {
		prepare("data X = x() | y() | z();");
		prepareMore("public int f(x()) = 1;");
		prepareMore("public int f(y()) = 2;");
		prepareMore("public int f(z()) = 3;");
		prepareMore("public default int f(int x) = x;");

		assertTrue(runTestInSameEvaluator("[f(x()),f(y()),f(z()),f(4)] == [1,2,3,4]"));
	}
	
	@Test public void dispatchTest3() {
		prepare("syntax X = \"x\" | \"y\" | \"z\";");
		prepareMore("public int f((X) `x`) = 1;");
		prepareMore("public int f((X) `y`) = 2;");
		prepareMore("public int f((X) `z`) = 3;");

		assertTrue(runTestInSameEvaluator("[f(`x`),f(`y`),f(`z`)] == [1,2,3]"));
	}
}

