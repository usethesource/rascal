package test.ErrorTests;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.NoSuchFunctionError;

import test.TestFramework;

public class NoSuchFunctionErrorTests extends TestFramework {
	
	@Test(expected=NoSuchFunctionError.class)
	public void testFunction11() {
		runTest("zap(1,2);");
	}
}
