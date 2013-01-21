/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no - CWI/UiB
*******************************************************************************/
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;


public class NumberTests extends TestFramework {

	@Test
	public void abs() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{abs(0) == 0;}"));
		assertTrue(runTestInSameEvaluator("{abs(0r) == 0r;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1) == 1;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1r1) == 1r1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1) == 1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1.5) == 1.5;}"));
		assertTrue(runTestInSameEvaluator("{abs(3r2) == 3r2;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1.5) == 1.5;}"));
		assertTrue(runTestInSameEvaluator("{abs(-3r2) == 3r2;}"));
	}

	@Test
	public void arbInt() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{int N = arbInt(10); (N >= 0) && (N < 10);}"));

		assertTrue(runTestInSameEvaluator("{int N = arbInt(); true;}"));
	}

	@Test
	public void compare() {

		assertTrue(runTestInSameEvaluator("1r1 == 1"));
		assertTrue(runTestInSameEvaluator("1r1 == 1.0"));
		assertTrue(runTestInSameEvaluator("-1r1 == -1"));

		assertTrue(runTestInSameEvaluator("1r2 < 1"));
		assertTrue(runTestInSameEvaluator("1r2 <= 1"));
		assertTrue(runTestInSameEvaluator("1r1 <= 1"));
		assertTrue(runTestInSameEvaluator("3r2 > 1"));
		assertTrue(runTestInSameEvaluator("3r2 >= 1"));
		assertTrue(runTestInSameEvaluator("3r1 >= 1"));

		assertTrue(runTestInSameEvaluator("1r2 < 1.0"));
		assertTrue(runTestInSameEvaluator("1r2 <= 1.0"));
		assertTrue(runTestInSameEvaluator("1r1 <= 1.0"));
		assertTrue(runTestInSameEvaluator("3r2 > 1.0"));
		assertTrue(runTestInSameEvaluator("3r2 >= 1.0"));
		assertTrue(runTestInSameEvaluator("3r1 >= 1.0"));
		
		assertTrue(runTestInSameEvaluator("1r2 < 2r2"));
		assertTrue(runTestInSameEvaluator("1r2 <= 2r2"));
		assertTrue(runTestInSameEvaluator("1r1 <= 2r2"));
		assertTrue(runTestInSameEvaluator("3r2 > 2r2"));
		assertTrue(runTestInSameEvaluator("3r2 >= 2r2"));
		assertTrue(runTestInSameEvaluator("3r1 >= 2r2"));
	}

	@Test
	public void arithPromotion() {
		assertTrue(runTestInSameEvaluator("2r4 + 1r2 == 1r"));
		assertTrue(runTestInSameEvaluator("2r4 - 1r2 == 0r"));
		assertTrue(runTestInSameEvaluator("2r4 * 1r2 == 1r4"));
		assertTrue(runTestInSameEvaluator("2r4 / 1r2 == 1r"));

		assertTrue(runTestInSameEvaluator("2r4 + 2 == 5r2"));
		assertTrue(runTestInSameEvaluator("2r4 - 2 == -3r2"));
		assertTrue(runTestInSameEvaluator("2r4 * 2 == 1r"));
		assertTrue(runTestInSameEvaluator("2r4 / 2 == 1r4"));

		assertTrue(runTestInSameEvaluator("2r4 + 2.0 == 2.5"));
		assertTrue(runTestInSameEvaluator("2r4 - 2.0 == -1.5"));
		assertTrue(runTestInSameEvaluator("2r4 * 2.0 == 1.0"));
		assertTrue(runTestInSameEvaluator("2r4 / 2.0 == 0.25"));

		
		assertTrue(runTestInSameEvaluator("2 + 1r2 == 5r2"));
		assertTrue(runTestInSameEvaluator("2 - 1r2 == 3r2"));
		assertTrue(runTestInSameEvaluator("2 * 1r2 == 1r"));
		assertTrue(runTestInSameEvaluator("2 / 1r2 == 4r"));

		assertTrue(runTestInSameEvaluator("2.0 + 1r2 == 2.5"));
		assertTrue(runTestInSameEvaluator("2.0 - 1r2 == 1.5"));
		assertTrue(runTestInSameEvaluator("2.0 * 1r2 == 1.0"));
		assertTrue(runTestInSameEvaluator("2.0 / 1r2 == 4.0"));
	}

	@Test
	public void arbReal() {

		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("{real D = arbReal(); (D >= 0.0) && (D <= 1.0);}"));
	}

	@Test
	public void max() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(10, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("max(10.0, 10.0) == 10.0;"));
		
		assertTrue(runTestInSameEvaluator("max(3.5, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(3, 10.5) == 10.5;"));
	}

	@Test
	public void min() {

		prepare("import util::Math;");

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

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("toInt(3) == 3;"));
		assertTrue(runTestInSameEvaluator("toInt(3.14) == 3;"));
		assertTrue(runTestInSameEvaluator("toInt(3r2) == 1;"));
		assertTrue(runTestInSameEvaluator("toInt(4r2) == 2;"));
	}

	@Test
	public void toReal() {

		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("toReal(3) == 3.0;"));
		assertTrue(runTestInSameEvaluator("toReal(3.14) == 3.14;"));
	}

	@Test
	public void testToString() {

		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("toString(314) == \"314\";"));
		assertTrue(runTestInSameEvaluator("toString(3.14) == \"3.14\";"));
		assertTrue(runTestInSameEvaluator("toString(4r8) == \"1r2\";"));

	}

}
