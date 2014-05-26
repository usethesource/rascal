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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.ModuleImport;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariable;
import org.rascalmpl.test.infrastructure.TestFramework;

public class ImportTests extends TestFramework {
	
	@Test
	public void testFun() {
		
		prepareModule("M", "module M" +
				         " public int f(int n) {return 2 * n;}" +
				         " private int g(int n) { return 2 * n;}");
		
		assertTrue(runTestInSameEvaluator("import M;"));
		assertTrue(runTestInSameEvaluator("M::f(3) == 6;"));
		assertTrue(runTestInSameEvaluator("f(3) == 6;"));
		assertTrue(runTestInSameEvaluator("{ int f(int n) {return 3 * n;} f(3) == 9;}"));
	}
	
	@Test
	public void testVar() {
		
		prepareModule("M", "module M\n" +
				         "public int n = 3;\n" +
				         "private int m = 3;");
		assertTrue(runTestInSameEvaluator("import M;"));
		assertTrue(runTestInSameEvaluator("M::n == 3;"));
		assertTrue(runTestInSameEvaluator("n == 3;"));
		assertTrue(runTestInSameEvaluator("{ int n = 4; n == 4;}"));
	}
	
	@Test
	public void testMbase1() {
		
		prepare("import Mbase;");
		
		assertTrue(runTestInSameEvaluator("Mbase::n == 2;"));
		assertTrue(runTestInSameEvaluator("n == 2;"));
		assertTrue(runTestInSameEvaluator("Mbase::f(3) == 6;"));
		assertTrue(runTestInSameEvaluator("f(3) == 6;"));
		assertTrue(runTestInSameEvaluator("{ int n = 3; n == 3;}"));
	}
	
	@Test
	public void testMbase2() {
		
		prepareModule("M", "module M " +
						 "import Mbase; " +
						 "public int m = n;" +
						 "public int f() { return n; }"	 +
						 "public int g() { return m; } "
		);
		
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("M::m == 2;"));
		assertTrue(runTestInSameEvaluator("M::f() == 2;"));
		assertTrue(runTestInSameEvaluator("M::g() == 2;"));
	}
	
	@Test
	public void testMbase3() {
		
		prepareModule("M", "module M " +
						 "import Mbase;" +
						 "public int g(int n) {return 3 * n;}" +
						 "public int h(int n) {return f(n);}" +
						 "public int m = 3;"
		);
		
		prepareMore("import M;");
		prepareMore("import Mbase;");
		assertTrue(runTestInSameEvaluator("Mbase::n == 2;"));
		assertTrue(runTestInSameEvaluator("Mbase::f(3) == 6;"));
		
		assertTrue(runTestInSameEvaluator("M::m == 3;"));
		assertTrue(runTestInSameEvaluator("M::g(3) == 9;"));
		
		assertTrue(runTestInSameEvaluator("M::h(3) == 6;"));
	}
	
	@Test
	public void testSize() {
		
		prepareModule("Msize", "module Msize \n" +
				         "import Set;\n" +
						 "public set[int] Procs = {1, 2, 3};\n" +
						 "public int f() {int nProcs = Set::size(Procs); return nProcs;}\n" +
						 "public int g() {int nProcs = size(Procs); return nProcs;}\n"
		);
		
		runTestInSameEvaluator("import Msize;");
		assertTrue(runTestInSameEvaluator("f() == 3;"));
		assertTrue(runTestInSameEvaluator("g() == 3;"));
	}
	
	@Test
	public void testDataImport(){
		prepareModule("Mdata", "module Mdata\n" +
				      "public data TYPE = natural() | string();");
		
		runTestInSameEvaluator("import Mdata;");
		assertTrue(runTestInSameEvaluator("natural() == natural();"));
		assertTrue(runTestInSameEvaluator("string() == string();"));
		assertTrue(runTestInSameEvaluator("natural() != string();"));
		
	}
}
