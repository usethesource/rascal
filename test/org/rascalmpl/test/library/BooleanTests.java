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
import org.rascalmpl.test.infrastructure.TestFramework;


public class BooleanTests extends TestFramework {

	@Test
	public void arb() {
		
		prepare("import Boolean;");
		
		assertTrue(runTestInSameEvaluator("{bool B = Boolean::arbBool(); (B == true) || (B == false);}"));
		assertTrue(runTestInSameEvaluator("{bool B = arbBool(); (B == true) || (B == false);}"));
	}

	@Test
	public void toInt() {
		
		prepare("import Boolean;");

		assertTrue(runTestInSameEvaluator("Boolean::toInt(false) == 0;"));
		assertTrue(runTestInSameEvaluator("Boolean::toInt(true) == 1;"));

		assertTrue(runTestInSameEvaluator("toInt(false) == 0;"));
		assertTrue(runTestInSameEvaluator("toInt(true) == 1;"));
	}

	@Test
	public void toReal() {
		
		prepare("import Boolean;");

		assertTrue(runTestInSameEvaluator("Boolean::toReal(false) == 0.0;"));
		assertTrue(runTestInSameEvaluator("Boolean::toReal(true) == 1.0;"));

		assertTrue(runTestInSameEvaluator("toReal(false) == 0.0;"));
		assertTrue(runTestInSameEvaluator("toReal(true) == 1.0;"));
	}

	@Test
	public void testToString() {
		prepare("import Boolean;");

		assertTrue(runTestInSameEvaluator("Boolean::toString(false) == \"false\";"));
		assertTrue(runTestInSameEvaluator("Boolean::toString(true) == \"true\";"));

		assertTrue(runTestInSameEvaluator("toString(false) == \"false\";"));
		assertTrue(runTestInSameEvaluator("toString(true) == \"true\";"));
	}
}
