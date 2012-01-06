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


public class RealTests extends TestFramework {

	@Test
	public void arbReal() {

		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("{real D = arbReal(); (D >= 0.0) && (D <= 1.0);}"));
	}

	@Test
	public void max() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
	}

	@Test
	public void min() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
	}

	@Test
	public void toInt() {

		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("toInt(3.14) == 3;"));
	}

	@Test
	public void testToString() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("toString(3.14) == \"3.14\";"));

	}
}
