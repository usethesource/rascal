package test.ErrorTests;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.NoSuchFieldError;

import test.TestFramework;

public class NoSuchFieldErrorTests extends TestFramework {
	
	@Test(expected=NoSuchFieldError.class)
	public void testField1() {
		runTest("{tuple[int a, str b] T = <1, \"a\">;T.zip == 0;}");
	}
}
