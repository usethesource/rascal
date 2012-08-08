/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;

public class RecoveryTests extends TestFramework {


	@Before
	public void prepare() {
		prepare("import RecoveryTests;");
	}
	
	@Test @Ignore
	public void testRecoveryOfLocalVariable() {
		assertTrue(runTestInSameEvaluator("recoveryOfLocalVariable() == 0;"));
	}

	@Test @Ignore
	public void testNestedRecoveryOfLocalVariable() {
		assertTrue(runTestInSameEvaluator("nestedRecoveryOfLocalVariable() == 3;"));
	}

	@Test @Ignore
	public void testNoNestedRecovery() {
		assertTrue(runTestInSameEvaluator("noNestedRecovery() == 12;"));
	}

	@Test @Ignore
	public void testRecoveryOfLocalVariableUsingIfThen() {
		assertTrue(runTestInSameEvaluator("recoveryOfLocalVariableUsingIfThen() == 0;"));
	}

	@Test @Ignore
	public void testRecoveryOfGlobalVariable() {
		assertTrue(runTestInSameEvaluator("recoveryOfGlobalVariable() == 0;"));
	}

	@Test @Ignore
	public void testRecoveryAfterFailingRule() {
		assertTrue(runTestInSameEvaluator("recoveryOfGlobalAfterFailingRule() == 0;"));
	}
	
	@Test @Ignore
	public void testRecoveryOfGlobalDuringComprehension() {
		assertTrue(runTestInSameEvaluator("recoveryOfGlobalDuringComprehension() == 0;"));
	}
}
