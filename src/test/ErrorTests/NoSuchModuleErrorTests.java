package test.ErrorTests;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.NoSuchModuleError;

import test.TestFramework;

public class NoSuchModuleErrorTests extends TestFramework {
	
	@Test(expected=NoSuchModuleError.class)
	public void testModule() {
		runTest("import zap;");
	}
}
