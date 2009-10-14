package org.meta_environment.rascal.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AllBenchmarks extends TestFramework {
	@Test
	public void BubbleSort() {
		prepare("import BubbleSort::BubbleSort;");
		assertTrue(runTestInSameEvaluator("BubbleSort::BubbleSort::measure();"));
	}

	@Test
	public void Factorial() {
		prepare("import Factorial::Factorial;");
		assertTrue(runTestInSameEvaluator("Factorial::Factorial::measure();"));
	}

	@Test
	public void Fibonacci() {
		prepare("import Fibonacci::Fibonacci;");
		assertTrue(runTestInSameEvaluator("Fibonacci::Fibonacci::measure();"));
	}

	@Test
	public void Reverse() {
		prepare("import Reverse::Reverse;");
		assertTrue(runTestInSameEvaluator("Reverse::Reverse::measure();"));
	}
	
	@Test
	public void RSFCalls() {
		prepare("import RSF::RSFCalls;");
		assertTrue(runTestInSameEvaluator("RSF::RSFCalls::measureOne();"));
	}
}
