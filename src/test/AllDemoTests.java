package test;

import java.io.IOException;

import junit.framework.TestCase;

public class AllDemoTests extends TestCase {
private static TestFramework tf = new TestFramework();
	
	public void testBoolAbstractRules() throws IOException {
		tf = new TestFramework("import Bool-abstract-rules;");
		assertTrue(tf.runTestInSameEvaluator("testBool-abstract-rules() == true;"));
	}
	
	public void testBoolAbstractVisit() throws IOException {
		tf = new TestFramework("import Bool-abstract-visit;");
		assertTrue(tf.runTestInSameEvaluator("testBool-abstract-visit() == true;"));
	}
	
	public void testIntegerAbstractRules() throws IOException {
		tf = new TestFramework("import Integer-abstract-rules;");
		assertTrue(tf.runTestInSameEvaluator("testInteger-abstract-rules() == true;"));
	}
	
	public void testCalls() throws IOException {
		tf = new TestFramework("import Calls;");
		assertTrue(tf.runTestInSameEvaluator("testCalls() == true;"));
	}
	
	public void testCarFDL() throws IOException {
		tf = new TestFramework("import CarFDL;");
		assertTrue(tf.runTestInSameEvaluator("testCarFDL() == true;"));
	}
	
	public void testLift() throws IOException {
		tf = new TestFramework("import Lift;");
		assertTrue(tf.runTestInSameEvaluator("testLift() == true;"));
	}
	
	public void testTrans() throws IOException {
		tf = new TestFramework("import Trans;");
		assertTrue(tf.runTestInSameEvaluator("testTrans() == true;"));
	}
}
