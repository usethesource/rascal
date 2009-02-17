package test.ErrorTests;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.AssignmentError;

import test.TestFramework;

public class AssignmentErrorTests extends TestFramework {
	
	@Test(expected=AssignmentError.class)
	public void testAssignment() {
		runTest("{int n = 3; n = true;}");
	}
	
}
