package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryIntegerTests extends TestCase {

	private static TestFramework tf = new TestFramework("import Integer;");

	public void testIntegerArbInt() throws IOException {
		
		new TestFramework("import Integer;");

		assertTrue(tf
				.runTestInSameEvaluator("{int N = Integer::arbInt(10); (N >= 0) && (N < 10);}"));
		assertTrue(tf
				.runTestInSameEvaluator("{int N = arbInt(10); (N >= 0) && (N < 10);}"));
		
		assertTrue(tf
				.runTestInSameEvaluator("{int N = arbInt(); true;}"));
	}

	public void testIntegerMax() throws IOException {
		
		new TestFramework("import Integer;");

		assertTrue(tf.runTestInSameEvaluator("Integer::max(3, 10) == 10;"));
		assertTrue(tf.runTestInSameEvaluator("max(3, 10) == 10;"));
		assertTrue(tf.runTestInSameEvaluator("Integer::max(10, 10) == 10;"));
	}

	public void testIntegerMin() throws IOException {
		
		new TestFramework("import Integer;");

		assertTrue(tf.runTestInSameEvaluator("Integer::min(3, 10) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("min(3, 10) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("Integer::min(10, 10) == 10;"));
	}

	public void testIntegerToReal() throws IOException {
		
		new TestFramework("import Integer;");

		assertTrue(tf.runTestInSameEvaluator("Integer::toReal(3) == 3.0;"));
		assertTrue(tf.runTestInSameEvaluator("toReal(3) == 3.0;"));
	}

	public void testIntegerToString() throws IOException {
		
		new TestFramework("import Integer;");

		assertTrue(tf
				.runTestInSameEvaluator("Integer::toString(314) == \"314\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(314) == \"314\";"));
	}

}
