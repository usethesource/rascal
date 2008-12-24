package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryBooleanTests extends TestCase {

	private static TestFramework tf = new TestFramework("import Boolean;");

	public void testBooleanArb() throws IOException {

		assertTrue(tf
				.runTestInSameEvaluator("{bool B = Boolean::arbBool(); (B == true) || (B == false);}"));
		assertTrue(tf
				.runTestInSameEvaluator("{bool B = arbBool(); (B == true) || (B == false);}"));
	}

	public void testBooleanToInt() throws IOException {

		assertTrue(tf.runTestInSameEvaluator("Boolean::toInt(false) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("Boolean::toInt(true) == 1;"));

		assertTrue(tf.runTestInSameEvaluator("toInt(false) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("toInt(true) == 1;"));
	}

	public void testBooleanToDouble() throws IOException {

		assertTrue(tf
				.runTestInSameEvaluator("Boolean::toDouble(false) == 0.0;"));
		assertTrue(tf.runTestInSameEvaluator("Boolean::toDouble(true) == 1.0;"));

		assertTrue(tf.runTestInSameEvaluator("toDouble(false) == 0.0;"));
		assertTrue(tf.runTestInSameEvaluator("toDouble(true) == 1.0;"));
	}

	public void testBooleanToString() throws IOException {

		assertTrue(tf
				.runTestInSameEvaluator("Boolean::toString(false) == \"false\";"));
		assertTrue(tf
				.runTestInSameEvaluator("Boolean::toString(true) == \"true\";"));

		assertTrue(tf.runTestInSameEvaluator("toString(false) == \"false\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(true) == \"true\";"));
	}
}
