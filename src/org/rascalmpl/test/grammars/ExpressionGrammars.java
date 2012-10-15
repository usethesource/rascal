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
package org.rascalmpl.test.grammars;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;

public class ExpressionGrammars extends TestFramework {
	@Test
	public void assoc() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\assoc()"));
	}

	@Test
	public void mutualAssoc1() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\mutualAssoc1()"));
	}

	@Test
	public void mutualAssoc2() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\mutualAssoc2()"));
	}

	@Test
	public void prio() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\prio()"));
	}

	@Test
	public void safePrio1() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\safePrio1()"));
	}

	@Test
	public void safePrio2() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\safePrio2()"));
	}

	@Test
	public void safePrio3() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\safePrio3()"));
	}

	@Test
	public void safePrio4() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\safePrio4()"));
	}

	@Test
	public void transPrio() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\transPrio()"));
	}

	@Test
	public void exceptNormal() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\exceptNormal()"));
	}

	@Test
	public void exceptInList() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\exceptInList()"));
	}

	@Test
	public void exceptInOpt() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\exceptInOpt()"));
	}

	@Test
	public void exceptInSeq1() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\exceptInSeq1()"));
	}

	@Test
	public void exceptInSeq2() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\exceptInSeq2()"));
	}

	@Test
	public void exceptInAlt() {
		prepare("import ExpressionGrammars;");
		assertTrue(runTestInSameEvaluator("\\exceptInAlt()"));
	}
}
