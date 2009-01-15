package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryRealTests extends TestCase {

	private static TestFramework tf = new TestFramework("import Real;");

	public void testRealArb() throws IOException {
		
		tf = new TestFramework("import Real;");

		assertTrue(tf
				.runTestInSameEvaluator("{real D = Real::arbReal(); (D >= 0.0) && (D <= 1.0);}"));
		assertTrue(tf
				.runTestInSameEvaluator("{real D = arbReal(); (D >= 0.0) && (D <= 1.0);}"));
	}

	public void testRealMax() throws IOException {
		
		tf = new TestFramework("import Real;");

		assertTrue(tf.runTestInSameEvaluator("Real::max(3.0, 10.0) == 10.0;"));
		assertTrue(tf.runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(tf
				.runTestInSameEvaluator("Real::max(10.0, 10.0) == 10.0;"));
	}

	public void testRealMin() throws IOException {
		
		tf = new TestFramework("import Real;");

		assertTrue(tf.runTestInSameEvaluator("Real::min(3.0, 10.0) == 3.0;"));
		assertTrue(tf.runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(tf
				.runTestInSameEvaluator("Real::min(10.0, 10.0) == 10.0;"));
	}

	public void testRealToInteger() throws IOException {
		
		tf = new TestFramework("import Real;");

		assertTrue(tf.runTestInSameEvaluator("Real::toInteger(3.14) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("toInteger(3.14) == 3;"));
	}

	public void testRealToString() throws IOException {
		
		tf = new TestFramework("import Real;");

		assertTrue(tf
				.runTestInSameEvaluator("Real::toString(3.14) == \"3.14\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(3.14) == \"3.14\";"));

	}
}
