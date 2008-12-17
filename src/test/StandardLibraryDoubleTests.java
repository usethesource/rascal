package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryDoubleTests extends TestCase {
	private TestFramework tf = new TestFramework();
	
	public void testDouble() throws IOException {
		
		tf.prepare("import Double;");
		
		System.err.println("Double::arb");
		
		assertTrue(tf.runTestInSameEvaluator("{double D = Double::arbDouble(); (D >= 0.0) && (D <= 1.0);};"));
		assertTrue(tf.runTestInSameEvaluator("{double D = arbDouble(); (D >= 0.0) && (D <= 1.0);};"));
		
		System.err.println("Double::max");
		
		assertTrue(tf.runTestInSameEvaluator("Double::max(3.0, 10.0) == 10.0;"));
		assertTrue(tf.runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(tf.runTestInSameEvaluator("Double::max(10.0, 10.0) == 10.0;"));
		
		System.err.println("Double::min");
		
		assertTrue(tf.runTestInSameEvaluator("Double::min(3.0, 10.0) == 3.0;"));
		assertTrue(tf.runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(tf.runTestInSameEvaluator("Double::min(10.0, 10.0) == 10.0;"));
		
		System.err.println("Double::toInteger");
		
		assertTrue(tf.runTestInSameEvaluator("Double::toInteger(3.14) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("toInteger(3.14) == 3;"));
		
		System.err.println("Double::toString");
		
		assertTrue(tf.runTestInSameEvaluator("Double::toString(3.14) == \"3.14\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(3.14) == \"3.14\";"));
		
	}
}
