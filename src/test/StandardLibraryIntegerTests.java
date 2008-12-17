package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryIntegerTests extends TestCase {
	
	private TestFramework tf = new TestFramework();

		public void testInteger() throws IOException {
		
		tf.prepare("import Integer;");
		
		System.err.println("Integer::arb");
		
		assertTrue(tf.runTestInSameEvaluator("{int N = Integer::arb(10); (N >= 0) && (N < 10);};"));
		assertTrue(tf.runTestInSameEvaluator("{int N = arb(10); (N >= 0) && (N < 10);};"));
		
		System.err.println("Integer::max");
		
		assertTrue(tf.runTestInSameEvaluator("Integer::max(3, 10) == 10;"));
		assertTrue(tf.runTestInSameEvaluator("max(3, 10) == 10;"));
		assertTrue(tf.runTestInSameEvaluator("Integer::max(10, 10) == 10;"));
		
		System.err.println("Integer::min");
		
		assertTrue(tf.runTestInSameEvaluator("Integer::min(3, 10) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("min(3, 10) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("Integer::min(10, 10) == 10;"));
		
		System.err.println("Integer::toDouble");
		
		assertTrue(tf.runTestInSameEvaluator("Integer::toDouble(3) == 3.0;"));
		assertTrue(tf.runTestInSameEvaluator("toDouble(3) == 3.0;"));
		
		System.err.println("Integer::toString");
		
		assertTrue(tf.runTestInSameEvaluator("Integer::toString(314) == \"314\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(314) == \"314\";"));
		
	}
	
}
