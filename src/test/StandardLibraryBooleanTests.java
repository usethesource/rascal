package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryBooleanTests extends TestCase {
	
	private TestFramework tf = new TestFramework();

	public void testBoolean() throws IOException {
		
		tf.prepare("import Boolean;");
		
		System.err.println("Boolean::arbBool");
		
		assertTrue(tf.runTestInSameEvaluator("{bool B = Boolean::arbBool(); (B == true) || (B == false);};"));
		assertTrue(tf.runTestInSameEvaluator("{bool B = arbBool(); (B == true) || (B == false);};"));
		
		System.err.println("Boolean::toInt");
		
		assertTrue(tf.runTestInSameEvaluator("Boolean::toInt(false) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("Boolean::toInt(true) == 1;"));
		
		assertTrue(tf.runTestInSameEvaluator("toInt(false) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("toInt(true) == 1;"));
		
		System.err.println("Boolean::toDouble");
		
		assertTrue(tf.runTestInSameEvaluator("Boolean::toDouble(false) == 0.0;"));
		assertTrue(tf.runTestInSameEvaluator("Boolean::toDouble(true) == 1.0;"));
		
		assertTrue(tf.runTestInSameEvaluator("toDouble(false) == 0.0;"));
		assertTrue(tf.runTestInSameEvaluator("toDouble(true) == 1.0;"));
		
		System.err.println("Boolean::toString");
		
		assertTrue(tf.runTestInSameEvaluator("Boolean::toString(false) == \"false\";"));
		assertTrue(tf.runTestInSameEvaluator("Boolean::toString(true) == \"true\";"));
		
		assertTrue(tf.runTestInSameEvaluator("toString(false) == \"false\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(true) == \"true\";"));
	}
}
