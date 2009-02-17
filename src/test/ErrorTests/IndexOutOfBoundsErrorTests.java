package test.ErrorTests;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.IndexOutOfBoundsError;

import test.TestFramework;

public class IndexOutOfBoundsErrorTests extends TestFramework {
	
	@Test(expected=IndexOutOfBoundsError.class)
	public void testSubscript1() {
		runTest("[1,2][5];");
	}
	
	@Test(expected=IndexOutOfBoundsError.class)
	public void testHead1() {
		prepare("import List;");
		runTest("head([], 2);");
	}
	
	@Test(expected=IndexOutOfBoundsError.class)
	public void testHead2() {
		prepare("import List;");
		runTest("head([1,2,3], 4);");
	}
	

	@Test(expected=IndexOutOfBoundsError.class)
	public void testInsertAt() {
		prepare("import List;");
		runTest("insertAt([1,2,3], 4, 5);");
	}
	
	@Test(expected=IndexOutOfBoundsError.class)
	public void testTail1() {
		prepare("import List;");
		runTest("tail([]);");
	}
	
	@Test(expected=IndexOutOfBoundsError.class)
	public void testTail2() {
		prepare("import List;");
		runTest("tail([1,2,3], 4);");
	}
}
