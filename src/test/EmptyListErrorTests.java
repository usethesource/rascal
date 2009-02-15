package test;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.EmptyListError;

public class EmptyListErrorTests extends TestFramework {
	
	@Test(expected=EmptyListError.class)
	public void testHead1() {
		prepare("import List;");
		runTest("head([]);");
	}
	
	@Test(expected=EmptyListError.class)
	public void testGetOneFrom() {
		prepare("import List;");
		runTest("getOneFrom([]);");
	}
	
	@Test(expected=EmptyListError.class)
	public void testTakeOneFrom() {
		prepare("import List;");
		runTest("takeOneFrom([]);");
	}
}
