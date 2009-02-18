package test;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AllBenchmarks extends TestFramework {

	@Test
	public void BubbleSort() {
		prepare("import BubbleSort;");
		assertTrue(runTestInSameEvaluator("BubbleSort::measure();"));
	}

	@Test
	public void Factorial() {
		prepare("import Factorial;");
		assertTrue(runTestInSameEvaluator("Factorial::measure();"));
	}

	@Test
	public void Fibonacci() {
		prepare("import Fibonacci;");
		assertTrue(runTestInSameEvaluator("Fibonacci::measure();"));
	}

	@Test
	public void Reverse() {
		prepare("import Reverse;");
		assertTrue(runTestInSameEvaluator("Reverse::measure();"));
	}
}
