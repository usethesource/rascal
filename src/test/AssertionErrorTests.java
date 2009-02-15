package test;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.AssertionError;

public class AssertionErrorTests extends TestFramework {

	@Test(expected=AssertionError.class)
	public void testAssertion() {
		runTest("assert \"a1\": 1 == 2;");
	}
}
