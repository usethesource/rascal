package test;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AllBenchmarks extends TestFramework {

	@Test
	public void testBubbleSort() {
		prepare("import BubbleSort;");
		assertTrue(runTestInSameEvaluator("BubbleSort::measure();"));
	}

	@Test
	public void testFactorial() {
		prepare("import Factorial;");
		assertTrue(runTestInSameEvaluator("Factorial::measure();"));
	}

	@Test
	public void testFibonacci() {
		prepare("import Fibonacci;");
		assertTrue(runTestInSameEvaluator("Fibonacci::measure();"));
	}

	@Test
	public void testReverse() {
		prepare("import Reverse;");
		assertTrue(runTestInSameEvaluator("Reverse::measure();"));
	}
}
