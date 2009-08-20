package test.StandardLibraryTests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import test.TestFramework;

public class RealTests extends TestFramework {

	@Test
	public void arbReal() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("{real D = Real::arbReal(); (D >= 0.0) && (D <= 1.0);}"));
		assertTrue(runTestInSameEvaluator("{real D = arbReal(); (D >= 0.0) && (D <= 1.0);}"));
	}

	@Test
	public void max() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("Real::max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("Real::max(10.0, 10.0) == 10.0;"));
	}

	@Test
	public void min() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("Real::min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("Real::min(10.0, 10.0) == 10.0;"));
	}

	@Test
	public void toInt() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("Real::toInt(3.14) == 3;"));
		assertTrue(runTestInSameEvaluator("toInt(3.14) == 3;"));
	}

	@Test
	public void testToString() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("Real::toString(3.14) == \"3.14\";"));
		assertTrue(runTestInSameEvaluator("toString(3.14) == \"3.14\";"));

	}
}
