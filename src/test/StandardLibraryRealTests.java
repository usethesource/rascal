package test;

import org.junit.Test;
import static org.junit.Assert.*;

public class StandardLibraryRealTests extends TestFramework {

	@Test
	public void testRealArb() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("{real D = Real::arbReal(); (D >= 0.0) && (D <= 1.0);}"));
		assertTrue(runTestInSameEvaluator("{real D = arbReal(); (D >= 0.0) && (D <= 1.0);}"));
	}

	@Test
	public void testRealMax() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("Real::max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("Real::max(10.0, 10.0) == 10.0;"));
	}

	@Test
	public void testRealMin() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("Real::min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("Real::min(10.0, 10.0) == 10.0;"));
	}

	@Test
	public void testRealToInteger() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("Real::toInteger(3.14) == 3;"));
		assertTrue(runTestInSameEvaluator("toInteger(3.14) == 3;"));
	}

	@Test
	public void testRealToString() {

		prepare("import Real;");

		assertTrue(runTestInSameEvaluator("Real::toString(3.14) == \"3.14\";"));
		assertTrue(runTestInSameEvaluator("toString(3.14) == \"3.14\";"));

	}
}
