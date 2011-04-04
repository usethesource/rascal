/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.TestFramework;


public class NumberTests extends TestFramework {

	@Test
	public void abs() {

		prepare("import Number;");

		assertTrue(runTestInSameEvaluator("{abs(0) == 0;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1) == 1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1) == 1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1.5) == 1.5;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1.5) == 1.5;}"));
	}

	@Test
	public void arbInt() {

		prepare("import Number;");

		assertTrue(runTestInSameEvaluator("{int N = arbInt(10); (N >= 0) && (N < 10);}"));

		assertTrue(runTestInSameEvaluator("{int N = arbInt(); true;}"));
	}
	
	@Test
	public void arbReal() {

		prepare("import Number;");
		assertTrue(runTestInSameEvaluator("{real D = arbReal(); (D >= 0.0) && (D <= 1.0);}"));
	}

	@Test
	public void max() {

		prepare("import Number;");

		assertTrue(runTestInSameEvaluator("max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(10, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("max(10.0, 10.0) == 10.0;"));
		
		assertTrue(runTestInSameEvaluator("max(3.5, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(3, 10.5) == 10.5;"));
	}

	@Test
	public void min() {

		prepare("import Number;");

		assertTrue(runTestInSameEvaluator("min(3, 10) == 3;"));
		assertTrue(runTestInSameEvaluator("min(10, 10) == 10;"));
		
		assertTrue(runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("min(10.0, 10.0) == 10.0;"));
		
		assertTrue(runTestInSameEvaluator("min(3.5, 10) == 3.5;"));
		assertTrue(runTestInSameEvaluator("min(3, 10.5) == 3;"));

	}
	
	@Test
	public void toInt() {

		prepare("import Number;");

		assertTrue(runTestInSameEvaluator("toInt(3) == 3;"));
		assertTrue(runTestInSameEvaluator("toInt(3.14) == 3;"));
	}

	@Test
	public void toReal() {

		prepare("import Number;");
		assertTrue(runTestInSameEvaluator("toReal(3) == 3.0;"));
		assertTrue(runTestInSameEvaluator("toReal(3.14) == 3.14;"));
	}

	@Test
	public void testToString() {

		prepare("import Number;");
		assertTrue(runTestInSameEvaluator("toString(314) == \"314\";"));
		assertTrue(runTestInSameEvaluator("toString(3.14) == \"3.14\";"));

	}

}
