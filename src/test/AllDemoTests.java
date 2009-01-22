package test;

import java.io.IOException;

import junit.framework.TestCase;

public class AllDemoTests extends TestCase {
private static TestFramework tf = new TestFramework();
	
	public void testBoolAbstractRules() throws IOException {
		tf = new TestFramework("import Bool-abstract-rules;");
		assertTrue(tf.runTestInSameEvaluator("Bool-abstract-rules::test();"));
	}
	
	public void testBoolAbstractVisit() throws IOException {
		tf = new TestFramework("import Bool-abstract-visit;");
		assertTrue(tf.runTestInSameEvaluator("Bool-abstract-visit::test();"));
	}
	
	public void testIntegerAbstractRules() throws IOException {
		tf = new TestFramework("import Integer-abstract-rules;");
		assertTrue(tf.runTestInSameEvaluator("Integer-abstract-rules::test();"));
	}
	
	public void testBubble() throws IOException {
		tf = new TestFramework("import Bubble;");
		assertTrue(tf.runTestInSameEvaluator("Bubble::test();"));
	}
	
	public void testCalls() throws IOException {
		tf = new TestFramework("import Calls;");
		assertTrue(tf.runTestInSameEvaluator("Calls::test();"));
	}
	
	public void testCarFDL() throws IOException {
		tf = new TestFramework("import CarFDL;");
		assertTrue(tf.runTestInSameEvaluator("CarFDL::test();"));
	}
	
	public void testDominators() throws IOException {
		tf = new TestFramework("import Dominators;");
		assertTrue(tf.runTestInSameEvaluator("Dominators::test();"));
	}
	
	public void testGraphDataType() throws IOException {
		tf = new TestFramework("import GraphDataType;");
		assertTrue(tf.runTestInSameEvaluator("GraphDataType::test();"));
	}
	
	public void testInnerproduct() throws IOException {
		tf = new TestFramework("import Innerproduct;");
		assertTrue(tf.runTestInSameEvaluator("Innerproduct::test();"));
	}
	
	public void testLift() throws IOException {
		tf = new TestFramework("import Lift;");
		assertTrue(tf.runTestInSameEvaluator("Lift::test();"));
	}
	
	public void testTrans() throws IOException {
		tf = new TestFramework("import Trans;");
		assertTrue(tf.runTestInSameEvaluator("Trans::test();"));
	}
	
	public void testAckermann() throws IOException {
		tf = new TestFramework("import Ackermann;");
		assertTrue(tf.runTestInSameEvaluator("Ackermann::test();"));
	}
	
	public void testWordcount() throws IOException {
		tf = new TestFramework("import WordCount;");
		assertTrue(tf.runTestInSameEvaluator("WordCount::test();"));
	}
	
	public void testFunAbstract() throws IOException {
		tf = new TestFramework("import Fun-abstract;");
		assertTrue(tf.runTestInSameEvaluator("Fun-abstract::test();"));
	}
	
	public void testMEPT() throws IOException {
		tf = new TestFramework("import MEPT;");
		assertTrue(tf.runTestInSameEvaluator("MEPT::test();"));
	}
	
	
}
