package test;

import java.io.IOException;

import junit.framework.TestCase;

public class AllBenchmarks extends TestCase {
private static TestFramework tf = new TestFramework();
	
	public void testBubbleSort() throws IOException {
		tf = new TestFramework("import BubbleSort;");
		assertTrue(tf.runTestInSameEvaluator("BubbleSort::measure();"));
	}
	
	public void testFactorial() throws IOException {
		tf = new TestFramework("import Factorial;");
		assertTrue(tf.runTestInSameEvaluator("Factorial::measure();"));
	}
	
	public void testFibonacci() throws IOException {
		tf = new TestFramework("import Fibonacci;");
		assertTrue(tf.runTestInSameEvaluator("Fibonacci::measure();"));
	}
	
	public void testReverse() throws IOException {
		tf = new TestFramework("import Reverse;");
		assertTrue(tf.runTestInSameEvaluator("Reverse::measure();"));
	}
}
