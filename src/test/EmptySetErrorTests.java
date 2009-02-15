package test;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.EmptySetError;

public class EmptySetErrorTests extends TestFramework {
	
	
	@Test(expected=EmptySetError.class)
	public void testGetOneFrom() {
		prepare("import Set;");
		runTest("getOneFrom({});");
	}
	
	@Test(expected=EmptySetError.class)
	public void testTakeOneFrom() {
		prepare("import Set;");
		runTest("takeOneFrom({});");
	}
}
