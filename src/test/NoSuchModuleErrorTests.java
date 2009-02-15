package test;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.NoSuchModuleError;

public class NoSuchModuleErrorTests extends TestFramework {
	
	@Test(expected=NoSuchModuleError.class)
	public void testModule() {
		runTest("import zap;");
	}
}
