package test;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.NoSuchFunctionError;

public class NoSuchFunctionErrorTests extends TestFramework {
	
	@Test(expected=NoSuchFunctionError.class)
	public void testFunction11() {
		runTest("zap(1,2);");
	}
}
