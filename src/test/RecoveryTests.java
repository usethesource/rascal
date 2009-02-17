package test;

import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;

public class RecoveryTests extends TestFramework {

	@Test
	public void testRecoveryOfLocalVariable() {
		prepare("import RecoveryTests;");
		assertTrue(runTestInSameEvaluator("recoveryOfLocalVariable() == 0;"));
	}

	@Test
	public void testRecoveryOfLocalVariableUsingIfThen() {
		prepare("import RecoveryTests;");
		assertTrue(runTestInSameEvaluator("recoveryOfLocalVariableUsingIfThen() == 0;"));
	}

	@Test
	public void testRecoveryOfGlobalVariable() {
		prepare("import RecoveryTests;");
		assertTrue(runTestInSameEvaluator("recoveryOfGlobalVariable() == 0;"));
	}

}
