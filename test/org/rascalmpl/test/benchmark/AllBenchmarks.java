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
package org.rascalmpl.test.benchmark;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;

public class AllBenchmarks extends TestFramework {

	
	@Test
	public void BubbleSort() {
		prepare("import BubbleSort::BubbleSort;");
		assertTrue(runTestInSameEvaluator("BubbleSort::BubbleSort::measure();"));
	}

	@Test
	public void Factorial() {
		prepare("import Factorial::Factorial;");
		assertTrue(runTestInSameEvaluator("Factorial::Factorial::measure();"));
	}

	@Test
	public void Fibonacci() {
		prepare("import Fibonacci::Fibonacci;");
		assertTrue(runTestInSameEvaluator("Fibonacci::Fibonacci::measure();"));
	}

	@Test
	public void Reverse() {
		prepare("import Reverse::Reverse;");
		assertTrue(runTestInSameEvaluator("Reverse::Reverse::measure();"));
	}
	
	@Test
	public void RSFCalls() {
		prepare("import RSF::RSFCalls;");
		assertTrue(runTestInSameEvaluator("RSF::RSFCalls::measureOne();"));
	}
}
