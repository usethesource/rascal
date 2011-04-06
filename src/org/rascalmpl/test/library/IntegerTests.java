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
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.TestFramework;


public class IntegerTests extends TestFramework {

	@Test
	public void abs() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("{abs(0) == 0;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1) == 1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1) == 1;}"));
	}

	@Test
	public void arbInt() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("{int N = Integer::arbInt(10); (N >= 0) && (N < 10);}"));
		assertTrue(runTestInSameEvaluator("{int N = arbInt(10); (N >= 0) && (N < 10);}"));

		assertTrue(runTestInSameEvaluator("{int N = arbInt(); true;}"));
	}

	@Test
	public void max() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("Integer::max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("Integer::max(10, 10) == 10;"));
	}

	@Test
	public void min() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("Integer::min(3, 10) == 3;"));
		assertTrue(runTestInSameEvaluator("min(3, 10) == 3;"));
		assertTrue(runTestInSameEvaluator("Integer::min(10, 10) == 10;"));
	}

	@Test
	public void toReal() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("Integer::toReal(3) == 3.0;"));
		assertTrue(runTestInSameEvaluator("toReal(3) == 3.0;"));
	}

	@Test
	public void testToString() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("Integer::toString(314) == \"314\";"));
		assertTrue(runTestInSameEvaluator("toString(314) == \"314\";"));
	}

}
