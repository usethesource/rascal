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


public class GraphTests extends TestFramework {

	@Test
	public void bottom() {

		prepare("import analysis::graphs::Graph;");
		assertTrue(runTestInSameEvaluator("bottom({}) == {};"));
		assertTrue(runTestInSameEvaluator("bottom({<1,2>, <1,3>, <2,4>, <3,4>}) == {4};"));
	}

	@Test
	public void predecessors(){
		prepare("import analysis::graphs::Graph;");
		assertTrue(runTestInSameEvaluator("predecessors({<1,2>, <1,3>, <2,4>, <3,4>}, 4) =={2, 3};"));
	}

	@Test
	public void reachR() {

		prepare("import analysis::graphs::Graph;");

		assertTrue(runTestInSameEvaluator("reachR({}, {}, {}) == {};"));
		assertTrue(runTestInSameEvaluator("reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {}) =={};"));
		assertTrue(runTestInSameEvaluator("reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2}) =={2};"));
		assertTrue(runTestInSameEvaluator("reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2,3}) =={2,3};"));
		assertTrue(runTestInSameEvaluator("reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2,4}) =={2, 4};"));
	}

	@Test
	public void reachX() {

		prepare("import analysis::graphs::Graph;");

		assertTrue(runTestInSameEvaluator("reachX({}, {}, {}) == {};"));
		// assertTrue(runTestInSameEvaluator("reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {}) =={2, 3, 4};"));
		assertTrue(runTestInSameEvaluator("reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {2}) =={3, 4};"));
		// assertTrue(runTestInSameEvaluator("reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {2,3}) =={};"));
		assertTrue(runTestInSameEvaluator("reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {4}) =={2, 3};"));
	}
	
	@Test 
	public void reach(){
		
		prepare("import analysis::graphs::Graph;");
		
//		assertTrue(runTestInSameEvaluator("reach({}, {}, {}) == {};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {1}) =={1,2, 3, 4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {2}) =={2, 4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {3}) =={3, 4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {4}) =={4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {3,4}) =={3,4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {2,3}) =={2, 3,4};"));
	}
	
	@Test
	public void successors(){
		prepare("import analysis::graphs::Graph;");
		assertTrue(runTestInSameEvaluator("successors({<1,2>, <1,3>, <2,4>, <3,4>}, 1) =={2, 3};"));
	}

	@Test
	public void top() {

		prepare("import analysis::graphs::Graph;");
		assertTrue(runTestInSameEvaluator("top({}) == {};"));
		assertTrue(runTestInSameEvaluator("top({<1,2>, <1,3>, <2,4>, <3,4>}) == {1};"));
	}
}
