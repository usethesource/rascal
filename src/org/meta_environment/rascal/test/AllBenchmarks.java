package org.meta_environment.rascal.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AllBenchmarks extends TestFramework {
	@Test
	public void BubbleSort() {
		prepare("import benchmark::BubbleSort::BubbleSort;");
		assertTrue(runTestInSameEvaluator("benchmark::BubbleSort::BubbleSort::measure();"));
	}

	@Test
	public void Factorial() {
		prepare("import benchmark::Factorial::Factorial;");
		assertTrue(runTestInSameEvaluator("benchmark::Factorial::Factorial::measure();"));
	}

	@Test
	public void Fibonacci() {
		prepare("import benchmark::Fibonacci::Fibonacci;");
		assertTrue(runTestInSameEvaluator("benchmark::Fibonacci::Fibonacci::measure();"));
	}

	@Test
	public void Reverse() {
		prepare("import benchmark::Reverse::Reverse;");
		assertTrue(runTestInSameEvaluator("benchmark::Reverse::Reverse::measure();"));
	}
	
	@Test
	public void RSFCalls() {
		prepare("import benchmark::RSF::RSFCalls;");
		assertTrue(runTestInSameEvaluator("benchmark::RSF::RSFCalls::measureOne();"));
	}
}
