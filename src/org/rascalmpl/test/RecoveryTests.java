package org.rascalmpl.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
