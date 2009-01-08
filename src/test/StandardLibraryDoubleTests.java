package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryDoubleTests extends TestCase {

	private static TestFramework tf = new TestFramework("import Double;");

	public void testDoubleArb() throws IOException {
		
		tf = new TestFramework("import Double;");

		assertTrue(tf
				.runTestInSameEvaluator("{double D = Double::arbDouble(); (D >= 0.0) && (D <= 1.0);}"));
		assertTrue(tf
				.runTestInSameEvaluator("{double D = arbDouble(); (D >= 0.0) && (D <= 1.0);}"));
	}

	public void testDoubleMax() throws IOException {
		
		tf = new TestFramework("import Double;");

		assertTrue(tf.runTestInSameEvaluator("Double::max(3.0, 10.0) == 10.0;"));
		assertTrue(tf.runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(tf
				.runTestInSameEvaluator("Double::max(10.0, 10.0) == 10.0;"));
	}

	public void testDoubleMin() throws IOException {
		
		tf = new TestFramework("import Double;");

		assertTrue(tf.runTestInSameEvaluator("Double::min(3.0, 10.0) == 3.0;"));
		assertTrue(tf.runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(tf
				.runTestInSameEvaluator("Double::min(10.0, 10.0) == 10.0;"));
	}

	public void testDoubleToInteger() throws IOException {
		
		tf = new TestFramework("import Double;");

		assertTrue(tf.runTestInSameEvaluator("Double::toInteger(3.14) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("toInteger(3.14) == 3;"));
	}

	public void testDoubleToString() throws IOException {
		
		tf = new TestFramework("import Double;");

		assertTrue(tf
				.runTestInSameEvaluator("Double::toString(3.14) == \"3.14\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(3.14) == \"3.14\";"));

	}
}
