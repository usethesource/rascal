package test.ErrorTests;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.AssertionError;

import test.TestFramework;

public class AssertionErrorTests extends TestFramework {

	@Test(expected=AssertionError.class)
	public void testAssertion() {
		runTest("assert 1 == 2;");
	}
}
