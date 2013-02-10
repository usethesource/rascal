/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.test.infrastructure.TestFramework;


public class MapTests extends TestFramework {

	@Test
	public void getOneFrom() {

		prepare("import Map;");

		assertTrue(runTestInSameEvaluator("getOneFrom((1:10)) == 1;"));
		assertTrue(runTestInSameEvaluator("{int N = getOneFrom((1:10, 2:20)); (N == 1) || (N ==2);}"));
	}
	
	@Test(expected=Throw.class)
	public void getOneFromError() {
		runTest("import Map;", "getOneFrom(());");
	}
	
	@Test
	public void invertUnique(){
		prepare("import Map;");
		assertTrue(runTestInSameEvaluator("invertUnique(()) == ();"));
		assertTrue(runTestInSameEvaluator("invertUnique((1:10)) == (10:1);"));
		assertTrue(runTestInSameEvaluator("invertUnique((1:10, 2:20)) == (10:1, 20:2);"));
		assertTrue(runTestInSameEvaluator("invertUnique(([[]]:0,[[2]]:2,[[1,2],[2,1]]:1,[[1]]:3)) == (0:[[]],2:[[2]],1:[[1,2],[2,1]],3:[[1]]);"));
		
	}
	
	@Test(expected=Throw.class) 
	public void invertError() {
		runTest("import Map;", "invertUnique((1:10, 2:10));");
	}
	
	@Test
	public void invert(){
		prepare("import Map;");
		assertTrue(runTestInSameEvaluator("invert(()) == ();"));
		assertTrue(runTestInSameEvaluator("invert((1:10)) == (10:{1});"));
		assertTrue(runTestInSameEvaluator("invert((1:10, 2:20)) == (10:{1}, 20:{2});"));
		assertTrue(runTestInSameEvaluator("invert((1:10, 2:10, 3:30, 4:30)) == (10: {1,2}, 30:{3,4});"));
		
	}
	
	@Test
	public void isEmpty(){
		prepare("import Map;");
		
		assertTrue(runTestInSameEvaluator("isEmpty(());"));
		assertTrue(runTestInSameEvaluator("isEmpty((1:10)) == false;"));
	}

	@Test
	public void domain() {

		prepare("import Map;");

		assertTrue(runTestInSameEvaluator("domain(()) == {};"));
		assertTrue(runTestInSameEvaluator("domain((1:10, 2:20)) == {1,2};"));
	}

	// mapper
	@Test
	public void mapper() {

		prepare("import Map;");

		String inc = "int inc(int n) {return n + 1;} ";
		String dec = "int dec(int n) {return n - 1;} ";

		assertTrue(runTestInSameEvaluator("{" + inc
				+ "mapper((), inc, inc) == ();}"));
		assertTrue(runTestInSameEvaluator("{" + inc
				+ "mapper((1:10,2:20), inc, inc) == (2:11,3:21);}"));

		assertTrue(runTestInSameEvaluator("{" + inc + dec
				+ "mapper((), inc, dec) == ();}"));
		assertTrue(runTestInSameEvaluator("{" + inc + dec
				+ "mapper((1:10,2:20), inc, dec) == (2:9,3:19);}"));
	}

	// range
	@Test
	public void range() {

		prepare("import Map;");

		assertTrue(runTestInSameEvaluator("range(()) == {};"));
		assertTrue(runTestInSameEvaluator("range((1:10, 2:20)) == {10,20};"));
	}

	// size
	@Test
	public void size() {

		prepare("import Map;");

		assertTrue(runTestInSameEvaluator("size(()) == 0;"));
		assertTrue(runTestInSameEvaluator("size((1:10)) == 1;"));
		assertTrue(runTestInSameEvaluator("size((1:10,2:20)) == 2;"));
	}

	// toList
	@Test
	public void toList() {

		prepare("import Map;");

		assertTrue(runTestInSameEvaluator("toList(()) == [];"));
		assertTrue(runTestInSameEvaluator("toList((1:10)) == [<1,10>];"));
		assertTrue(runTestInSameEvaluator("{list[tuple[int,int]] L = toList((1:10, 2:20)); (L == [<1,10>,<2,20>]) || (L == [<2,20>,<1,10>]);}"));
	}

	// toRel
	@Test
	public void toRel() {

		prepare("import Map;");

		assertTrue(runTestInSameEvaluator("toRel(()) == {};"));
		assertTrue(runTestInSameEvaluator("toRel((1:10)) == {<1,10>};"));
		assertTrue(runTestInSameEvaluator("{rel[int,int] R = toRel((1:10, 2:20)); R == {<1,10>,<2,20>};}"));
	}

	// toString
	@Test
	public void testToString() {

		prepare("import Map;");

		assertTrue(runTestInSameEvaluator("toString(()) == \"()\";"));
		assertTrue(runTestInSameEvaluator("toString((1:10)) == \"(1:10)\";"));
	}
	
	@Test
	public void mapExpressions() {
		assertTrue(runTest("{ value n = 1; value s = \"string\"; map[int, int] _ := ( n : n ) && map[str, str] _ := ( s : s ) && map[int, str] _ := ( n : s ); }"));
	}


}
