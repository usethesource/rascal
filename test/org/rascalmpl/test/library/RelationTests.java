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


public class RelationTests extends TestFramework {

	@Test
	public void carrier() {

		prepare("import Relation;");

		assertTrue(runTestInSameEvaluator("carrier({<1,10>,<2,20>}) == {1,2,10,20};"));
		assertTrue(runTestInSameEvaluator("carrier({<1,10,100>,<2,20,200>}) == {1,2,10,20,100,200};"));
		assertTrue(runTestInSameEvaluator("carrier({<1,10,100,1000>,<2,20,200,2000>}) == {1,2,10,20,100,200,1000,2000};"));
		assertTrue(runTestInSameEvaluator("carrier({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {1,2,10,20,100,200,1000,2000,10000,20000};"));

	}

	@Test
	public void carrierR() {

		prepare("import Relation;");

		assertTrue(runTestInSameEvaluator("carrierR({<1,10>,<2,20>}, {} ) == {};"));
		assertTrue(runTestInSameEvaluator("carrierR({<1,10>,<2,20>}, {2,3} ) == {};"));
		assertTrue(runTestInSameEvaluator("carrierR({<1,10>,<2,20>}, {2,20} ) == {<2,20>};"));
		assertTrue(runTestInSameEvaluator("carrierR({<1,10,100>,<2,20,200>}, {2, 20,200}) == {<2,20,200>};"));
		assertTrue(runTestInSameEvaluator("carrierR({<1,10,100>,<2,20,200>}, {1,2,10,20,100,200}) == {<1,10,100>,<2,20,200>};"));
		assertTrue(runTestInSameEvaluator("carrierR({<1,10,100,1000>,<2,20,200,2000>}, {1,10,100,1000}) == {<1,10,100,1000>};"));
		assertTrue(runTestInSameEvaluator("carrierR({<1,10,100,1000>,<2,20,200,2000>}, {2,20,200,2000}) == {<2,20,200,2000>};"));
	}

	@Test
	public void carrierX() {

		prepare("import Relation;");

		assertTrue(runTestInSameEvaluator("carrierX({<1,10>,<2,20>}, {} ) == {<1,10>,<2,20>};"));
		assertTrue(runTestInSameEvaluator("carrierX({<1,10>,<2,20>}, {2,3} ) == {<1,10>};"));
		assertTrue(runTestInSameEvaluator("carrierX({<1,10,100>,<2,20,200>}, {20}) == {<1,10,100>};"));
		assertTrue(runTestInSameEvaluator("carrierX({<1,10,100>,<2,20,200>}, {20,100}) == {};"));
		assertTrue(runTestInSameEvaluator("carrierX({<1,10,100,1000>,<2,20,200,2000>}, {1000}) == {<2,20,200,2000>};"));
		assertTrue(runTestInSameEvaluator("carrierX({<1,10,100,1000>,<2,20,200,2000>}, {2}) == {<1,10,100,1000>};"));
	}

	@Test
	public void complement() {

		prepare("import Relation;");

		assertTrue(runTestInSameEvaluator("complement({<1,10>,<2,20>}) == {<2,10>,<1,20>};"));
		assertTrue(runTestInSameEvaluator("complement({<1,10,100>,<2,20,200>}) == {<2,20,100>,<2,10,200>,<2,10,100>,<1,20,200>,<1,20,100>,<1,10,200>};"));
		assertTrue(runTestInSameEvaluator("complement({<1,10,100,1000>,<2,20,200,2000>}) == {<2,20,200,1000>,<1,10,100,2000>,<1,10,200,1000>,<1,10,200,2000>,<1,20,100,1000>,<1,20,100,2000>,<1,20,200,1000>,<1,20,200,2000>,<2,10,100,1000>,<2,10,100,2000>,<2,10,200,1000>,<2,10,200,2000>,<2,20,100,1000>,<2,20,100,2000>};"));
	}

	@Test
	public void domain() {

		prepare("import Relation;");

		assertTrue(runTestInSameEvaluator("domain({<1,10>,<2,20>}) == {1,2};"));
		assertTrue(runTestInSameEvaluator("domain({<1,10,100>,<2,20,200>}) == {1,2};"));
		assertTrue(runTestInSameEvaluator("domain({<1,10,100,1000>,<2,20,200,2000>}) == {1,2};"));
		assertTrue(runTestInSameEvaluator("domain({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {1,2};"));
	}

	@Test
	public void domainR() {

		prepare("import Relation;");

		assertTrue(runTestInSameEvaluator("domainR({<1,10>,<2,20>}, {}) == {};"));
		assertTrue(runTestInSameEvaluator("domainR({<1,10>,<2,20>}, {2}) == {<2,20>};"));
		assertTrue(runTestInSameEvaluator("domainR({<1,10,100>,<2,20,200>}, {2,5}) == {<2,20,200>};"));
		assertTrue(runTestInSameEvaluator("domainR({<1,10,100,1000>,<2,20,200,2000>}, {1,3}) == {<1,10,100,1000>};"));
		assertTrue(runTestInSameEvaluator("domainR({<1,10,100,1000,10000>,<2,20,200,2000,20000>},{2,5}) == {<2,20,200,2000,20000>};"));
	}

	@Test
	public void domainX() {

		prepare("import Relation;");

		assertTrue(runTestInSameEvaluator("domainX({<1,10>,<2,20>}, {}) == {<1,10>,<2,20>};"));
		assertTrue(runTestInSameEvaluator("domainX({<1,10>,<2,20>}, {2}) == {<1,10>};"));
		assertTrue(runTestInSameEvaluator("domainX({<1,10,100>,<2,20,200>}, {2,5}) == {<1,10,100>};"));
		assertTrue(runTestInSameEvaluator("domainX({<1,10,100,1000>,<2,20,200,2000>}, {1,3}) == {<2,20,200,2000>};"));
		assertTrue(runTestInSameEvaluator("domainX({<1,10,100,1000,10000>,<2,20,200,2000,20000>},{2,5}) == {<1,10,100,1000,10000>};"));

	}
	
	@Test
	public void ident() {

		prepare("import Relation;");
		
		//assertTrue(runTestInSameEvaluator("ident({}) == {};"));
		assertTrue(runTestInSameEvaluator("ident({1}) == {<1,1>};"));
		assertTrue(runTestInSameEvaluator("ident({1,2,3}) == {<1,1>,<2,2>,<3,3>};"));
	}

	@Test
	public void invert() {

		prepare("import Relation;");

		assertTrue(runTestInSameEvaluator("invert({<1,10>,<2,20>}) == {<10,1>,<20,2>};"));
		assertTrue(runTestInSameEvaluator("invert({<1,10,100>,<2,20,200>}) == {<100,10,1>,<200,20,2>};"));
		assertTrue(runTestInSameEvaluator("invert({<1,10,100,1000>,<2,20,200,2000>}) == {<1000,100,10,1>,<2000,200,20,2>};"));
		assertTrue(runTestInSameEvaluator("invert({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10000,1000,100,10,1>,<20000,2000,200,20,2>};"));
	}

	@Test
	public void range() {

		prepare("import Relation;");

		assertTrue(runTestInSameEvaluator("range({<1,10>,<2,20>}) == {10,20};"));
		assertTrue(runTestInSameEvaluator("range({<1,10,100>,<2,20,200>}) == {<10,100>,<20,200>};"));
		assertTrue(runTestInSameEvaluator("range({<1,10,100,1000>,<2,20,200,2000>}) == {<10,100,1000>,<20,200,2000>};"));
		assertTrue(runTestInSameEvaluator("range({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10,100,1000,10000>,<20,200,2000,20000>};"));
	}

	@Test
	public void rangeR() {

		prepare("import Relation;");

		// assertTrue(runTestInSameEvaluator("rangeR({<1,10>,<2,20>}, {}) == {};"));
		assertTrue(runTestInSameEvaluator("rangeR({<1,10>,<2,20>}, {20}) == {<2,20>};"));
		// assertTrue(runTestInSameEvaluator("rangeR({<1,10,100>,<2,20,200>}) == {<10,100>,<20,200>};"));
		// assertTrue(runTestInSameEvaluator("rangeR({<1,10,100,1000>,<2,20,200,2000>}) == {<10,100,1000>,<20,200,2000>};"));
		// assertTrue(runTestInSameEvaluator("rangeR({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10,100,1000,10000>,<20,200,2000,20000>};"));

	}

	@Test
	public void rangeX() {

		prepare("import Relation;");

		// assertTrue(runTestInSameEvaluator("rangeX({<1,10>,<2,20>}, {}) == {<1,10>,<2,20>};"));
		assertTrue(runTestInSameEvaluator("rangeX({<1,10>,<2,20>}, {20}) == {<1,10>};"));
		// assertTrue(runTestInSameEvaluator("rangeX({<1,10,100>,<2,20,200>}) == {<10,100>,<20,200>};"));
		// assertTrue(runTestInSameEvaluator("rangeX({<1,10,100,1000>,<2,20,200,2000>}) == {<10,100,1000>,<20,200,2000>};"));
		// assertTrue(runTestInSameEvaluator("rangeX({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10,100,1000,10000>,<20,200,2000,20000>};"));

	}
	
	// Tests related to the correctness of the dynamic types of relations produced by the library functions;
	// incorrect dynamic types make pattern matching fail;

//	@Test
//	public void testDynamicTypes() {
//		// substraction is not supported yet: assertTrue(runTestInSameEvaluator("{ rel[value, value] sr = {<\"1\",\"1\">,<2,2>,<3,3>}; rel[int, int] _ := sr - <\"1\",\"1\">; }"));
//		assertTrue(runTestInSameEvaluator("{ rel[value a, value b] sr = {<\"1\",\"1\">,<2,2>,<3,3>}; rel[int, int] _ := sr - {<\"1\",\"1\">} && (sr - {<\"1\",\"1\">}).a == {2,3} && (sr - {<\"1\",\"1\">}).b == {2,3}; }"));
//		assertTrue(runTestInSameEvaluator("{ {<\"1\",\"1\">, *tuple[int,int] _} := {<\"1\",\"1\">,<2,2>,<3,3>}; }"));
//		
//		assertTrue(runTestInSameEvaluator("{ rel[value a, value b] sr1 = {<\"1\",\"1\">,<2,2>,<3,3>}; rel[value a, value b] sr2 = {<2,2>,<3,3>}; rel[int, int] _ := sr1 & sr2 && (sr1 & sr2).a == {2,3} && (sr2 & sr1).b == {2,3}; }"));
//	}


}
