package test.ErrorTests;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.*;

import test.TestFramework;

public class EmptyListErrorTests extends TestFramework {
	
	@Test(expected=EmptyListError.class)
	public void testHead1() {
		runTest("import List;", "head([]);");
	}
	
	@Test(expected=IndexOutOfBoundsError.class)
	public void testHead2() {
		prepare("import List;");
		runTestInSameEvaluator("head([],3);");
	}
	
	@Test(expected=EmptyListError.class)
	public void testGetOneFrom() {
		prepare("import List;");
		runTestInSameEvaluator("getOneFrom([]);");
	}
	
	@Test(expected=EmptyListError.class)
	public void testTakeOneFrom() {
		prepare("import List;");
		runTestInSameEvaluator("takeOneFrom([]);");
	}
}
